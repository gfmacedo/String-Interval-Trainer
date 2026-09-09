package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

class SynthesizerAudioEngine {

    private val sampleRate = 44100

    /**
     * Synthesizes an acoustic pluck tone with pluck envelope harmonics
     */
    fun playPluck(frequency: Float, durationMs: Int = 380, scope: CoroutineScope) {
        if (frequency <= 20f || frequency > 5000f) return

        scope.launch(Dispatchers.Default) {
            try {
                val numSamples = (sampleRate * (durationMs / 1000f)).toInt()
                val buffer = ShortArray(numSamples)

                val dt = 2.0 * PI * frequency / sampleRate
                val decayRate = 5.0 / (durationMs / 1000.0)

                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    val envelope = exp(-decayRate * t)

                    // Fundamental + harmonics for rich string guitar tone
                    val fundamental = sin(i * dt)
                    val secondHarmonic = 0.5 * sin(i * dt * 2.0)
                    val thirdHarmonic = 0.25 * sin(i * dt * 3.0)
                    val fourthHarmonic = 0.12 * sin(i * dt * 4.0)

                    val sampleVal = (fundamental + secondHarmonic + thirdHarmonic + fourthHarmonic) * envelope
                    val normalized = (sampleVal * 0.45 * Short.MAX_VALUE).toInt()
                    buffer[i] = normalized.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()

                // Release after playback finished
                kotlinx.coroutines.delay(durationMs.toLong() + 100L)
                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
                // Ignore audio playback exceptions
            }
        }
    }

    /**
     * Plays a pleasant dual-tone ascending chime for correct note validation
     */
    fun playSuccessChime(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            playPluck(587.33f, durationMs = 180, scope = this) // D5
            kotlinx.coroutines.delay(100L)
            playPluck(880.00f, durationMs = 300, scope = this) // A5
        }
    }

    /**
     * Plays a low gentle dull click tone for incorrect attempt
     */
    fun playIncorrectTone(scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            playPluck(130.81f, durationMs = 120, scope = this) // C3
        }
    }
}
