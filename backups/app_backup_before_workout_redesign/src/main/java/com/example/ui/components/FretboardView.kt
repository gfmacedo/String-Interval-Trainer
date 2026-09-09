package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FretCoordinate
import com.example.data.model.FretRange
import com.example.data.model.FretboardGeometryEngine
import com.example.data.model.IntervallicFunction
import com.example.data.model.PitchClass
import com.example.data.model.Tuning
import com.example.data.model.TwoPointPair

@Composable
fun FretboardView(
    tuning: Tuning,
    targetRoot: PitchClass?,
    targetPitchClass: PitchClass?,
    targetInterval: IntervallicFunction?,
    fretRange: FretRange = FretRange.ALL,
    allowedStrings: Set<Int> = (0 until tuning.stringCount).toSet(),
    showTwoPointVectors: Boolean = true,
    showAllIntervals: Boolean = false,
    selectedFret: FretCoordinate? = null,
    onFretTapped: (FretCoordinate) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val stringCount = tuning.stringCount
    val startFret = fretRange.startFret
    val endFret = fretRange.endFret
    val totalFrets = endFret - startFret + 1

    // Fret width calculation for horizontal layout
    val fretWidthPx = 68.dp
    val neckHeightPx = 200.dp
    val boardWidth = fretWidthPx * totalFrets + 40.dp

    val twoPointPairs = remember(tuning, targetRoot, targetPitchClass, targetInterval, fretRange, allowedStrings) {
        if (targetRoot != null && targetPitchClass != null && targetInterval != null) {
            FretboardGeometryEngine.calculateTwoPointPairs(
                tuning, targetRoot, targetPitchClass, targetInterval, fretRange, allowedStrings
            )
        } else {
            emptyList()
        }
    }

    // Color definitions
    val rosewoodColor = Color(0xFF1E1B18)
    val fretWireColor = Color(0xFFB0B0B0)
    val nutColor = Color(0xFFF3E5AB)
    val rootColor = Color(0xFF2563EB) // Royal Blue
    val targetColor = Color(0xFF10B981) // Vibrant Emerald
    val vectorLineColor = Color(0xFF38BDF8) // Electric Cyan
    val selectedFretColor = Color(0xFFF59E0B) // Amber
    val dotColor = Color(0x33FFFFFF)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0F172A))
            .padding(8.dp)
            .testTag("fretboard_view")
    ) {
        // Legend row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (targetRoot != null) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(rootColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Root (${targetRoot.standardLabel})",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(16.dp))
            }

            if (targetInterval != null && targetPitchClass != null) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(targetColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Target ${targetInterval.label} (${targetPitchClass.standardLabel})",
                    color = Color(0xFF34D399),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Tap any fret to test/hear",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 11.sp
            )
        }

        // Horizontal Scrollable Neck Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Canvas(
                modifier = Modifier
                    .width(boardWidth)
                    .height(neckHeightPx)
                    .pointerInput(tuning, startFret, endFret) {
                        detectTapGestures { offset ->
                            val fretIndex = ((offset.x - 20.dp.toPx()) / fretWidthPx.toPx()).toInt()
                            val actualFret = (startFret + fretIndex).coerceIn(startFret, endFret)

                            val stringSpacing = (size.height - 40.dp.toPx()) / (stringCount - 1).coerceAtLeast(1)
                            val stringIdx = ((offset.y - 20.dp.toPx()) / stringSpacing)
                                .toInt()
                                .coerceIn(0, stringCount - 1)

                            val midi = tuning.stringBaseMidi[stringIdx] + actualFret
                            val pc = PitchClass.fromMidi(midi)
                            onFretTapped(FretCoordinate(stringIdx, actualFret, midi, pc))
                        }
                    }
            ) {
                val startX = 20.dp.toPx()
                val topY = 20.dp.toPx()
                val bottomY = size.height - 20.dp.toPx()
                val stringSpacing = (bottomY - topY) / (stringCount - 1).coerceAtLeast(1)

                // Fretboard Wood Background
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF1E1713), Color(0xFF2A201B), Color(0xFF1E1713))
                    ),
                    topLeft = Offset(startX, topY - 10.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(
                        fretWidthPx.toPx() * totalFrets,
                        (bottomY - topY) + 20.dp.toPx()
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )

                // Inlay Position Marker Dots on fretboard (frets 3, 5, 7, 9, 12, 15, 17, 19, 21, 24)
                val midY = (topY + bottomY) / 2f
                for (f in startFret..endFret) {
                    val fretMidX = startX + (f - startFret) * fretWidthPx.toPx() + fretWidthPx.toPx() / 2f

                    when (f) {
                        3, 5, 7, 9, 15, 17, 19, 21 -> {
                            drawCircle(
                                color = dotColor,
                                radius = 4.dp.toPx(),
                                center = Offset(fretMidX, midY)
                            )
                        }
                        12, 24 -> {
                            // Double dots
                            drawCircle(
                                color = dotColor,
                                radius = 4.dp.toPx(),
                                center = Offset(fretMidX, midY - 24.dp.toPx())
                            )
                            drawCircle(
                                color = dotColor,
                                radius = 4.dp.toPx(),
                                center = Offset(fretMidX, midY + 24.dp.toPx())
                            )
                        }
                    }
                }

                // Fret Wires
                for (f in 0..totalFrets) {
                    val fx = startX + f * fretWidthPx.toPx()
                    val actualFretNum = startFret + f

                    if (actualFretNum == 0) {
                        // Nut
                        drawLine(
                            color = nutColor,
                            start = Offset(fx, topY - 10.dp.toPx()),
                            end = Offset(fx, bottomY + 10.dp.toPx()),
                            strokeWidth = 5.dp.toPx()
                        )
                    } else {
                        drawLine(
                            color = fretWireColor,
                            start = Offset(fx, topY - 10.dp.toPx()),
                            end = Offset(fx, bottomY + 10.dp.toPx()),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }

                // Strings (graduated thickness: 1st string = 1.2dp, 6th string = 3.5dp)
                for (s in 0 until stringCount) {
                    val sy = topY + s * stringSpacing
                    val gauge = 1.2.dp.toPx() + (s.toFloat() / (stringCount - 1).coerceAtLeast(1)) * 2.3.dp.toPx()

                    drawLine(
                        color = Color(0xFFC0C7D1),
                        start = Offset(startX, sy),
                        end = Offset(startX + totalFrets * fretWidthPx.toPx(), sy),
                        strokeWidth = gauge
                    )
                }

                // Two-Point Vector Lines between Root and Target (Core Pedagogical Visualization)
                if (showTwoPointVectors) {
                    twoPointPairs.take(6).forEach { pair ->
                        val r = pair.rootCoord
                        val t = pair.targetCoord

                        val rFretOffset = if (r.fret == 0) 0.25f else 0.5f
                        val tFretOffset = if (t.fret == 0) 0.25f else 0.5f

                        val rx = startX + (r.fret - startFret) * fretWidthPx.toPx() + fretWidthPx.toPx() * rFretOffset
                        val ry = topY + r.stringIndex * stringSpacing

                        val tx = startX + (t.fret - startFret) * fretWidthPx.toPx() + fretWidthPx.toPx() * tFretOffset
                        val ty = topY + t.stringIndex * stringSpacing

                        // Draw glowing dashed connection vector
                        drawLine(
                            color = vectorLineColor.copy(alpha = 0.65f),
                            start = Offset(rx, ry),
                            end = Offset(tx, ty),
                            strokeWidth = 2.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                        )
                    }
                }

                // Draw Note Nodes on Fretboard
                for (s in 0 until stringCount) {
                    val sy = topY + s * stringSpacing
                    for (f in startFret..endFret) {
                        val fretOffset = if (f == 0) 0.25f else 0.5f
                        val fx = startX + (f - startFret) * fretWidthPx.toPx() + fretWidthPx.toPx() * fretOffset
                        val midi = tuning.stringBaseMidi[s] + f
                        val pc = PitchClass.fromMidi(midi)

                        val isRoot = targetRoot != null && pc == targetRoot
                        val isTarget = targetPitchClass != null && pc == targetPitchClass
                        val isSelected = selectedFret != null && selectedFret.stringIndex == s && selectedFret.fret == f

                        if (isRoot || isTarget || isSelected) {
                            val nodeColor = when {
                                isTarget -> targetColor
                                isRoot -> rootColor
                                else -> selectedFretColor
                            }

                            // Glowing halo
                            drawCircle(
                                color = nodeColor.copy(alpha = 0.35f),
                                radius = 16.dp.toPx(),
                                center = Offset(fx, sy)
                            )

                            // Main Node
                            drawCircle(
                                color = nodeColor,
                                radius = 11.dp.toPx(),
                                center = Offset(fx, sy)
                            )

                            // Node border
                            drawCircle(
                                color = Color.White,
                                radius = 11.dp.toPx(),
                                center = Offset(fx, sy),
                                style = Stroke(width = 1.5.dp.toPx())
                            )
                        }
                    }
                }
            }
        }

        // Fret Numbers Row beneath canvas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(vertical = 4.dp)
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            for (f in startFret..endFret) {
                Box(
                    modifier = Modifier.width(fretWidthPx),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (f == 0) "NUT" else f.toString(),
                        color = if (f in listOf(3, 5, 7, 9, 12, 15, 17, 19, 21, 24)) Color(0xFF38BDF8) else Color.White.copy(alpha = 0.4f),
                        fontSize = 11.sp,
                        fontWeight = if (f in listOf(12, 24)) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
