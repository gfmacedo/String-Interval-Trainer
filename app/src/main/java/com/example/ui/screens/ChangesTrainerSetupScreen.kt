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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
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
import com.example.data.model.ChangesCycleMode
import com.example.data.model.ChangesStartingNote
import com.example.data.model.ChangesStructurePreset
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
    val transpositionEnabled by viewModel.changesTranspositionEnabled.collectAsState()
    val cycleMode by viewModel.changesCycleMode.collectAsState()
    val structurePreset by viewModel.changesStructurePreset.collectAsState()
    val startingNote by viewModel.changesStartingNote.collectAsState()
    val randomizeIntervals by viewModel.changesRandomizeIntervalOrder.collectAsState()
    val customProgs by viewModel.customProgressions.collectAsState()

    // Unified tabs: Cadences (35), Standards & Vamps (54), Custom
    val tabTitles = listOf("Cadences (35)", "Standards & Vamps (54)", "Custom")
    var selectedTab by remember { mutableIntStateOf(0) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val cadencesList = remember {
        TheoryDatabase.PROGRESSIONS.filter { it.category == "Cadences & Foundations" }
    }
    val jazzStandardsList = remember {
        TheoryDatabase.PROGRESSIONS.filter { it.category == "Jazz Standards & Repertoire" }
    }
    val modalVampsList = remember {
        TheoryDatabase.PROGRESSIONS.filter { it.category == "Modal Vamps" }
    }
    val currentTabList = when (selectedTab) {
        0 -> cadencesList
        1 -> jazzStandardsList + modalVampsList
        else -> customProgs
    }

    val outerScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(outerScrollState)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
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
                    text = "Full cadence completion, modal structures & voice-leading",
                    fontSize = 12.sp,
                    color = Color(0xFFF59E0B)
                )
            }
        }

        // PROGRESSION SELECTION CARD WITH QUICK CATEGORY TABS AND DROPDOWN SELECTOR
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("changes_progression_selection_card"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1E293B))
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Category Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFF0F172A),
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color(0xFFF59E0B)
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) Color(0xFFF59E0B) else Color.White.copy(alpha = 0.6f)
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ALWAYS-VISIBLE DROPDOWN MENU
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                            .clickable { dropdownExpanded = !dropdownExpanded }
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                            .testTag("progression_dropdown_toggle"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ACTIVE PROGRESSION (TAP TO SELECT)",
                                fontSize = 9.5.sp,
                                color = Color(0xFFF59E0B),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = selectedProg.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            val chordsPreview = selectedProg.bars.flatten().take(6).joinToString(" | ") { it.displayName }
                            Text(
                                text = "Key: ${selectedProg.defaultKey.standardLabel} • $chordsPreview",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8),
                                maxLines = 1
                            )
                        }
                        Icon(
                            imageVector = if (dropdownExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = "Toggle Dropdown",
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Dropdown menu showing all items in current category
                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF334155))
                            .height(340.dp)
                            .width(320.dp)
                    ) {
                        if (currentTabList.isEmpty()) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "No custom progressions yet",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                },
                                onClick = { dropdownExpanded = false }
                            )
                        } else {
                            currentTabList.forEach { item ->
                                val isItemSel = item.id == selectedProg.id
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = item.title,
                                                fontSize = 12.5.sp,
                                                fontWeight = if (isItemSel) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isItemSel) Color(0xFFF59E0B) else Color.White
                                            )
                                            Text(
                                                text = "Key: ${item.defaultKey.standardLabel} • ${item.category}",
                                                fontSize = 10.sp,
                                                color = Color(0xFF94A3B8)
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.selectedProgression.value = item
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // KEY TRANSPOSITION & CADENCE COMPLETION ENGINE
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF131D31))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            // On/Off Toggle for Transposition
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "KEY TRANSPOSITION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (transpositionEnabled) {
                            "Starting in Key: ${transposedKey.standardLabel}"
                        } else {
                            "Original Key: ${selectedProg.defaultKey.standardLabel} (Transposition Off)"
                        },
                        fontSize = 12.sp,
                        color = if (transpositionEnabled) Color(0xFFF59E0B) else Color(0xFF94A3B8),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Switch(
                    checked = transpositionEnabled,
                    onCheckedChange = { viewModel.changesTranspositionEnabled.value = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFF59E0B))
                )
            }

            // 12 Key Selector (Only active when transposition is enabled)
            if (transpositionEnabled) {
                Spacer(modifier = Modifier.height(10.dp))
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
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E293B)))
            Spacer(modifier = Modifier.height(14.dp))

            // CADENCE COMPLETION MODE (Repeat, Cycle of 5ths, Cycle of 4ths)
            Text(
                text = "CADENCE COMPLETION MODE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ChangesCycleMode.entries.forEach { mode ->
                    val isSelected = cycleMode == mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B))
                            .clickable { viewModel.changesCycleMode.value = mode }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = cycleMode.description,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                lineHeight = 15.sp
            )
        }

        // STRUCTURE PRESET WITH STARTING NOTE OFFSET & RANDOMIZE INTERVALS TOGGLES
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF131D31))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                .padding(14.dp)
                .testTag("structure_preset_card")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STRUCTURE PRESET",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = structurePreset.label,
                    fontSize = 12.sp,
                    color = Color(0xFFF59E0B),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Flow of all structure presets
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ChangesStructurePreset.entries.forEach { preset ->
                    val isSelected = structurePreset == preset
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B))
                            .clickable { viewModel.changesStructurePreset.value = preset }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = preset.label,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = structurePreset.description,
                fontSize = 11.5.sp,
                color = Color(0xFFCBD5E1),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E293B)))
            Spacer(modifier = Modifier.height(14.dp))

            // STARTING NOTE TOGGLE (1st, 2nd, 3rd, 4th, 5th, 6th, 7th note)
            Column {
                Text(
                    text = "SEQUENCE STARTING NOTE (ROTATION)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Start interval targets from 1st up to 7th note while completing the full cyclic sequence",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ChangesStartingNote.entries.forEach { noteChoice ->
                        val isSelected = startingNote == noteChoice
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B))
                                .clickable { viewModel.changesStartingNote.value = noteChoice }
                                .padding(vertical = 8.dp)
                                .testTag("start_note_${noteChoice.name.lowercase()}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = noteChoice.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Sequence Order Preview Text
                val sequencePreview = remember(structurePreset, startingNote, randomizeIntervals) {
                    getSequencePreview(structurePreset, startingNote, randomizeIntervals)
                }
                Text(
                    text = sequencePreview,
                    fontSize = 11.sp,
                    color = if (randomizeIntervals) Color(0xFF38BDF8) else Color(0xFFFBBF24),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E293B)))
            Spacer(modifier = Modifier.height(14.dp))

            // RANDOMIZE INTERVAL TARGETS TOGGLE
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = null,
                            tint = if (randomizeIntervals) Color(0xFF38BDF8) else Color(0xFF94A3B8),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RANDOMIZE INTERVAL ORDER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.6f),
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Randomizes target intervals to beat for each key center / chord change",
                        fontSize = 11.sp,
                        color = if (randomizeIntervals) Color(0xFF38BDF8) else Color(0xFF94A3B8)
                    )
                }

                Switch(
                    checked = randomizeIntervals,
                    onCheckedChange = { viewModel.changesRandomizeIntervalOrder.value = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF38BDF8),
                        checkedTrackColor = Color(0xFF38BDF8).copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.testTag("randomize_intervals_switch")
                )
            }
        }

        // START WORKOUT BUTTON
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
                text = "START CHANGES WORKOUT",
                color = Color.Black,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                letterSpacing = 0.8.sp
            )
        }
    }
}

