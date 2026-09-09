package com.example.data.model

import kotlin.math.abs

data class TwoPointPair(
    val rootCoord: FretCoordinate,
    val targetCoord: FretCoordinate,
    val interval: IntervallicFunction,
    val stringDistance: Int,
    val fretDistance: Int
)

object FretboardGeometryEngine {

    /**
     * Resolves all fret coordinates for a given tuning up to maxFrets
     */
    fun getCoordinates(tuning: Tuning, maxFrets: Int = 24): List<FretCoordinate> {
        val list = mutableListOf<FretCoordinate>()
        tuning.stringBaseMidi.forEachIndexed { stringIndex, baseMidi ->
            for (fret in 0..maxFrets) {
                val midi = baseMidi + fret
                val pitchClass = PitchClass.fromMidi(midi)
                list.add(FretCoordinate(stringIndex, fret, midi, pitchClass))
            }
        }
        return list
    }

    /**
     * Finds all coordinates matching a specific pitch class within a fret range and string set
     */
    fun findCoordinates(
        tuning: Tuning,
        pitchClass: PitchClass,
        fretRange: FretRange = FretRange.ALL,
        allowedStrings: Set<Int> = (0 until tuning.stringCount).toSet()
    ): List<FretCoordinate> {
        return getCoordinates(tuning, fretRange.endFret).filter {
            it.stringIndex in allowedStrings &&
            it.fret in fretRange.startFret..fretRange.endFret &&
            it.pitchClass == pitchClass
        }
    }

    /**
     * Calculates the Two-Point Intervallic relationships: pairs of Root node and Target Interval node
     * on adjacent or nearby strings (within 1 string offset and within 4-5 frets), which represents
     * the core Two-Point Intervallic System pedagogical concept!
     */
    fun calculateTwoPointPairs(
        tuning: Tuning,
        rootPitchClass: PitchClass,
        targetPitchClass: PitchClass,
        interval: IntervallicFunction,
        fretRange: FretRange = FretRange.ALL,
        allowedStrings: Set<Int> = (0 until tuning.stringCount).toSet()
    ): List<TwoPointPair> {
        val rootCoords = findCoordinates(tuning, rootPitchClass, fretRange, allowedStrings)
        val targetCoords = findCoordinates(tuning, targetPitchClass, fretRange, allowedStrings)

        val pairs = mutableListOf<TwoPointPair>()
        for (r in rootCoords) {
            for (t in targetCoords) {
                val strDist = abs(r.stringIndex - t.stringIndex)
                val fretDist = abs(r.fret - t.fret)

                // The Two-Point system focuses on adjacent strings (strDist == 1) or same string within reasonable fret span (<= 5 frets)
                if (strDist <= 2 && fretDist <= 5) {
                    pairs.add(TwoPointPair(r, t, interval, strDist, fretDist))
                }
            }
        }
        return pairs.sortedBy { it.stringDistance * 10 + it.fretDistance }
    }
}
