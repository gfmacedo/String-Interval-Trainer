package com.example.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.example.data.model.PitchClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.log2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class PitchEvent(
    val frequency: Float,
    val midiNote: Int,
    val pitchClass: PitchClass,
    val confidence: Float,
    val rmsDb: Float,
    val timestamp: Long = System.currentTimeMillis()
)

class PitchDetectorEngine(
    private val onPitchDetected: (PitchEvent) -> Unit,
    private val onVuLevelChanged: (Float) -> Unit, // 0.0f to 1.0f
    private val onStatusChanged: (AudioHealthStatus) -> Unit
) {
    enum class AudioHealthStatus {
        ACTIVE,      // Active microphone listening
        HIGH_NOISE,  // High noise floor
        BLOCKED      // Input blocked / permission not granted
    }

    private var recordingJob: Job? = null
    private var audioRecord: AudioRecord? = null

    // Audio Settings
    var pitchReference: Float = 440.0f
    var inputSensitivity: Float = 2.2f // Boosted gain for quieter guitars
    var noiseGateThresholdDb: Float = -45.0f // In dB
    var isBassFilterEnabled: Boolean = false
    var usbChannel: Int = 1 // 1 for left/ch1, 2 for right/ch2, 0 for mono default

    private val sampleRate = 44100
    private val bufferSize = 2048 // ~46ms window for low guitar pitches (E2 ~ 82Hz)
    private val hopSize = 1024    // ~23ms hop for fast, low-latency evaluation

    // Fast & Accurate Multi-Frame Pitch Stability Tracking
    private var candidatePitchClass: PitchClass? = null
    private var candidateStreak: Int = 0
    private var candidateMidi: Int = -1
    private var candidateFreq: Float = 0f
    private var lastEmittedPitchClass: PitchClass? = null
    private var lastEmittedTime: Long = 0L

    fun clearTransientState() {
        candidatePitchClass = null
        candidateStreak = 0
        candidateMidi = -1
        candidateFreq = 0f
        lastEmittedPitchClass = null
        lastEmittedTime = 0L
    }

    @SuppressLint("MissingPermission")
    fun start(scope: CoroutineScope) {
        stop()
        clearTransientState()

        try {
            val minBufSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val actualBufSize = max(bufferSize * 4, minBufSize)

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                actualBufSize
            )

            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                onStatusChanged(AudioHealthStatus.BLOCKED)
                return
            }

            audioRecord?.startRecording()
            onStatusChanged(AudioHealthStatus.ACTIVE)

            recordingJob = scope.launch(Dispatchers.Default) {
                val hopAudioBuffer = ShortArray(hopSize)
                val slidingFloatBuffer = FloatArray(bufferSize)

                while (isActive) {
                    val readSamples = audioRecord?.read(hopAudioBuffer, 0, hopSize) ?: -1
                    if (readSamples <= 0) continue

                    // Shift slidingFloatBuffer left by readSamples
                    System.arraycopy(slidingFloatBuffer, readSamples, slidingFloatBuffer, 0, bufferSize - readSamples)

                    // Normalize incoming new samples and append to end of slidingFloatBuffer
                    var hopSumSquares = 0.0
                    val insertOffset = bufferSize - readSamples
                    for (i in 0 until readSamples) {
                        var sample = hopAudioBuffer[i].toFloat() * inputSensitivity
                        if (sample > 32767f) sample = 32767f
                        if (sample < -32768f) sample = -32768f
                        val norm = sample / 32768.0f
                        slidingFloatBuffer[insertOffset + i] = norm
                        hopSumSquares += norm * norm
                    }

                    val rms = sqrt(hopSumSquares / readSamples).toFloat()
                    val rmsDb = if (rms > 0.00001f) 20f * kotlin.math.log10(rms) else -90f

                    // Map rms (-60dB to 0dB) to 0.0..1.0 for VU meter
                    val vuLevel = ((rmsDb + 60f) / 60f).coerceIn(0f, 1f)
                    onVuLevelChanged(vuLevel)

                    // Noise gate check: reject room noise immediately without false positives
                    if (rmsDb < noiseGateThresholdDb) {
                        candidateStreak = 0
                        candidatePitchClass = null
                        continue
                    }

                    if (rmsDb > -12f && rms > 0.6f) {
                        onStatusChanged(AudioHealthStatus.HIGH_NOISE)
                    } else {
                        onStatusChanged(AudioHealthStatus.ACTIVE)
                    }

                    // YIN pitch detection on full 2048-sample analysis window
                    val pitchResult = detectPitchYin(slidingFloatBuffer, bufferSize, sampleRate)

                    if (pitchResult != null && pitchResult.second >= 0.74f) {
                        val freq = pitchResult.first
                        val confidence = pitchResult.second

                        // Filter by instrument frequency bounds:
                        // Guitar: E2 (82.4Hz) to E6 (1318.5Hz)
                        // Bass: B0 (30.87Hz) to G4 (392Hz)
                        val minFreq = if (isBassFilterEnabled) 30.0f else 75.0f
                        val maxFreq = if (isBassFilterEnabled) 420.0f else 1400.0f

                        if (freq in minFreq..maxFreq) {
                            val midiFloat = 69f + 12f * log2(freq / pitchReference)
                            val midiNote = midiFloat.roundToInt()
                            val pitchClass = PitchClass.fromMidi(midiNote)
                            val now = System.currentTimeMillis()

                            // Track stability at PitchClass level:
                            // Prevents guitar pick attack octave transients (e.g. E2 to E3 harmonic jumps)
                            // from resetting detection, avoiding false negatives on string attacks!
                            if (pitchClass == candidatePitchClass) {
                                candidateStreak++
                                candidateMidi = midiNote
                                candidateFreq = freq
                            } else {
                                candidatePitchClass = pitchClass
                                candidateStreak = 1
                                candidateMidi = midiNote
                                candidateFreq = freq
                            }

                            // Dynamic confirmation window:
                            // Ultra-high confidence (>= 0.84): only 2 frames (~46ms) needed!
                            // Solid confidence (>= 0.74): 3 frames (~69ms) needed!
                            val requiredStreak = if (confidence >= 0.84f) 2 else 3

                            if (candidateStreak >= requiredStreak) {
                                val isNewPitch = pitchClass != lastEmittedPitchClass
                                val timeSinceLastEmit = now - lastEmittedTime

                                // Emit IMMEDIATELY on new confirmed pitch!
                                // While sustaining the same note, throttle emit to once every 160ms
                                if (isNewPitch || timeSinceLastEmit >= 160L) {
                                    lastEmittedPitchClass = pitchClass
                                    lastEmittedTime = now

                                    val event = PitchEvent(
                                        frequency = candidateFreq,
                                        midiNote = candidateMidi,
                                        pitchClass = pitchClass,
                                        confidence = confidence,
                                        rmsDb = rmsDb,
                                        timestamp = now
                                    )
                                    onPitchDetected(event)
                                }
                            }
                        }
                    } else {
                        // Drop streak if signal is transient or unclear
                        candidateStreak = 0
                        if (System.currentTimeMillis() - lastEmittedTime > 250L) {
                            lastEmittedPitchClass = null
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("PitchDetectorEngine", "Error initializing audio capture", e)
            onStatusChanged(AudioHealthStatus.BLOCKED)
        }
    }

    fun stop() {
        try {
            recordingJob?.cancel()
            recordingJob = null
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
            onVuLevelChanged(0f)
        } catch (e: Exception) {
            Log.e("PitchDetectorEngine", "Error stopping audio capture", e)
        }
    }

    /**
     * YIN Pitch Detection Algorithm implementation
     * Step 1: Difference function
     * Step 2: Cumulative mean normalized difference function (CMNDF)
     * Step 3: Absolute thresholding
     * Step 4: Parabolic interpolation
     */
    private fun detectPitchYin(buffer: FloatArray, bufferLength: Int, sampleRate: Int): Pair<Float, Float>? {
        val halfLength = bufferLength / 2
        // Limit tau search to instrument frequency bounds:
        // Guitar: lowest note E2 ~ 82.4Hz, so 70Hz -> tau ~ 630 samples
        // Bass: lowest note B0 ~ 30.87Hz -> tau up to halfLength
        val maxTau = if (isBassFilterEnabled) halfLength else min(halfLength, sampleRate / 70)
        val yinBuffer = FloatArray(maxTau)

        // Step 1: Difference function
        for (tau in 0 until maxTau) {
            var diff = 0f
            for (i in 0 until halfLength) {
                val delta = buffer[i] - buffer[i + tau]
                diff += delta * delta
            }
            yinBuffer[tau] = diff
        }

        // Step 2: Cumulative Mean Normalized Difference Function
        yinBuffer[0] = 1f
        var runningSum = 0f
        for (tau in 1 until maxTau) {
            runningSum += yinBuffer[tau]
            yinBuffer[tau] = if (runningSum > 0f) {
                yinBuffer[tau] * tau / runningSum
            } else {
                1f
            }
        }

        // Step 3: Absolute Thresholding & Local Minimum Dip Finder
        val threshold = 0.20f // Standard YIN threshold
        var tauEstimate = -1
        for (tau in 2 until maxTau) {
            if (yinBuffer[tau] < threshold) {
                var localMin = tau
                while (localMin + 1 < maxTau && yinBuffer[localMin + 1] < yinBuffer[localMin]) {
                    localMin++
                }
                tauEstimate = localMin
                break
            }
        }

        if (tauEstimate == -1) {
            // Find global minimum if below fallback threshold
            var minVal = Float.MAX_VALUE
            var minTau = -1
            for (tau in 4 until maxTau) {
                if (yinBuffer[tau] < minVal) {
                    minVal = yinBuffer[tau]
                    minTau = tau
                }
            }
            if (minVal < 0.32f && minTau != -1) {
                tauEstimate = minTau
            } else {
                return null
            }
        }

        // Step 4: Parabolic Interpolation
        val betterTau = if (tauEstimate > 0 && tauEstimate < maxTau - 1) {
            val s0 = yinBuffer[tauEstimate - 1]
            val s1 = yinBuffer[tauEstimate]
            val s2 = yinBuffer[tauEstimate + 1]
            val denom = 2f * (2f * s1 - s0 - s2)
            if (abs(denom) > 1e-6f) {
                tauEstimate + (s2 - s0) / denom
            } else {
                tauEstimate.toFloat()
            }
        } else {
            tauEstimate.toFloat()
        }

        if (betterTau <= 0f) return null

        val frequency = sampleRate / betterTau
        val confidence = (1.0f - min(yinBuffer[tauEstimate], 1.0f)).coerceIn(0f, 1f)

        return Pair(frequency, confidence)
    }
}
