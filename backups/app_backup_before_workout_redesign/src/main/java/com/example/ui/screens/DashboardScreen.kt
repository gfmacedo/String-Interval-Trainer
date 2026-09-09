package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.PitchDetectorEngine
import com.example.data.model.TrainerModule
import com.example.data.model.Tuning
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TrainerViewModel

@Composable
fun DashboardScreen(
    viewModel: TrainerViewModel,
    onNavigateTo: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentTuning by viewModel.currentTuning.collectAsState()
    val audioHealth by viewModel.audioHealthStatus.collectAsState()
    val recentSessions by viewModel.workoutHistory.collectAsState()

    var showTuningMenu by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP APP BAR / BRAND HEADER
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "BINO STRING TRAINER",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Two-Point Intervallic System",
                        fontSize = 12.sp,
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Audio Health Indicator Chip
                val (healthColor, healthLabel) = when (audioHealth) {
                    PitchDetectorEngine.AudioHealthStatus.ACTIVE -> Color(0xFF10B981) to "Mic Active"
                    PitchDetectorEngine.AudioHealthStatus.HIGH_NOISE -> Color(0xFFF59E0B) to "High Noise"
                    PitchDetectorEngine.AudioHealthStatus.BLOCKED -> Color(0xFFEF4444) to "Mic Inactive"
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(healthColor.copy(alpha = 0.18f))
                        .border(1.dp, healthColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .clickable { onNavigateTo(AppScreen.AUDIO_CALIBRATION) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(healthColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = healthLabel,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = healthColor
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { onNavigateTo(AppScreen.WORKOUT_HISTORY) },
                    modifier = Modifier.testTag("history_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Workout History",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }

                IconButton(
                    onClick = { onNavigateTo(AppScreen.AUDIO_CALIBRATION) },
                    modifier = Modifier.testTag("audio_calibration_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Audio Calibration",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // ACTIVE INSTRUMENT SELECTOR CARD
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF131D31))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                    .clickable { showTuningMenu = true }
                    .padding(14.dp)
                    .testTag("instrument_tuning_selector")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0284C7).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ACTIVE INSTRUMENT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.5f),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = currentTuning.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = currentTuning.description,
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Text(
                        text = "CHANGE",
                        color = Color(0xFF38BDF8),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                DropdownMenu(
                    expanded = showTuningMenu,
                    onDismissRequest = { showTuningMenu = false }
                ) {
                    Tuning.ALL_TUNINGS.forEach { tuning ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(tuning.name, fontWeight = FontWeight.SemiBold)
                                    Text(tuning.description, fontSize = 11.sp, color = Color.Gray)
                                }
                            },
                            onClick = {
                                viewModel.setTuning(tuning)
                                showTuningMenu = false
                            }
                        )
                    }
                }
            }
        }


        // SECTION HEADER: 4 WORKOUT MODULES
        item {
            Text(
                text = "TRAINING MODULES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.55f),
                letterSpacing = 1.2.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // MODULE 1: Note Trainer
        item {
            ModuleNavigationCard(
                moduleNumber = 1,
                title = "Note Trainer",
                description = "Master instant absolute note identification across all 6 strings.",
                accentColor = Color(0xFF38BDF8),
                onOpenModule = { onNavigateTo(AppScreen.NOTE_TRAINER_SETUP) },
                testTag = "module_1_card"
            )
        }

        // MODULE 2: Interval Trainer
        item {
            ModuleNavigationCard(
                moduleNumber = 2,
                title = "Interval Trainer",
                description = "Learn to locate intervals from fixed and floating roots using 2-point shapes.",
                accentColor = Color(0xFF10B981),
                onOpenModule = { onNavigateTo(AppScreen.INTERVAL_TRAINER_SETUP) },
                testTag = "module_2_card"
            )
        }

        // MODULE 3: Changes Trainer
        item {
            ModuleNavigationCard(
                moduleNumber = 3,
                title = "Changes Trainer",
                description = "Outline chord progressions single-note across standards, cadences, and blues.",
                accentColor = Color(0xFFF59E0B),
                onOpenModule = { onNavigateTo(AppScreen.CHANGES_TRAINER_SETUP) },
                testTag = "module_3_card"
            )
        }

        // MODULE 4: Scale Trainer
        item {
            ModuleNavigationCard(
                moduleNumber = 4,
                title = "Scale Trainer",
                description = "Deconstruct modal, minor, and altered scales into functional interval steps.",
                accentColor = Color(0xFFA855F7),
                onOpenModule = { onNavigateTo(AppScreen.SCALE_TRAINER_SETUP) },
                testTag = "module_4_card"
            )
        }

        // EXTRA TOOLS ROW: Progression Builder & Two-Point Guide
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Progression Builder Button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateTo(AppScreen.PROGRESSION_BUILDER) }
                        .testTag("progression_builder_launcher"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF1E293B), Color(0xFF334155))))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Chord Progression Builder",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Create custom songs",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                // Audio Calibration Button
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateTo(AppScreen.AUDIO_CALIBRATION) }
                        .testTag("audio_calibration_launcher"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF1E293B), Color(0xFF334155))))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Audio Calibration",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "432-445Hz & Noise Gate",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }

        // PEDAGOGICAL AXIOM / TWO-POINT SYSTEM EXPLAINER
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF10192A))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "THE TWO-POINT INTERVALLIC SYSTEM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Discards static geometric box shapes (CAGED / 3-notes-per-string) in favor of functional interval targeting relative to active roots. The engine dynamically detects your played pitch and guides real-time fretboard visualization.",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8),
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ModuleNavigationCard(
    moduleNumber: Int,
    title: String,
    description: String,
    accentColor: Color,
    onOpenModule: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onOpenModule)
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                listOf(accentColor.copy(alpha = 0.35f), Color(0xFF1E293B))
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(accentColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "MODULE 0$moduleNumber",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = accentColor
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Open $title",
                    tint = accentColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                color = Color(0xFF94A3B8),
                lineHeight = 18.sp
            )
        }
    }
}
