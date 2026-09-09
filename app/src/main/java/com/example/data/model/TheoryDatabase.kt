package com.example.data.model

object TheoryDatabase {

    // Helper builders for clean chord declarations
    private fun m7(root: PitchClass) = Chord(root, "m7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))
    private fun dom7(root: PitchClass) = Chord(root, "7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SEVENTH))
    private fun maj7(root: PitchClass) = Chord(root, "maj7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SEVENTH))
    private fun m7b5(root: PitchClass) = Chord(root, "m7b5", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SEVENTH))
    private fun maj(root: PitchClass) = Chord(root, "", listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.PERFECT_FIFTH))
    private fun min(root: PitchClass) = Chord(root, "m", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH))
    private fun power5(root: PitchClass) = Chord(root, "5", listOf(IntervallicFunction.ROOT, IntervallicFunction.PERFECT_FIFTH))
    private fun dim7(root: PitchClass) = Chord(root, "dim7", listOf(IntervallicFunction.ROOT, IntervallicFunction.MINOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MAJOR_SIXTH))
    private fun bar(vararg chords: Chord): List<Chord> = chords.toList()

    // Structure Presets for Interval Trainer and Changes Trainer
    // Complemented with ALL 7 MODES and core structures
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
        "Half-Diminished (m7b5)" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.DIMINISHED_FIFTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "Diminished 7th" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.DIMINISHED_FIFTH,
            IntervallicFunction.MAJOR_SIXTH
        ),
        // ALL 7 MODES OF THE MAJOR SCALE
        "Ionian (Major) Mode" to listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MAJOR_SEVENTH
        ),
        "Dorian Mode" to listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "Phrygian Mode" to listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "Lydian Mode" to listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MAJOR_SEVENTH
        ),
        "Mixolydian Mode" to listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "Aeolian (Natural Minor) Mode" to listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "Locrian Mode" to listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SIXTH,
            IntervallicFunction.MINOR_SEVENTH
        ),
        "Altered Tensions" to listOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.MINOR_SEVENTH,
            IntervallicFunction.MINOR_SECOND,  // b9
            IntervallicFunction.MINOR_THIRD,   // #9
            IntervallicFunction.DIMINISHED_FIFTH, // #11 / b5
            IntervallicFunction.MINOR_SIXTH    // b13
        )
    )

    // Scales categorized
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
        ScaleDefinition("altered_super_locrian", "Altered Scale (Super Locrian)", "Melodic Minor", listOf(
            IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
            IntervallicFunction.MAJOR_THIRD, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SIXTH,
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

    // Preloaded Progressions:
    // 1) 35 Cadences for guitar across Pop, R&B, Rock, Metal, Grunge, Sertanejo, Hardcore, Jazz ("Cadences & Foundations")
    // 2) Top 50 Jazz Standards used to teach improvisation + Modal Vamps ("Jazz Standards & Repertoire", "Modal Vamps")
    val PROGRESSIONS = listOf(
        // ==========================================
        // CADENCES & FOUNDATIONS (Top 35 Guitar Cadences)
        // ==========================================
        Progression(
            id = "cadence_royal_road",
            title = "Royal Road Cadence (IVmaj7 - V7 - iiim7 - vim7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(maj7(PitchClass.F)), bar(dom7(PitchClass.G)), bar(m7(PitchClass.E)), bar(m7(PitchClass.A)))
        ),
        Progression(
            id = "cadence_pop_punk_four_chords",
            title = "Pop-Punk / 4 Chords (I - V - vi - IV)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(maj(PitchClass.C)), bar(maj(PitchClass.G)), bar(min(PitchClass.A)), bar(maj(PitchClass.F)))
        ),
        Progression(
            id = "cadence_50s_doo_wop",
            title = "50s Doo-Wop Cadence (I - vi - IV - V)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(maj(PitchClass.C)), bar(min(PitchClass.A)), bar(maj(PitchClass.F)), bar(maj(PitchClass.G)))
        ),
        Progression(
            id = "cadence_sensitive_pop",
            title = "Sensitive Modern Pop (vi - IV - I - V)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(min(PitchClass.A)), bar(maj(PitchClass.F)), bar(maj(PitchClass.C)), bar(maj(PitchClass.G)))
        ),
        Progression(
            id = "cadence_rock_blues_power",
            title = "Rock & Blues Power Cadence (I - IV - V)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.A,
            bars = listOf(bar(power5(PitchClass.A)), bar(power5(PitchClass.D)), bar(power5(PitchClass.E)), bar(power5(PitchClass.A)))
        ),
        Progression(
            id = "cadence_mixolydian_rock",
            title = "Mixolydian Rock Anthem (I - bVII - IV)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.D,
            bars = listOf(bar(maj(PitchClass.D)), bar(maj(PitchClass.C)), bar(maj(PitchClass.G)), bar(maj(PitchClass.D)))
        ),
        Progression(
            id = "cadence_andalusian",
            title = "Andalusian Cadence (i - bVII - bVI - V)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.A,
            bars = listOf(bar(min(PitchClass.A)), bar(maj(PitchClass.G)), bar(maj(PitchClass.F)), bar(dom7(PitchClass.E)))
        ),
        Progression(
            id = "cadence_aeolian_dark_rock",
            title = "Aeolian Dark Rock (i - bVI - bIII - bVII)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.E,
            bars = listOf(bar(min(PitchClass.E)), bar(maj(PitchClass.C)), bar(maj(PitchClass.G)), bar(maj(PitchClass.D)))
        ),
        Progression(
            id = "cadence_heavy_metal_gallop",
            title = "Heavy Metal Power Gallop (i - bVI - bVII - i)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.E,
            bars = listOf(bar(power5(PitchClass.E)), bar(power5(PitchClass.C)), bar(power5(PitchClass.D)), bar(power5(PitchClass.E)))
        ),
        Progression(
            id = "cadence_phrygian_metal",
            title = "Phrygian Metal Breakdown (i - bII - i - bVII)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.E,
            bars = listOf(bar(power5(PitchClass.E)), bar(power5(PitchClass.F)), bar(power5(PitchClass.E)), bar(power5(PitchClass.D)))
        ),
        Progression(
            id = "cadence_grunge_sludge_drift",
            title = "Grunge Sludge Drift (I - bIII - IV - bVI)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.E,
            bars = listOf(bar(maj(PitchClass.E)), bar(maj(PitchClass.G)), bar(maj(PitchClass.A)), bar(maj(PitchClass.C)))
        ),
        Progression(
            id = "cadence_grunge_drop",
            title = "Grunge Half-Step Drop (i - bII - bV - IV)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.D,
            bars = listOf(bar(power5(PitchClass.D)), bar(power5(PitchClass.Eb)), bar(power5(PitchClass.Ab)), bar(power5(PitchClass.G)))
        ),
        Progression(
            id = "cadence_hardcore_punk_blast",
            title = "Hardcore Punk 3-Chord Blast (I - bVII - IV - I)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.A,
            bars = listOf(bar(power5(PitchClass.A)), bar(power5(PitchClass.G)), bar(power5(PitchClass.D)), bar(power5(PitchClass.A)))
        ),
        Progression(
            id = "cadence_melodic_hardcore",
            title = "Melodic Hardcore Octaves (vi - IV - I - V)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.A,
            bars = listOf(bar(min(PitchClass.Gb)), bar(maj(PitchClass.D)), bar(maj(PitchClass.A)), bar(maj(PitchClass.E)))
        ),
        Progression(
            id = "cadence_sertanejo_universitario",
            title = "Sertanejo Universitário Anthem (I - V - vi - IV)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.G,
            bars = listOf(bar(maj(PitchClass.G)), bar(maj(PitchClass.D)), bar(min(PitchClass.E)), bar(maj(PitchClass.C)))
        ),
        Progression(
            id = "cadence_sertanejo_modao",
            title = "Sertanejo Romântico / Modão (I - IV - V - I)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.A,
            bars = listOf(bar(maj(PitchClass.A)), bar(maj(PitchClass.D)), bar(dom7(PitchClass.E)), bar(maj(PitchClass.A)))
        ),
        Progression(
            id = "cadence_sertanejo_sofrencia",
            title = "Sertanejo Sofrência Turn (vi - IV - V - I)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.G,
            bars = listOf(bar(min(PitchClass.E)), bar(maj(PitchClass.C)), bar(maj(PitchClass.D)), bar(maj(PitchClass.G)))
        ),
        Progression(
            id = "cadence_sertanejo_guarania",
            title = "Sertanejo Guarânia Clássica (I - ii - V - I)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.D,
            bars = listOf(bar(maj(PitchClass.D)), bar(min(PitchClass.E)), bar(dom7(PitchClass.A)), bar(maj(PitchClass.D)))
        ),
        Progression(
            id = "cadence_neo_soul_turnaround",
            title = "Neo-Soul R&B Turnaround (ii9 - V13 - Imaj9 - VI7alt)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)), bar(maj7(PitchClass.C)), bar(dom7(PitchClass.A)))
        ),
        Progression(
            id = "cadence_rnb_90s_groove",
            title = "90s R&B Groove (IVmaj7 - iiim7 - vim7 - V7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(maj7(PitchClass.F)), bar(m7(PitchClass.E)), bar(m7(PitchClass.A)), bar(dom7(PitchClass.G)))
        ),
        Progression(
            id = "cadence_rnb_soul_ballad",
            title = "R&B Soul Ballad (Imaj7 - vim7 - iim7 - V7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.Eb,
            bars = listOf(bar(maj7(PitchClass.Eb)), bar(m7(PitchClass.C)), bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb)))
        ),
        Progression(
            id = "cadence_major_ii_v_i",
            title = "Major II - V - I (ii7 - V7 - Imaj7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)), bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C)))
        ),
        Progression(
            id = "cadence_minor_ii_v_i",
            title = "Minor II - V - i (iiø7 - V7alt - im7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(m7b5(PitchClass.D)), bar(dom7(PitchClass.G)), bar(m7(PitchClass.C)), bar(m7(PitchClass.C)))
        ),
        Progression(
            id = "cadence_turnaround",
            title = "Jazz Turnaround (Imaj7 - VI7 - ii7 - V7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(maj7(PitchClass.C)), bar(dom7(PitchClass.A)), bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)))
        ),
        Progression(
            id = "cadence_coltrane_changes",
            title = "Coltrane Changes Cycle (I - V/bVI - bVI - V/III - III)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(maj7(PitchClass.C)), bar(dom7(PitchClass.Eb)), bar(maj7(PitchClass.Ab)), bar(dom7(PitchClass.B)), bar(maj7(PitchClass.E)))
        ),
        Progression(
            id = "cadence_bird_blues",
            title = "Bird Blues Cadence (I7 - viiø7/III7 - vim7/II7 - iim7/V7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.F,
            bars = listOf(bar(dom7(PitchClass.F)), bar(m7b5(PitchClass.E), dom7(PitchClass.A)), bar(m7(PitchClass.D), dom7(PitchClass.G)), bar(m7(PitchClass.C), dom7(PitchClass.F)), bar(dom7(PitchClass.Bb)))
        ),
        Progression(
            id = "cadence_backdoor",
            title = "Backdoor Cadence (iv7 - bVII7 - Imaj7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb)), bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C)))
        ),
        Progression(
            id = "cadence_deceptive",
            title = "Deceptive Cadence (I - IV - V - vi)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(maj(PitchClass.C)), bar(maj(PitchClass.F)), bar(maj(PitchClass.G)), bar(min(PitchClass.A)))
        ),
        Progression(
            id = "cadence_plagal_gospel",
            title = "Plagal Gospel Cadence (I - IV - iv - I)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(maj(PitchClass.C)), bar(maj(PitchClass.F)), bar(min(PitchClass.F)), bar(maj(PitchClass.C)))
        ),
        Progression(
            id = "cadence_circle_of_fifths",
            title = "Circle of Fifths Progression (vi - ii - V - I)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(m7(PitchClass.A)), bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)), bar(maj7(PitchClass.C)))
        ),
        Progression(
            id = "cadence_bossa_classic",
            title = "Bossa Nova Classic Turn (Imaj7 - II7 - ii7 - V7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.F,
            bars = listOf(bar(maj7(PitchClass.F)), bar(dom7(PitchClass.G)), bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)))
        ),
        Progression(
            id = "cadence_flamenco_phrygian",
            title = "Flamenco Phrygian Cadence (iv - III - II - I)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.A,
            bars = listOf(bar(min(PitchClass.D)), bar(maj(PitchClass.C)), bar(maj(PitchClass.Bb)), bar(maj(PitchClass.A)))
        ),
        Progression(
            id = "cadence_funk_groove",
            title = "Funk Two-Chord Groove (i7 - IV7)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.E,
            bars = listOf(bar(dom7(PitchClass.E)), bar(dom7(PitchClass.E)), bar(dom7(PitchClass.A)), bar(dom7(PitchClass.E)))
        ),
        Progression(
            id = "cadence_post_rock_ambient",
            title = "Post-Rock Ambient Crescendo (vi - I - V - IV)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(min(PitchClass.A)), bar(maj(PitchClass.C)), bar(maj(PitchClass.G)), bar(maj(PitchClass.F)))
        ),
        Progression(
            id = "cadence_doom_metal",
            title = "Doom Metal Sludge (i - bV - iv - bVI)",
            category = "Cadences & Foundations",
            defaultKey = PitchClass.C,
            bars = listOf(bar(power5(PitchClass.C)), bar(power5(PitchClass.Gb)), bar(power5(PitchClass.F)), bar(power5(PitchClass.Ab)))
        ),

        // ============================================================
        // JAZZ STANDARDS (Top 50 Repertoire for Jazz Improv Education)
        // ============================================================
        Progression(
            id = "std_01_autumn_leaves",
            title = "Autumn Leaves",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.G,
            bars = listOf(
                bar(m7(PitchClass.A)), bar(dom7(PitchClass.D)), bar(maj7(PitchClass.G)), bar(maj7(PitchClass.C)),
                bar(m7b5(PitchClass.Gb)), bar(dom7(PitchClass.B)), bar(m7(PitchClass.E)), bar(m7(PitchClass.E))
            )
        ),
        Progression(
            id = "std_02_blue_bossa",
            title = "Blue Bossa",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(m7(PitchClass.C)), bar(m7(PitchClass.C)), bar(m7(PitchClass.F)), bar(m7(PitchClass.F)),
                bar(m7b5(PitchClass.D)), bar(dom7(PitchClass.G)), bar(m7(PitchClass.C)), bar(m7(PitchClass.C)),
                bar(m7(PitchClass.Eb)), bar(dom7(PitchClass.Ab)), bar(maj7(PitchClass.Db)), bar(maj7(PitchClass.Db)),
                bar(m7b5(PitchClass.D)), bar(dom7(PitchClass.G)), bar(m7(PitchClass.C)), bar(dom7(PitchClass.G))
            )
        ),
        Progression(
            id = "std_03_all_the_things_you_are",
            title = "All The Things You Are",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Ab,
            bars = listOf(
                bar(m7(PitchClass.F)), bar(m7(PitchClass.Bb)), bar(dom7(PitchClass.Eb)), bar(maj7(PitchClass.Ab)),
                bar(maj7(PitchClass.Db)), bar(dom7(PitchClass.G)), bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_04_take_the_a_train",
            title = "Take The A Train",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C)), bar(dom7(PitchClass.D)), bar(dom7(PitchClass.D)),
                bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)), bar(maj7(PitchClass.C)), bar(dom7(PitchClass.G))
            )
        ),
        Progression(
            id = "std_05_satin_doll",
            title = "Satin Doll",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(m7(PitchClass.D), dom7(PitchClass.G)), bar(m7(PitchClass.D), dom7(PitchClass.G)),
                bar(m7(PitchClass.E), dom7(PitchClass.A)), bar(m7(PitchClass.E), dom7(PitchClass.A)),
                bar(m7(PitchClass.A), dom7(PitchClass.D)), bar(dom7(PitchClass.Ab), dom7(PitchClass.Db)),
                bar(maj7(PitchClass.C)), bar(m7(PitchClass.D), dom7(PitchClass.G))
            )
        ),
        Progression(
            id = "std_06_so_what",
            title = "So What",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.D,
            bars = listOf(
                bar(m7(PitchClass.D)), bar(m7(PitchClass.D)), bar(m7(PitchClass.D)), bar(m7(PitchClass.D)),
                bar(m7(PitchClass.Eb)), bar(m7(PitchClass.Eb)), bar(m7(PitchClass.D)), bar(m7(PitchClass.D))
            )
        ),
        Progression(
            id = "std_07_fly_me_to_the_moon",
            title = "Fly Me To The Moon",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(m7(PitchClass.A)), bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)), bar(maj7(PitchClass.C)),
                bar(maj7(PitchClass.F)), bar(m7b5(PitchClass.B)), bar(dom7(PitchClass.E)), bar(min(PitchClass.A))
            )
        ),
        Progression(
            id = "std_08_summertime",
            title = "Summertime",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.D,
            bars = listOf(
                bar(m7(PitchClass.D)), bar(dom7(PitchClass.A)), bar(m7(PitchClass.D)), bar(dom7(PitchClass.A)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(maj7(PitchClass.F)), bar(dom7(PitchClass.A))
            )
        ),
        Progression(
            id = "std_09_cantaloupe_island",
            title = "Cantaloupe Island",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(m7(PitchClass.F)), bar(m7(PitchClass.F)), bar(m7(PitchClass.F)), bar(m7(PitchClass.F)),
                bar(dom7(PitchClass.Db)), bar(dom7(PitchClass.Db)), bar(m7(PitchClass.D)), bar(m7(PitchClass.D))
            )
        ),
        Progression(
            id = "std_10_watermelon_man",
            title = "Watermelon Man",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(dom7(PitchClass.F)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.F)),
                bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.F)),
                bar(dom7(PitchClass.C)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_11_footprints",
            title = "Footprints",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(m7(PitchClass.C)), bar(m7(PitchClass.C)), bar(m7(PitchClass.C)), bar(m7(PitchClass.C)),
                bar(m7(PitchClass.F)), bar(m7(PitchClass.F)), bar(m7(PitchClass.C)), bar(m7(PitchClass.C)),
                bar(m7b5(PitchClass.Gb)), bar(dom7(PitchClass.B)), bar(m7(PitchClass.C)), bar(m7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_12_solar",
            title = "Solar",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(m7(PitchClass.C)), bar(m7(PitchClass.C)), bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)),
                bar(maj7(PitchClass.F)), bar(maj7(PitchClass.F)), bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb)),
                bar(maj7(PitchClass.Eb)), bar(m7b5(PitchClass.D)), bar(dom7(PitchClass.G)), bar(m7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_13_maiden_voyage",
            title = "Maiden Voyage",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.D,
            bars = listOf(
                bar(dom7(PitchClass.D)), bar(dom7(PitchClass.D)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.F)),
                bar(dom7(PitchClass.Eb)), bar(dom7(PitchClass.Eb)), bar(dom7(PitchClass.Db)), bar(dom7(PitchClass.Db))
            )
        ),
        Progression(
            id = "std_14_doxy",
            title = "Doxy",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Bb,
            bars = listOf(
                bar(maj7(PitchClass.Bb)), bar(dom7(PitchClass.Ab)), bar(dom7(PitchClass.G)), bar(dom7(PitchClass.G)),
                bar(m7(PitchClass.C)), bar(dom7(PitchClass.F)), bar(maj7(PitchClass.Bb)), bar(dom7(PitchClass.F))
            )
        ),
        Progression(
            id = "std_15_oleo",
            title = "Oleo (Rhythm Changes A)",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Bb,
            bars = listOf(
                bar(maj7(PitchClass.Bb), dom7(PitchClass.G)), bar(m7(PitchClass.C), dom7(PitchClass.F)),
                bar(m7(PitchClass.D), dom7(PitchClass.G)), bar(m7(PitchClass.C), dom7(PitchClass.F)),
                bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Eb)),
                bar(maj7(PitchClass.Bb)), bar(dom7(PitchClass.F))
            )
        ),
        Progression(
            id = "std_16_billies_bounce",
            title = "Billie's Bounce",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(dom7(PitchClass.F)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.F)),
                bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.F)), bar(m7(PitchClass.D)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_17_c_jam_blues",
            title = "C Jam Blues",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(dom7(PitchClass.C)), bar(dom7(PitchClass.C)), bar(dom7(PitchClass.C)), bar(dom7(PitchClass.C)),
                bar(dom7(PitchClass.F)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.C)), bar(dom7(PitchClass.C)),
                bar(dom7(PitchClass.G)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.C)), bar(dom7(PitchClass.G))
            )
        ),
        Progression(
            id = "std_18_nows_the_time",
            title = "Now's The Time",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(dom7(PitchClass.F)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.F)),
                bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.D)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_19_freddie_freeloader",
            title = "Freddie Freeloader",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Bb,
            bars = listOf(
                bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)),
                bar(dom7(PitchClass.Eb)), bar(dom7(PitchClass.Eb)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)),
                bar(dom7(PitchClass.F)), bar(dom7(PitchClass.Eb)), bar(dom7(PitchClass.Ab)), bar(dom7(PitchClass.Ab))
            )
        ),
        Progression(
            id = "std_20_impressions",
            title = "Impressions",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.D,
            bars = listOf(
                bar(m7(PitchClass.D)), bar(m7(PitchClass.D)), bar(m7(PitchClass.D)), bar(m7(PitchClass.D)),
                bar(m7(PitchClass.Eb)), bar(m7(PitchClass.Eb)), bar(m7(PitchClass.D)), bar(m7(PitchClass.D))
            )
        ),
        Progression(
            id = "std_21_all_of_me",
            title = "All Of Me",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C)), bar(dom7(PitchClass.E)), bar(dom7(PitchClass.E)),
                bar(dom7(PitchClass.A)), bar(dom7(PitchClass.A)), bar(m7(PitchClass.D)), bar(m7(PitchClass.D)),
                bar(dom7(PitchClass.E)), bar(dom7(PitchClass.E)), bar(m7(PitchClass.A)), bar(m7(PitchClass.A)),
                bar(dom7(PitchClass.D)), bar(dom7(PitchClass.D)), bar(m7(PitchClass.D)), bar(dom7(PitchClass.G))
            )
        ),
        Progression(
            id = "std_22_there_will_never_be_another_you",
            title = "There Will Never Be Another You",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Eb,
            bars = listOf(
                bar(maj7(PitchClass.Eb)), bar(maj7(PitchClass.Eb)), bar(m7b5(PitchClass.D)), bar(dom7(PitchClass.G)),
                bar(m7(PitchClass.C)), bar(m7(PitchClass.C)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)),
                bar(maj7(PitchClass.Ab)), bar(dom7(PitchClass.Db)), bar(maj7(PitchClass.Eb)), bar(m7(PitchClass.C)),
                bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb)), bar(maj7(PitchClass.Eb)), bar(dom7(PitchClass.Bb))
            )
        ),
        Progression(
            id = "std_23_someday_my_prince_will_come",
            title = "Someday My Prince Will Come",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Bb,
            bars = listOf(
                bar(maj7(PitchClass.Bb)), bar(dom7(PitchClass.D)), bar(maj7(PitchClass.Eb)), bar(dom7(PitchClass.G)),
                bar(m7(PitchClass.C)), bar(dom7(PitchClass.G)), bar(m7(PitchClass.C)), bar(dom7(PitchClass.F))
            )
        ),
        Progression(
            id = "std_24_bye_bye_blackbird",
            title = "Bye Bye Blackbird",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(maj7(PitchClass.F)), bar(maj7(PitchClass.F)), bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(maj7(PitchClass.F)), bar(maj7(PitchClass.F))
            )
        ),
        Progression(
            id = "std_25_four",
            title = "Four",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Eb,
            bars = listOf(
                bar(maj7(PitchClass.Eb)), bar(maj7(PitchClass.Eb)), bar(m7(PitchClass.Eb)), bar(dom7(PitchClass.Ab)),
                bar(maj7(PitchClass.Db)), bar(maj7(PitchClass.Db)), bar(m7(PitchClass.C)), bar(dom7(PitchClass.F)),
                bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb)), bar(maj7(PitchClass.Eb)), bar(dom7(PitchClass.Bb))
            )
        ),
        Progression(
            id = "std_26_have_you_met_miss_jones",
            title = "Have You Met Miss Jones",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(maj7(PitchClass.F)), bar(dom7(PitchClass.D)), bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)),
                bar(m7(PitchClass.A)), bar(dom7(PitchClass.D)), bar(m7(PitchClass.G)), bar(dom7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_27_alone_together",
            title = "Alone Together",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.D,
            bars = listOf(
                bar(m7(PitchClass.D)), bar(m7(PitchClass.D)), bar(m7b5(PitchClass.E)), bar(dom7(PitchClass.A)),
                bar(m7(PitchClass.D)), bar(m7(PitchClass.D)), bar(m7b5(PitchClass.E)), bar(dom7(PitchClass.A)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(maj7(PitchClass.F)), bar(maj7(PitchClass.Bb))
            )
        ),
        Progression(
            id = "std_28_black_orpheus",
            title = "Black Orpheus (Manhã de Carnaval)",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.A,
            bars = listOf(
                bar(m7(PitchClass.A)), bar(m7b5(PitchClass.B), dom7(PitchClass.E)), bar(m7(PitchClass.A)), bar(m7b5(PitchClass.B), dom7(PitchClass.E)),
                bar(m7(PitchClass.A)), bar(m7(PitchClass.D)), bar(maj7(PitchClass.G)), bar(dom7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_29_beautiful_love",
            title = "Beautiful Love",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.D,
            bars = listOf(
                bar(m7b5(PitchClass.E)), bar(dom7(PitchClass.A)), bar(m7(PitchClass.D)), bar(m7(PitchClass.D)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(maj7(PitchClass.F)), bar(dom7(PitchClass.A))
            )
        ),
        Progression(
            id = "std_30_softly_as_in_a_morning_sunrise",
            title = "Softly As In A Morning Sunrise",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(m7(PitchClass.C)), bar(m7b5(PitchClass.D), dom7(PitchClass.G)), bar(m7(PitchClass.C)), bar(m7b5(PitchClass.D), dom7(PitchClass.G)),
                bar(m7(PitchClass.C)), bar(m7(PitchClass.F)), bar(m7b5(PitchClass.D)), bar(dom7(PitchClass.G))
            )
        ),
        Progression(
            id = "std_31_lady_bird",
            title = "Lady Bird",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C)), bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb)),
                bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C)), bar(m7(PitchClass.Ab)), bar(dom7(PitchClass.Db)),
                bar(maj7(PitchClass.C)), bar(m7(PitchClass.A)), bar(m7(PitchClass.D)), bar(dom7(PitchClass.G))
            )
        ),
        Progression(
            id = "std_32_on_green_dolphin_street",
            title = "On Green Dolphin Street",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Eb,
            bars = listOf(
                bar(maj7(PitchClass.Eb)), bar(maj7(PitchClass.Eb)), bar(maj7(PitchClass.Gb)), bar(maj7(PitchClass.Gb)),
                bar(maj7(PitchClass.F)), bar(dom7(PitchClass.E)), bar(maj7(PitchClass.Eb)), bar(dom7(PitchClass.Bb))
            )
        ),
        Progression(
            id = "std_33_body_and_soul",
            title = "Body And Soul",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Db,
            bars = listOf(
                bar(m7(PitchClass.Eb)), bar(dom7(PitchClass.Bb)), bar(m7(PitchClass.Eb)), bar(dom7(PitchClass.Ab)),
                bar(maj7(PitchClass.Db)), bar(maj7(PitchClass.Gb)), bar(m7b5(PitchClass.F)), bar(dom7(PitchClass.Bb))
            )
        ),
        Progression(
            id = "std_34_stella_by_starlight",
            title = "Stella By Starlight",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Bb,
            bars = listOf(
                bar(m7b5(PitchClass.E)), bar(dom7(PitchClass.A)), bar(m7(PitchClass.C)), bar(dom7(PitchClass.F)),
                bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb)), bar(maj7(PitchClass.Eb)), bar(dom7(PitchClass.Ab)),
                bar(maj7(PitchClass.Bb)), bar(m7(PitchClass.E)), bar(dom7(PitchClass.A)), bar(m7(PitchClass.D))
            )
        ),
        Progression(
            id = "std_35_days_of_wine_and_roses",
            title = "Days Of Wine And Roses",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(maj7(PitchClass.F)), bar(maj7(PitchClass.Eb)), bar(dom7(PitchClass.D)), bar(dom7(PitchClass.D)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(m7(PitchClass.A)), bar(dom7(PitchClass.D))
            )
        ),
        Progression(
            id = "std_36_just_friends",
            title = "Just Friends",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(maj7(PitchClass.Bb)), bar(maj7(PitchClass.Bb)), bar(min(PitchClass.Bb)), bar(min(PitchClass.Bb)),
                bar(maj7(PitchClass.F)), bar(maj7(PitchClass.F)), bar(m7(PitchClass.A)), bar(dom7(PitchClass.D)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(maj7(PitchClass.F)), bar(dom7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_37_night_and_day",
            title = "Night And Day",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(maj7(PitchClass.Ab)), bar(maj7(PitchClass.Ab)), bar(dom7(PitchClass.G)), bar(dom7(PitchClass.G)),
                bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_38_my_funny_valentine",
            title = "My Funny Valentine",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(min(PitchClass.C)), bar(maj7(PitchClass.C)), bar(m7(PitchClass.C)), bar(dom7(PitchClass.A)),
                bar(maj7(PitchClass.Ab)), bar(m7(PitchClass.F)), bar(m7b5(PitchClass.D)), bar(dom7(PitchClass.G))
            )
        ),
        Progression(
            id = "std_39_misty",
            title = "Misty",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Eb,
            bars = listOf(
                bar(maj7(PitchClass.Eb)), bar(m7(PitchClass.Bb), dom7(PitchClass.Eb)), bar(maj7(PitchClass.Ab)), bar(dom7(PitchClass.Ab)),
                bar(maj7(PitchClass.Eb)), bar(m7(PitchClass.C)), bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb))
            )
        ),
        Progression(
            id = "std_40_girl_from_ipanema",
            title = "The Girl From Ipanema",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(maj7(PitchClass.F)), bar(maj7(PitchClass.F)), bar(dom7(PitchClass.G)), bar(dom7(PitchClass.G)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.Gb)), bar(maj7(PitchClass.F)), bar(dom7(PitchClass.Gb))
            )
        ),
        Progression(
            id = "std_41_recorda_me",
            title = "Recorda-Me",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.A,
            bars = listOf(
                bar(m7(PitchClass.A)), bar(m7(PitchClass.A)), bar(m7(PitchClass.C)), bar(dom7(PitchClass.F)),
                bar(maj7(PitchClass.Bb)), bar(maj7(PitchClass.Bb)), bar(m7b5(PitchClass.B)), bar(dom7(PitchClass.E))
            )
        ),
        Progression(
            id = "std_42_blue_monk",
            title = "Blue Monk",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Bb,
            bars = listOf(
                bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Eb)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)),
                bar(dom7(PitchClass.Eb)), bar(dom7(PitchClass.Eb)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.G)),
                bar(m7(PitchClass.C)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.F))
            )
        ),
        Progression(
            id = "std_43_straight_no_chaser",
            title = "Straight No Chaser",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(dom7(PitchClass.F)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.F)),
                bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.D)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(dom7(PitchClass.F)), bar(dom7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_44_song_for_my_father",
            title = "Song For My Father",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(m7(PitchClass.F)), bar(m7(PitchClass.F)), bar(dom7(PitchClass.Eb)), bar(dom7(PitchClass.Eb)),
                bar(dom7(PitchClass.Db)), bar(dom7(PitchClass.C)), bar(m7(PitchClass.F)), bar(m7(PitchClass.F))
            )
        ),
        Progression(
            id = "std_45_st_thomas",
            title = "St. Thomas",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(maj(PitchClass.C)), bar(maj(PitchClass.C)), bar(dom7(PitchClass.G)), bar(maj(PitchClass.C)),
                bar(maj(PitchClass.C)), bar(maj(PitchClass.C)), bar(dom7(PitchClass.G)), bar(maj(PitchClass.C)),
                bar(m7(PitchClass.E)), bar(m7(PitchClass.A)), bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)),
                bar(maj(PitchClass.C)), bar(dom7(PitchClass.G)), bar(maj(PitchClass.C)), bar(maj(PitchClass.C))
            )
        ),
        Progression(
            id = "std_46_cherokee",
            title = "Cherokee",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Bb,
            bars = listOf(
                bar(maj7(PitchClass.Bb)), bar(maj7(PitchClass.Bb)), bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb)),
                bar(maj7(PitchClass.Eb)), bar(maj7(PitchClass.Eb)), bar(dom7(PitchClass.Ab)), bar(dom7(PitchClass.Ab)),
                bar(maj7(PitchClass.Bb)), bar(dom7(PitchClass.G)), bar(m7(PitchClass.C)), bar(dom7(PitchClass.F))
            )
        ),
        Progression(
            id = "std_47_confirmation",
            title = "Confirmation",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(maj7(PitchClass.F)), bar(m7b5(PitchClass.E), dom7(PitchClass.A)), bar(m7(PitchClass.D), dom7(PitchClass.G)), bar(m7(PitchClass.C), dom7(PitchClass.F)),
                bar(dom7(PitchClass.Bb)), bar(dom7(PitchClass.A)), bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)),
                bar(m7(PitchClass.G)), bar(dom7(PitchClass.C)), bar(maj7(PitchClass.F)), bar(dom7(PitchClass.C))
            )
        ),
        Progression(
            id = "std_48_yardbird_suite",
            title = "Yardbird Suite",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.C,
            bars = listOf(
                bar(maj7(PitchClass.C)), bar(m7(PitchClass.F)), bar(dom7(PitchClass.Bb)), bar(maj7(PitchClass.C)),
                bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)), bar(maj7(PitchClass.E)), bar(dom7(PitchClass.A)),
                bar(m7(PitchClass.D)), bar(dom7(PitchClass.G)), bar(maj7(PitchClass.C)), bar(dom7(PitchClass.G))
            )
        ),
        Progression(
            id = "std_49_joy_spring",
            title = "Joy Spring",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.F,
            bars = listOf(
                bar(maj7(PitchClass.F)), bar(m7(PitchClass.G), dom7(PitchClass.C)), bar(m7(PitchClass.A), dom7(PitchClass.D)), bar(m7(PitchClass.G), dom7(PitchClass.C)),
                bar(maj7(PitchClass.F)), bar(m7(PitchClass.F), dom7(PitchClass.Bb)), bar(maj7(PitchClass.Eb)), bar(dom7(PitchClass.Eb))
            )
        ),
        Progression(
            id = "std_50_round_midnight",
            title = "'Round Midnight",
            category = "Jazz Standards & Repertoire",
            defaultKey = PitchClass.Eb,
            bars = listOf(
                bar(min(PitchClass.Eb)), bar(m7b5(PitchClass.F), dom7(PitchClass.Bb)), bar(min(PitchClass.Eb)), bar(dom7(PitchClass.Ab)),
                bar(maj7(PitchClass.Db)), bar(m7(PitchClass.C), dom7(PitchClass.F)), bar(min(PitchClass.Bb)), bar(dom7(PitchClass.Bb))
            )
        ),

        // ==========================================
        // MODAL VAMPS
        // ==========================================
        Progression(
            id = "vamp_so_what",
            title = "So What (Dorian Vamp)",
            category = "Modal Vamps",
            defaultKey = PitchClass.D,
            bars = listOf(bar(m7(PitchClass.D)), bar(m7(PitchClass.D)), bar(m7(PitchClass.Eb)), bar(m7(PitchClass.D)))
        ),
        Progression(
            id = "vamp_mixo_funk",
            title = "Mixolydian Funk Groove",
            category = "Modal Vamps",
            defaultKey = PitchClass.A,
            bars = listOf(bar(dom7(PitchClass.A)), bar(dom7(PitchClass.A)), bar(dom7(PitchClass.D)), bar(dom7(PitchClass.A)))
        ),
        Progression(
            id = "vamp_lydian_space",
            title = "Lydian Ambient Space Jam",
            category = "Modal Vamps",
            defaultKey = PitchClass.C,
            bars = listOf(bar(maj7(PitchClass.C)), bar(maj7(PitchClass.C)), bar(dom7(PitchClass.D)), bar(maj7(PitchClass.C)))
        ),
        Progression(
            id = "vamp_phrygian_dark",
            title = "Phrygian Dark Drone",
            category = "Modal Vamps",
            defaultKey = PitchClass.E,
            bars = listOf(bar(min(PitchClass.E)), bar(maj(PitchClass.F)), bar(min(PitchClass.E)), bar(maj(PitchClass.D)))
        )
    )
}
