package com.example.data.model

import kotlin.math.log2
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * 12 Chromatic Pitch Classes: 0 through 11 mapped to [C, Db, D, Eb, E, F, Gb, G, Ab, A, Bb, B]
 */
enum class PitchClass(val semitoneOffset: Int, val standardLabel: String, val alternateLabel: String = standardLabel) {
    C(0, "C"),
    Db(1, "Db", "C#"),
    D(2, "D"),
    Eb(3, "Eb", "D#"),
    E(4, "E"),
    F(5, "F"),
    Gb(6, "Gb", "F#"),
    G(7, "G"),
    Ab(8, "Ab", "G#"),
    A(9, "A"),
    Bb(10, "Bb", "A#"),
    B(11, "B");

    val isNatural: Boolean
        get() = this in listOf(C, D, E, F, G, A, B)

    val isAccidental: Boolean
        get() = !isNatural

    fun frequency(octave: Int = 4, pitchReference: Float = 440f): Float {
        // MIDI note for pitchClass at given octave: C4 is MIDI 60, A4 is MIDI 69
        val midiNote = (octave + 1) * 12 + semitoneOffset
        return (pitchReference * 2.0.pow((midiNote - 69) / 12.0)).toFloat()
    }

    companion object {
        fun fromMidi(midi: Int): PitchClass {
            val noteIndex = (midi % 12 + 12) % 12
            return entries.first { it.semitoneOffset == noteIndex }
        }

        fun fromFrequency(freq: Float, pitchReference: Float = 440f): Pair<PitchClass, Float> {
            if (freq <= 0f) return Pair(A, 0f)
            val midiFloat = 69f + 12f * (log2(freq / pitchReference))
            val midiRound = midiFloat.roundToInt()
            val deviationCents = (midiFloat - midiRound) * 100f
            return Pair(fromMidi(midiRound), deviationCents)
        }
    }
}

/**
 * Two-Point Intervallic Functions: semitone offsets and functional labels
 */
enum class IntervallicFunction(val semitones: Int, val label: String, val fullName: String) {
    ROOT(0, "R", "Root"),
    MINOR_SECOND(1, "b2", "Minor 2nd / b9"),
    MAJOR_SECOND(2, "2", "Major 2nd / 9"),
    MINOR_THIRD(3, "b3", "Minor 3rd / #9"),
    MAJOR_THIRD(4, "3", "Major 3rd"),
    PERFECT_FOURTH(5, "4", "Perfect 4th / 11"),
    DIMINISHED_FIFTH(6, "b5", "Diminished 5th / #11"),
    PERFECT_FIFTH(7, "5", "Perfect 5th"),
    MINOR_SIXTH(8, "b6", "Minor 6th / b13"),
    MAJOR_SIXTH(9, "6", "Major 6th / 13"),
    MINOR_SEVENTH(10, "b7", "Minor 7th"),
    MAJOR_SEVENTH(11, "7", "Major 7th"),
    OCTAVE(12, "8", "Octave / 8ve");

    companion object {
        fun fromSemitones(semitones: Int): IntervallicFunction {
            val mod = (semitones % 12 + 12) % 12
            return entries.firstOrNull { it.semitones == mod } ?: ROOT
        }
    }
}

/**
 * Instrument Tuning definition with open-string MIDI base values (low string to high string)
 */
data class Tuning(
    val id: String,
    val name: String,
    val description: String,
    val stringBaseMidi: List<Int> // Standard order: 1st string (high E) to 6th string (low E)
) {
    val stringCount: Int get() = stringBaseMidi.size

    companion object {
        val GUITAR_STANDARD = Tuning(
            id = "guitar_std",
            name = "6-String Guitar (Standard)",
            description = "E4 - B3 - G3 - D3 - A2 - E2",
            stringBaseMidi = listOf(64, 59, 55, 50, 45, 40)
        )
        val GUITAR_ALL_FOURTHS = Tuning(
            id = "guitar_all_4ths",
            name = "All-Fourths Guitar",
            description = "F4 - C4 - G3 - D3 - A2 - E2",
            stringBaseMidi = listOf(65, 60, 55, 50, 45, 40)
        )
        val GUITAR_DROP_D = Tuning(
            id = "guitar_drop_d",
            name = "Drop D Guitar",
            description = "E4 - B3 - G3 - D3 - A2 - D2",
            stringBaseMidi = listOf(64, 59, 55, 50, 45, 38)
        )
        val GUITAR_7_STRING = Tuning(
            id = "guitar_7_str",
            name = "7-String Guitar (Standard)",
            description = "E4 - B3 - G3 - D3 - A2 - E2 - B1",
            stringBaseMidi = listOf(64, 59, 55, 50, 45, 40, 35)
        )
        val BASS_STANDARD = Tuning(
            id = "bass_std",
            name = "4-String Bass (Standard)",
            description = "G2 - D2 - A1 - E1",
            stringBaseMidi = listOf(43, 38, 33, 28)
        )

        val ALL_TUNINGS = listOf(
            GUITAR_STANDARD,
            GUITAR_ALL_FOURTHS,
            GUITAR_DROP_D,
            GUITAR_7_STRING,
            BASS_STANDARD
        )
    }
}

