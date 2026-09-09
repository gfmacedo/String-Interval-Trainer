package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PitchDetectorEngine
import com.example.data.model.ExerciseSessionState

@Composable
fun VUMeterAndStatusBadge(
    vuLevel: Float, // 0.0f to 1.0f
    audioHealth: PitchDetectorEngine.AudioHealthStatus,
    lastEvaluation: ExerciseSessionState.PitchEvaluated?,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF131D31))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("vu_meter_status_badge")
    ) {
        // VU Meter Bars & Label
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = "VU Meter",
                tint = Color(0xFF38BDF8),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "INPUT LEVEL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Segmented LED Meter (12 segments)
            val segmentCount = 12
            val activeSegments = (vuLevel * segmentCount).toInt().coerceIn(0, segmentCount)

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until segmentCount) {
                    val isActive = i < activeSegments
                    val segmentColor = when {
                        !isActive -> Color(0xFF1E293B)
                        i < 8 -> Color(0xFF10B981) // Green
                        i < 10 -> Color(0xFFF59E0B) // Amber
                        else -> Color(0xFFEF4444) // Red peak
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(2.dp))
                            .background(segmentColor)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Evaluation Status Feedback Bar
        val isRecent = lastEvaluation != null && (System.currentTimeMillis() - lastEvaluation.timestamp < 1800L)

        val statusBackgroundColor by animateColorAsState(
            targetValue = when {
                isRecent && lastEvaluation?.isCorrect == true -> Color(0xFF064E3B)
                isRecent && lastEvaluation?.isCorrect == false -> Color(0xFF7F1D1D)
                audioHealth == PitchDetectorEngine.AudioHealthStatus.BLOCKED -> Color(0xFF451A03)
                else -> Color(0xFF1E293B)
            },
            animationSpec = tween(150, easing = FastOutSlowInEasing),
            label = "statusBg"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(statusBackgroundColor)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                isRecent && lastEvaluation?.isCorrect == true -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Correct",
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pitch Correct: ${lastEvaluation.detectedPitchClass.standardLabel}",
                        color = Color(0xFF6EE7B7),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "NEXT ►",
                        color = Color(0xFF34D399),
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
                isRecent && lastEvaluation?.isCorrect == false -> {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Incorrect",
                        tint = Color(0xFFF87171),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Played ${lastEvaluation.detectedPitchClass.standardLabel} (Waiting for target...)",
                        color = Color(0xFFFCA5A5),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                audioHealth == PitchDetectorEngine.AudioHealthStatus.BLOCKED -> {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Mic Blocked",
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Microphone inactive. Tap any fret below to answer!",
                        color = Color(0xFFFDE68A),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                else -> {
                    // Listening waveform pulse
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF38BDF8).copy(alpha = pulseAlpha))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Listening for pitch (or tap fret below)...",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
