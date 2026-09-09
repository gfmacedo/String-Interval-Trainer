package com.example.data.model

object TheoryDatabase {

    // Presets for Interval Trainer (Screen 03)
    val INTERVAL_PRESETS = listOf(
        "Triad Tones (R, 3, 5)" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH
        ),
        "Minor Triad (R, b3, 5)" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH
        ),
        "Major 7th Arpeggio" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH,
            IntervallicFunction.MAJOR_SEVENTH
        ),
        "Dominant 7th Arpeggio" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "Minor 7th Arpeggio" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "Dorian Mode Set" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_SECOND,
            IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH,
            IntervallicFunction.PERFECT_FIFTH,
            IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "Altered Tensions" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.MINOR_SEVENTH,
            IntervallicFunction.MINOR_SECOND, // b9
            IntervallicFunction.MINOR_THIRD,  // #9
            IntervallicFunction.DIMINISHED_FIFTH, // #11 / b5
            IntervallicFunction.MINOR_SIXTH   // b13
        )
    )

    // Scales categorized (Screen 05)
    val SCALES = listOf(
        // Major Scale Modes
        ScaleDefinition("ionian", "Ionian (Major)", "Major Modes", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MAJOR_SEVENTH
        )),
        ScaleDefinition("dorian", "Dorian", "Major Modes", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        )),
        ScaleDefinition("phrygian", "Phrygian", "Major Modes", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        )),
        ScaleDefinition("lydian", "Lydian", "Major Modes", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MAJOR_SEVENTH
        )),
        ScaleDefinition("mixolydian", "Mixolydian", "Major Modes", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        )),
        ScaleDefinition("aeolian", "Aeolian (Natural Minor)", "Major Modes", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        )),
        ScaleDefinition("locrian", "Locrian", "Major Modes", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        )),

        // Melodic Minor Modes
        ScaleDefinition("melodic_minor", "Jazz Minor (Melodic Minor)", "Melodic Minor", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MAJOR_SEVENTH
        )),
        ScaleDefinition("lydian_dominant", "Lydian Dominant (#11)", "Melodic Minor", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        )),
        ScaleDefinition("altered_super_locrian", "Super Locrian (Altered Scale)", "Melodic Minor", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.MAJOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        )),

        // Harmonic Minor
        ScaleDefinition("harmonic_minor", "Harmonic Minor", "Harmonic Minor", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SIXTH,
            IntervallicFunction.MAJOR_SEVENTH
        )),
        ScaleDefinition("phrygian_dominant", "Phrygian Dominant (Spanish Phrygian)", "Harmonic Minor", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        )),

        // Pentatonic & Blues
        ScaleDefinition("minor_pentatonic", "Minor Pentatonic", "Pentatonic & Blues", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FOURTH,
            IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH
        )),
        ScaleDefinition("blues_scale", "Blues Scale", "Pentatonic & Blues", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FOURTH,
            IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH
        )),
        ScaleDefinition("major_pentatonic", "Major Pentatonic", "Pentatonic & Blues", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH
        )),

        // Symmetrical
        ScaleDefinition("diminished_hw", "Half-Whole Diminished", "Symmetrical", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.MAJOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.PERFECT_FIFTH,
            IntervallicFunction.MAJOR_SIXTH, IntervallicFunction.MINOR_SEVENTH
        )),
        ScaleDefinition("whole_tone", "Whole Tone", "Symmetrical", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SIXTH, IntervallicFunction.MINOR_SEVENTH
        ))
    )

    // Preloaded Progressions (Screen 04)
    val PROGRESSIONS = listOf(
        // Cadences & Foundations
        Progression(
            id = "cadence_major_ii_v_i",
            title = "Major II - V - I",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(
                listOf(Chord(PitchClass.D, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.G, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "maj7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "maj7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH)))
            )
        ),
        Progression(
            id = "cadence_minor_ii_v_i",
            title = "Minor II - V - i",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(
                listOf(Chord(PitchClass.D, "m7b5", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.G, "7alt", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH)))
            )
        ),
        Progression(
            id = "cadence_turnaround",
            title = "Turnaround (I - VI - II - V)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(
                listOf(Chord(PitchClass.C, "maj7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH))),
                listOf(Chord(PitchClass.A, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.D, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.G, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH)))
            )
        ),
        Progression(
            id = "cadence_blues_12_bar",
            title = "12-Bar Blues",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.F,
            bars = listOf(
                listOf(Chord(PitchClass.F, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.Bb, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.Bb, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.Bb, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.D, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.G, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH)))
            )
        ),

        // Jazz Standards & Repertoire
        Progression(
            id = "standard_autumn_leaves",
            title = "Autumn Leaves (A Section)",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.G,
            bars = listOf(
                listOf(Chord(PitchClass.A, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.D, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.G, "maj7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "maj7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "#m7b5", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.B, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.E, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.E, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH)))
            )
        ),
        Progression(
            id = "standard_blue_bossa",
            title = "Blue Bossa",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                listOf(Chord(PitchClass.C, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.D, "m7b5", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.G, "7alt", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH)))
            )
        ),
        Progression(
            id = "standard_solar",
            title = "Solar",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                listOf(Chord(PitchClass.C, "m(maj7)", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "m(maj7)", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH))),
                listOf(Chord(PitchClass.G, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.C, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "maj7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "maj7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH))),
                listOf(Chord(PitchClass.F, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.Bb, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH)))
            )
        ),

        // Modal Vamps
        Progression(
            id = "vamp_so_what",
            title = "So What (Dorian Vamp)",
            category = "Modal Vamps",
            defaultKey = PitchClass.D,
            bars = listOf(
                listOf(Chord(PitchClass.D, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.D, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.Eb, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.D, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH)))
            )
        ),
        Progression(
            id = "vamp_mixo_funk",
            title = "Mixolydian Funk Groove",
            category = "Modal Vamps",
            defaultKey = PitchClass.A,
            bars = listOf(
                listOf(Chord(PitchClass.A, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.A, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.D, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))),
                listOf(Chord(PitchClass.A, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH)))
            )
        )
    )
}