/**
 * Fretboard Coordinate representing a physical location on the instrument
 */
data class FretCoordinate(
    val stringIndex: Int, // 0 = high string (e.g. high E), up to stringCount - 1
    val fret: Int,
    val midiNote: Int,
    val pitchClass: PitchClass
)

/**
 * Chord representation
 */
data class Chord(
    val root: PitchClass,
    val qualityLabel: String,
    val intervals: List<IntervallicFunction>
) {
    val displayName: String get() = "${root.standardLabel}$qualityLabel"
}

/**
 * Progression representation
 */
data class Progression(
    val id: String,
    val title: String,
    val category: String,
    val defaultKey: PitchClass,
    val bars: List<List<Chord>>
)

/**
 * Structure Presets for Changes Trainer & Interval Training
 */
enum class ChangesStructurePreset(val label: String, val description: String) {
    CHORD_TONES("Chord Arpeggios", "Full chord tones (R, 3, 5, 7) for each chord"),
    ROOTS_ONLY("Roots Only", "Root notes only (R)"),
    ROOTS_AND_FIFTHS("Roots & 5ths", "Roots and 5ths (R, 5)"),
    TRIAD_TONES("Triad Tones", "Triad tones (R, 3, 5)"),
    GUIDE_TONES("Guide Tones (3 & 7)", "Key voice-leading notes: 3rds and 7ths"),
    IONIAN_MODE("Ionian Mode", "Major scale (R, 2, 3, 4, 5, 6, 7)"),
    DORIAN_MODE("Dorian Mode", "Dorian mode (R, 2, b3, 4, 5, 6, b7)"),
    PHRYGIAN_MODE("Phrygian Mode", "Phrygian mode (R, b2, b3, 4, 5, b6, b7)"),
    LYDIAN_MODE("Lydian Mode", "Lydian mode (R, 2, 3, #4, 5, 6, 7)"),
    MIXOLYDIAN_MODE("Mixolydian Mode", "Dominant mode (R, 2, 3, 4, 5, 6, b7)"),
    AEOLIAN_MODE("Aeolian Mode", "Natural minor (R, 2, b3, 4, 5, b6, b7)"),
    LOCRIAN_MODE("Locrian Mode", "Locrian mode (R, b2, b3, 4, b5, b6, b7)"),
    ALTERED_TENSIONS("Altered Tensions", "Tensions (b9, #9, #11, b13) and extensions")
}

/**
 * Progression Cycle Transposition Mode for Changes Trainer
 */
enum class ChangesCycleMode(val label: String, val description: String) {
    REPEAT("Repeat", "Repeat cadence in current key"),
    CYCLE_OF_5THS("Cycle of 5ths (+5th)", "Continue transposed a perfect 5th higher each cycle"),
    CYCLE_OF_4THS("Cycle of 4ths (+4th)", "Continue transposed a perfect 4th higher each cycle")
}

/**
 * Starting note selector for cyclic sequence rotation in Changes Trainer
 */
enum class ChangesStartingNote(val label: String, val offset: Int) {
    FIRST("1st", 0),
    SECOND("2nd", 1),
    THIRD("3rd", 2),
    FOURTH("4th", 3),
    FIFTH("5th", 4),
    SIXTH("6th", 5),
    SEVENTH("7th", 6)
}

/**
 * Scale definition
 */
data class ScaleDefinition(
    val id: String,
    val name: String,
    val category: String,
    val intervals: List<IntervallicFunction>
)
