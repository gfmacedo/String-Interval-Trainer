package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PitchClass
import com.example.data.model.Progression
import com.example.data.model.TheoryDatabase
import com.example.data.model.TrainerModule
import com.example.ui.viewmodel.TrainerViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChangesTrainerSetupScreen(
    viewModel: TrainerViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedProg by viewModel.selectedProgression.collectAsState()
    val transposedKey by viewModel.changesTransposedKey.collectAsState()
    val randomKeyOnRepeat by viewModel.changesRandomKeyOnRepeat.collectAsState()
    val difficultyLevel by viewModel.changesDifficultyLevel.collectAsState()
    val customProgs by viewModel.customProgressions.collectAsState()

    val categories = listOf("Cadences & Foundations", "Jazz Standards & Repertoire", "Modal Vamps", "Custom")
    var selectedTab by remember { mutableIntStateOf(0) }

    val allProgressions = TheoryDatabase.PROGRESSIONS + customProgs
    val filteredProgressions = allProgressions.filter {
        if (selectedTab == 3) it.category == "Custom"
        else it.category == categories[selectedTab]
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "CHANGES TRAINER SETUP",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Single-note voice leading over full chord progressions",
                        fontSize = 12.sp,
                        color = Color(0xFFF59E0B)
                    )
                }
            }
        }

        // CATEGORY TABS
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF131D31),
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFFF59E0B)
                    )
                }
            ) {
                categories.forEachIndexed { index, cat ->
                    val shortName = when (index) {
                        0 -> "Cadences"
                        1 -> "Standards"
                        2 -> "Vamps"
                        else -> "Custom"
                    }
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = shortName,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) Color(0xFFF59E0B) else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    )
                }
            }
        }

        // PROGRESSION LIST
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (filteredProgressions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No custom progressions yet. Use Progression Builder to add one!",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 13.sp
                        )
                    }
                } else {
                    filteredProgressions.forEach { prog ->
                        val isSelected = selectedProg.id == prog.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectedProgression.value = prog },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF131D31)
                            ),
                            border = CardDefaults.outlinedCardBorder().copy(
                                brush = androidx.compose.ui.graphics.SolidColor(
                                    if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B)
                                )
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = prog.title,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFFFBBF24) else Color.White,
                                        fontSize = 15.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "Key: ${prog.defaultKey.standardLabel}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF38BDF8),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Quick chords preview
                                val chordsList = prog.bars.flatten().map { it.displayName }
                                Text(
                                    text = chordsList.joinToString("  |  "),
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        // KEY TRANSPOSITION ENGINE
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF131D31))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "KEY TRANSPOSITION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Target Key: ${transposedKey.standardLabel}",
                        fontSize = 13.sp,
                        color = Color(0xFFF59E0B),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 12 Key Selectors
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PitchClass.entries.forEach { pc ->
                        val isSelected = transposedKey == pc
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B))
                                .clickable { viewModel.changesTransposedKey.value = pc }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = pc.standardLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Random Key on Repeat",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Forces real-time transposition across all 12 keys",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Switch(
                        checked = randomKeyOnRepeat,
                        onCheckedChange = { viewModel.changesRandomKeyOnRepeat.value = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFF59E0B))
                    )
                }
            }
        }

        // INTERVALLIC DIFFICULTY LEVELS (SLIDER 1-50)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF131D31))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "INTERVALLIC DIFFICULTY LEVEL",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Level $difficultyLevel / 50",
                        fontSize = 13.sp,
                        color = Color(0xFFF59E0B),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                val difficultyDescription = when {
                    difficultyLevel <= 10 -> "Levels 1–10: Roots and Perfect 5ths only"
                    difficultyLevel <= 20 -> "Levels 11–20: Guide Tones (Roots, 3rds, and 7ths)"
                    difficultyLevel <= 35 -> "Levels 21–35: Full 4-Note Arpeggios (R-3-5-7) & Voice-Leading"
                    else -> "Levels 36–50: Tensions, Extensions (9, 11, 13) & Altered Dominant choices"
                }

                Text(
                    text = difficultyDescription,
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    fontWeight = FontWeight.Medium
                )

                Slider(
                    value = difficultyLevel.toFloat(),
                    onValueChange = { viewModel.changesDifficultyLevel.value = it.toInt() },
                    valueRange = 1f..50f,
                    steps = 49,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFF59E0B),
                        activeTrackColor = Color(0xFFF59E0B),
                        inactiveTrackColor = Color(0xFF334155)
                    ),
                    modifier = Modifier.testTag("changes_difficulty_slider")
                )
            }
        }

        // START BUTTON
        item {
            Button(
                onClick = { viewModel.startWorkout(TrainerModule.CHANGES_TRAINER) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_changes_trainer_workout")
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "START WORKOUT",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}
