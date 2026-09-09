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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.Chord
import com.example.data.model.IntervallicFunction
import com.example.data.model.PitchClass
import com.example.data.model.Progression
import com.example.ui.viewmodel.TrainerViewModel
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProgressionBuilderScreen(
    viewModel: TrainerViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("Custom Progression") }
    var keyCenter by remember { mutableStateOf(PitchClass.C) }

    val bars = remember {
        mutableStateListOf(
            mutableListOf(
                Chord(
                    root = PitchClass.D,
                    qualityLabel = "m7",
                    intervals = listOf(
                        IntervallicFunction.ROOT,
                        IntervallicFunction.MINOR_THIRD,
                        IntervallicFunction.PERFECT_FIFTH,
                        IntervallicFunction.MINOR_SEVENTH
                    )
                )
            ),
            mutableListOf(
                Chord(
                    root = PitchClass.G,
                    qualityLabel = "7",
                    intervals = listOf(
                        IntervallicFunction.ROOT,
                        IntervallicFunction.MAJOR_THIRD,
                        IntervallicFunction.PERFECT_FIFTH,
                        IntervallicFunction.MINOR_SEVENTH
                    )
                )
            ),
            mutableListOf(
                Chord(
                    root = PitchClass.C,
                    qualityLabel = "maj7",
                    intervals = listOf(
                        IntervallicFunction.ROOT,
                        IntervallicFunction.MAJOR_THIRD,
                        IntervallicFunction.PERFECT_FIFTH,
                        IntervallicFunction.MAJOR_SEVENTH
                    )
                )
            )
        )
    }

    var selectedRoot by remember { mutableStateOf(PitchClass.C) }
    var selectedQuality by remember { mutableStateOf("maj7") }

    val chordQualities = listOf(
        "maj7" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH,
            IntervallicFunction.MAJOR_SEVENTH
        ),
        "m7" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "7" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "m7b5" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.DIMINISHED_FIFTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "dim7" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.DIMINISHED_FIFTH,
            IntervallicFunction.MAJOR_SIXTH
        ),
        "7alt" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.DIMINISHED_FIFTH,
            IntervallicFunction.MINOR_SEVENTH
        )
    )

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
                        text = "PROGRESSION BUILDER",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Build custom bar-by-bar songs & cadences",
                        fontSize = 12.sp,
                        color = Color(0xFF38BDF8)
                    )
                }
            }
        }

        // Title Input
        item {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Song / Progression Title") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF38BDF8),
                    unfocusedBorderColor = Color(0xFF1E293B)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Key Center
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF131D31))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "DEFAULT KEY CENTER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PitchClass.entries.forEach { pc ->
                        val isSelected = keyCenter == pc
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFF0284C7) else Color(0xFF1E293B))
                                .clickable { keyCenter = pc }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = pc.standardLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        // Current Bars List
        item {
            Text(
                text = "BAR SEQUENCE (${bars.size} BARS)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 1.sp
            )
        }

        items(bars.size) { barIdx ->
            val chordList = bars[barIdx]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF1E293B))
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bar ${barIdx + 1}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8),
                        modifier = Modifier.width(52.dp)
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        chordList.forEach { chord ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = chord.displayName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = { if (bars.size > 1) bars.removeAt(barIdx) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove Bar",
                            tint = Color(0xFFF87171),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Add Bar Form
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF131D31))
                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "ADD CHORD TO NEW BAR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Root selector
                Text("Select Root:", fontSize = 11.sp, color = Color(0xFF94A3B8))
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PitchClass.entries.forEach { pc ->
                        val isSelected = selectedRoot == pc
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0xFF38BDF8) else Color(0xFF1E293B))
                                .clickable { selectedRoot = pc }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = pc.standardLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quality selector
                Text("Select Quality:", fontSize = 11.sp, color = Color(0xFF94A3B8))
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    chordQualities.forEach { (qual, _) ->
                        val isSelected = selectedQuality == qual
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) Color(0xFF10B981) else Color(0xFF1E293B))
                                .clickable { selectedQuality = qual }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = qual,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val intervals = chordQualities.firstOrNull { it.first == selectedQuality }?.second ?: emptyList()
                        bars.add(
                            mutableListOf(
                                Chord(
                                    root = selectedRoot,
                                    qualityLabel = selectedQuality,
                                    intervals = intervals
                                )
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF38BDF8))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ADD BAR (${selectedRoot.standardLabel}$selectedQuality)", color = Color(0xFF38BDF8))
                }
            }
        }

        // SAVE BUTTON
        item {
            Button(
                onClick = {
                    val newProg = Progression(
                        id = "custom_${UUID.randomUUID()}",
                        title = title.ifBlank { "Custom Song" },
                        category = "Custom",
                        defaultKey = keyCenter,
                        bars = bars.toList()
                    )
                    viewModel.addCustomProgression(newProg)
                    viewModel.selectedProgression.value = newProg
                    onBack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("save_progression_button")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SAVE TO CHANGES TRAINER",
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}
