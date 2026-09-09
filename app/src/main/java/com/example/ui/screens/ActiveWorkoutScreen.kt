package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExerciseSessionState
import com.example.ui.components.FretboardView
import com.example.ui.components.VUMeterAndStatusBadge
import com.example.ui.viewmodel.TrainerViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActiveWorkoutScreen(
    viewModel: TrainerViewModel,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sessionState by viewModel.sessionState.collectAsState()
    val vuLevel by viewModel.vuLevel.collectAsState()
    val audioHealth by viewModel.audioHealthStatus.collectAsState()
    val currentTuning by viewModel.currentTuning.collectAsState()
    val fretRange by viewModel.fretRange.collectAsState()
    val allowedStrings by viewModel.allowedStrings.collectAsState()
    val selectedFret by viewModel.selectedFret.collectAsState()
    val isMicListening by viewModel.isMicListening.collectAsState()

    val scrollState = rememberScrollState()

    // Workout completed: automatically logged to Room; navigate cleanly back to Dashboard without popup dialog
    if (sessionState is ExerciseSessionState.Completed) {
        LaunchedEffect(Unit) {
            onExit()
        }
    }

    if (sessionState is ExerciseSessionState.Active) {
        val active = sessionState as ExerciseSessionState.Active

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF090D16))
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // SESSION HEADER HUD: Timer, Streak, Score, Key Center & Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF131D31))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.togglePauseWorkout() },
                    modifier = Modifier.size(36.dp).testTag("pause_workout_button")
                ) {
                    Icon(
                        imageVector = if (active.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = "Pause",
                        tint = if (active.isPaused) Color(0xFF10B981) else Color(0xFFF59E0B)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Elapsed Timer (Endless)
                val minutes = active.timeRemainingSeconds / 60
                val seconds = active.timeRemainingSeconds % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)

                Column {
                    Text(
                        text = "TIME",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.5f),
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = timeString,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Streak counter
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "STREAK",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.5f),
                        letterSpacing = 0.8.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${active.streak}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = if (active.streak > 0) Color(0xFF10B981) else Color.White
                        )
                        if (active.streak >= 5) {
                            Text(text = "🔥", fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Score
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "SCORE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.5f),
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "${active.score}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF38BDF8)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (active.score > 0 || active.promptIndex > 0) {
                            viewModel.finishWorkout()
                        } else {
                            onExit()
                        }
                    },
                    modifier = Modifier.size(36.dp).testTag("exit_workout_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit or Finish Workout",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            // CENTRAL WORKOUT DISPLAY (Expanded & Centralized)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF131D31), Color(0xFF0A0F1D))
                        )
                    )
                    .border(1.5.dp, Color(0xFF1E293B), RoundedCornerShape(24.dp))
                    .padding(horizontal = 20.dp, vertical = 28.dp)
                    .testTag("target_function_display"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Big Key Center in the center (without prefix)
                    if (active.currentChord != null) {
                        Text(
                            text = active.currentChord.displayName,
                            fontSize = 68.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            letterSpacing = (-1).sp
                        )
                    } else {
                        Text(
                            text = active.targetRoot.standardLabel,
                            fontSize = 84.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            letterSpacing = (-2).sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "TARGET INTERVALS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.5f),
                        letterSpacing = 1.5.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Display every chosen target interval in the configure tab below the key center
                    val intervalsToDisplay = if (active.cycleIntervals.isNotEmpty()) {
                        active.cycleIntervals
                    } else {
                        listOf(active.targetInterval)
                    }

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        intervalsToDisplay.forEachIndexed { idx, interval ->
                            val isDimmed = idx in active.completedIntervalIndices
                            val isActive = idx == active.cycleIndex && !isDimmed

                            val textColor = when {
                                isDimmed -> Color.White.copy(alpha = 0.20f)
                                isActive -> Color(0xFF10B981) // Vibrant emerald green for active
                                else -> Color(0xFF6EE7B7).copy(alpha = 0.75f) // Clear green for upcoming
                            }

                            val textWeight = when {
                                isActive -> FontWeight.Black
                                isDimmed -> FontWeight.Medium
                                else -> FontWeight.Bold
                            }

                            val textSize = when {
                                isActive -> 30.sp
                                else -> 24.sp
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = interval.label,
                                    fontSize = textSize,
                                    fontWeight = textWeight,
                                    color = textColor,
                                    letterSpacing = 0.5.sp
                                )
                                if (isActive) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .size(width = 18.dp, height = 3.dp)
                                            .clip(RoundedCornerShape(1.5.dp))
                                            .background(Color(0xFF10B981))
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(7.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Current: ${active.targetInterval.fullName}",
                        fontSize = 13.sp,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Note Reveal Toggle Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF1E293B))
                            .clickable { viewModel.toggleRevealNoteName() }
                            .padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (active.revealNoteName) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle note hint",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (active.revealNoteName) "Target Note: ${active.requiredPitchClass.standardLabel}" else "Reveal Note Name",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (active.revealNoteName) Color(0xFF38BDF8) else Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // VU METER & AUDIO EVALUATION STATUS BADGE
            VUMeterAndStatusBadge(
                vuLevel = vuLevel,
                audioHealth = audioHealth,
                lastEvaluation = active.lastPitchEvaluation
            )

            // Mic Toggle & Quick Input Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.toggleMicListening(!isMicListening) },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isMicListening) Color(0xFF0284C7).copy(alpha = 0.2f) else Color(0xFF7F1D1D).copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = if (isMicListening) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = "Mic toggle",
                        tint = if (isMicListening) Color(0xFF38BDF8) else Color(0xFFF87171),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (isMicListening) "Live Mic Listening" else "Mic Muted (Tap Frets Mode)",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.weight(1f))

                // Fretboard slide-up toggle button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E293B))
                        .clickable { viewModel.toggleFretboardDiagram() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (active.showFretboardDiagram) "Hide Visualizer" else "Show Visualizer",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (active.showFretboardDiagram) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // SLIDE-UP 2-POINT FRETBOARD VISUALIZER
            AnimatedVisibility(
                visible = active.showFretboardDiagram,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column {
                    Text(
                        text = "TWO-POINT INTERVALLIC VISUALIZER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    FretboardView(
                        tuning = currentTuning,
                        targetRoot = active.targetRoot,
                        targetPitchClass = active.requiredPitchClass,
                        targetInterval = active.targetInterval,
                        fretRange = fretRange,
                        allowedStrings = allowedStrings,
                        showTwoPointVectors = true,
                        selectedFret = selectedFret,
                        onFretTapped = { coord ->
                            viewModel.onFretTappedInWorkout(coord)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCompletedDialog(
    completed: ExerciseSessionState.Completed,
    onRestart: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0F172A),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = "🎉", fontSize = 36.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "WORKOUT COMPLETE!",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = completed.module.title,
                    fontSize = 13.sp,
                    color = Color(0xFF38BDF8),
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Prompts:", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    Text("${completed.totalNotesTargeted}", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Accuracy:", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    Text(
                        String.format("%.1f%%", completed.accuracyPercent),
                        color = if (completed.accuracyPercent >= 80f) Color(0xFF10B981) else Color(0xFFF59E0B),
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Max Streak:", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    Text("${completed.maxStreak} 🔥", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Avg Response Latency:", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    Text("${completed.averageResponseTimeMs} ms", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Final Score:", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    Text("${completed.score} pts", color = Color(0xFF38BDF8), fontWeight = FontWeight.Black)
                }

                if (completed.weakIntervals.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Intervals Needing Practice:",
                        fontSize = 12.sp,
                        color = Color(0xFFF87171),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = completed.weakIntervals.take(4).joinToString(", "),
                        fontSize = 12.sp,
                        color = Color(0xFFFCA5A5)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("PRACTICE AGAIN", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("DASHBOARD")
            }
        }
    )
}
