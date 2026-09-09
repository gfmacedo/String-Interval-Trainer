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
    var inputSensitivity: Float = 1.0f // Multiplier
    var noiseGateThresholdDb: Float = -45.0f // In dB
    var isBassFilterEnabled: Boolean = false
    var usbChannel: Int = 1 // 1 for left/ch1, 2 for right/ch2, 0 for mono default

    private val sampleRate = 44100
    private val bufferSize = 2048 // ~46ms window for lower guitar pitches (E2 ~ 82Hz requires min ~1076 samples at 44.1k)

    // Debounce & Stability
    private var lastStableMidi: Int = -1
    private var stableMidiStartTime: Long = 0L
    private val debounceWindowMs = 160L // 150-200ms debounce
    private var lastEmittedTime: Long = 0L

    @SuppressLint("MissingPermission")
    fun start(scope: CoroutineScope) {
        stop()

        try {
            val minBufSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            val actualBufSize = max(bufferSize * 2, minBufSize)

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
                val audioBuffer = ShortArray(bufferSize)
                val floatBuffer = FloatArray(bufferSize)

                while (isActive) {
                    val readSamples = audioRecord?.read(audioBuffer, 0, bufferSize) ?: -1
                    if (readSamples <= 0) continue

                    // Calculate RMS amplitude and normalize to float buffer
                    var sumSquares = 0.0
                    for (i in 0 until readSamples) {
                        var sample = audioBuffer[i].toFloat() * inputSensitivity
                        if (sample > 32767f) sample = 32767f
                        if (sample < -32768f) sample = -32768f
                        floatBuffer[i] = sample / 32768.0f
                        sumSquares += floatBuffer[i] * floatBuffer[i]
                    }

                    val rms = sqrt(sumSquares / readSamples).toFloat()
                    val rmsDb = if (rms > 0.00001f) 20f * kotlin.math.log10(rms) else -90f

                    // Map rms (-60dB to 0dB) to 0.0..1.0 for VU meter
                    val vuLevel = ((rmsDb + 60f) / 60f).coerceIn(0f, 1f)
                    onVuLevelChanged(vuLevel)

                    // Noise gate check
                    if (rmsDb < noiseGateThresholdDb) {
                        // Below noise gate, reset debounce tracker
                        lastStableMidi = -1
                        continue
                    }

                    if (rmsDb > -12f && rms > 0.6f) {
                        onStatusChanged(AudioHealthStatus.HIGH_NOISE)
                    } else {
                        onStatusChanged(AudioHealthStatus.ACTIVE)
                    }

                    // YIN pitch detection algorithm
                    val pitchResult = detectPitchYin(floatBuffer, readSamples, sampleRate)

                    if (pitchResult != null && pitchResult.second >= 0.78f) {
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
                            val now = System.currentTimeMillis()

                            if (midiNote == lastStableMidi) {
                                if (now - stableMidiStartTime >= debounceWindowMs) {
                                    if (now - lastEmittedTime > 120L) {
                                        lastEmittedTime = now
                                        val pitchClass = PitchClass.fromMidi(midiNote)
                                        val event = PitchEvent(
                                            frequency = freq,
                                            midiNote = midiNote,
                                            pitchClass = pitchClass,
                                            confidence = confidence,
                                            rmsDb = rmsDb,
                                            timestamp = now
                                        )
                                        onPitchDetected(event)
                                    }
                                }
                            } else {
                                lastStableMidi = midiNote
                                stableMidiStartTime = now
                            }
                        }
                    } else {
                        // Low confidence or transient
                        if (System.currentTimeMillis() - stableMidiStartTime > 400L) {
                            lastStableMidi = -1
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
        val yinBuffer = FloatArray(halfLength)

        // Step 1: Difference function
        for (tau in 0 until halfLength) {
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
        for (tau in 1 until halfLength) {
            runningSum += yinBuffer[tau]
            yinBuffer[tau] = if (runningSum > 0f) {
                yinBuffer[tau] * tau / runningSum
            } else {
                1f
            }
        }

        // Step 3: Absolute Thresholding
        val threshold = 0.20f // Lower value = higher pitch confidence
        var tauEstimate = -1
        for (tau in 2 until halfLength) {
            if (yinBuffer[tau] < threshold) {
                while (tau + 1 < halfLength && yinBuffer[tau + 1] < yinBuffer[tau]) {
                    tauEstimate = tau + 1
                    break
                }
                if (tauEstimate == -1) {
                    tauEstimate = tau
                }
                break
            }
        }

        if (tauEstimate == -1) {
            // Find global minimum if below fallback threshold
            var minVal = Float.MAX_VALUE
            var minTau = -1
            for (tau in 4 until halfLength) {
                if (yinBuffer[tau] < minVal) {
                    minVal = yinBuffer[tau]
                    minTau = tau
                }
            }
            if (minVal < 0.35f && minTau != -1) {
                tauEstimate = minTau
            } else {
                return null
            }
        }

        // Step 4: Parabolic Interpolation
        val betterTau = if (tauEstimate > 0 && tauEstimate < halfLength - 1) {
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