private fun getSequencePreview(
    preset: ChangesStructurePreset,
    startNote: ChangesStartingNote,
    isRandomized: Boolean
): String {
    if (isRandomized) {
        return "🔀 Shuffled: Intervals randomized per chord change (all intervals required)"
    }
    val intervals = when (preset) {
        ChangesStructurePreset.CHORD_TONES -> listOf("R", "3rd", "5th", "7th")
        ChangesStructurePreset.ROOTS_ONLY -> listOf("R")
        ChangesStructurePreset.ROOTS_AND_FIFTHS -> listOf("R", "5th")
        ChangesStructurePreset.TRIAD_TONES -> listOf("R", "3rd", "5th")
        ChangesStructurePreset.GUIDE_TONES -> listOf("3rd", "7th")
        ChangesStructurePreset.IONIAN_MODE -> listOf("R", "2nd", "3rd", "4th", "5th", "6th", "7th")
        ChangesStructurePreset.DORIAN_MODE -> listOf("R", "2nd", "b3", "4th", "5th", "6th", "b7")
        ChangesStructurePreset.PHRYGIAN_MODE -> listOf("R", "b2", "b3", "4th", "5th", "b6", "b7")
        ChangesStructurePreset.LYDIAN_MODE -> listOf("R", "2nd", "3rd", "#4", "5th", "6th", "7th")
        ChangesStructurePreset.MIXOLYDIAN_MODE -> listOf("R", "2nd", "3rd", "4th", "5th", "6th", "b7")
        ChangesStructurePreset.AEOLIAN_MODE -> listOf("R", "2nd", "b3", "4th", "5th", "b6", "b7")
        ChangesStructurePreset.LOCRIAN_MODE -> listOf("R", "b2", "b3", "4th", "b5", "b6", "b7")
        ChangesStructurePreset.ALTERED_TENSIONS -> listOf("R", "b9", "#9", "3rd", "#11", "5th", "b13", "b7")
    }
    if (intervals.size <= 1) return "Single target: ${intervals.first()} (full sequence: 1 note)"
    val offset = startNote.offset % intervals.size
    val rotated = intervals.drop(offset) + intervals.take(offset)
    return "Target Sequence: ${rotated.joinToString(" → ")} (${rotated.size}/${intervals.size} notes)"
}
