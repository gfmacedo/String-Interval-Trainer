package com.example.data.model

enum class TrainerModule(val title: String, val subtitle: String, val moduleNumber: Int) {
    NOTE_TRAINER("Note Trainer", "Master instant absolute note identification across all strings.", 1),
    INTERVAL_TRAINER("Interval Trainer", "Learn to locate intervals from fixed and floating roots using 2-point shapes.", 2),
    CHANGES_TRAINER("Changes Trainer", "Outline chord progressions single-note across standards, cadences, and blues.", 3),
    SCALE_TRAINER("Scale Trainer", "Deconstruct modal, minor, and altered scales into functional interval steps.", 4)
}

enum class CycleMode(val displayName: String) {
    PURE_RANDOM("Pure Random"),
    CYCLE_OF_4THS("Cycle of 4ths"),
    CYCLE_OF_5THS("Cycle of 5ths"),
    CHROMATIC("Chromatic")
}

enum class RootSelectionStrategy(val displayName: String) {
    FIXED_ROOT("Fixed Root"),
    DYNAMIC_RANDOM_ROOT("Dynamic / Random Root")
}

enum class IntervalOrdering(val displayName: String) {
    RANDOMIZED("Randomized"),
    ASCENDING_FORMULA("Ascending Formula"),
    DESCENDING_FORMULA("Descending Formula")
}

enum class ScaleOrderingAlgorithm(val displayName: String) {
    LINEAR_ASCENDING("Linear (Ascending)"),
    LINEAR_DESCENDING("Linear (Descending)"),
    BROKEN_THIRDS("Broken Thirds (1-3-2-4-3-5...)"),
    RANDOM_FUNCTIONAL("Random Functional Order")
}

data class FretRange(
    val startFret: Int,
    val endFret: Int,
    val label: String
) {
    companion object {
        val ALL = FretRange(0, 24, "Frets 0–24 (Full Neck)")
        val OPEN_POS = FretRange(0, 5, "Frets 0–5 (Open Position)")
        val MID_POS = FretRange(5, 12, "Frets 5–12 (Mid Neck)")
        val HIGH_POS = FretRange(12, 24, "Frets 12–24 (Upper Register)")

        val PRESETS = listOf(ALL, OPEN_POS, MID_POS, HIGH_POS)
    }
}

/**
 * Prompt Item in the workout execution queue
 */
data class WorkoutTargetPrompt(
    val targetRoot: PitchClass,
    val targetInterval: IntervallicFunction,
    val requiredPitchClass: PitchClass,
    val activeChord: Chord? = null,
    val barIndex: Int = 0,
    val noteContextLabel: String = "",
    val cycleIntervals: List<IntervallicFunction> = emptyList(),
    val cycleIndex: Int = 0,
    val completedIntervalIndices: Set<Int> = emptySet()
)

/**
 * State machine states for active workout
 */
sealed class ExerciseSessionState {
    object Idle : ExerciseSessionState()

    data class Active(
        val module: TrainerModule,
        val promptIndex: Int,
        val totalQueueSize: Int,
        val currentChord: Chord?,
        val targetRoot: PitchClass,
        val targetInterval: IntervallicFunction,
        val requiredPitchClass: PitchClass,
        val timeRemainingSeconds: Int,
        val isEndless: Boolean,
        val score: Int,
        val streak: Int,
        val maxStreak: Int,
        val showFretboardDiagram: Boolean = false,
        val revealNoteName: Boolean = false,
        val isPaused: Boolean = false,
        val lastPitchEvaluation: PitchEvaluated? = null,
        val cycleIntervals: List<IntervallicFunction> = emptyList(),
        val cycleIndex: Int = 0,
        val completedIntervalIndices: Set<Int> = emptySet()
    ) : ExerciseSessionState()

    data class PitchEvaluated(
        val detectedPitchClass: PitchClass,
        val isCorrect: Boolean,
        val confidence: Float,
        val timestamp: Long = System.currentTimeMillis()
    )

    data class Completed(
        val module: TrainerModule,
        val totalNotesTargeted: Int,
        val correctNotes: Int,
        val accuracyPercent: Float,
        val averageResponseTimeMs: Long,
        val maxStreak: Int,
        val score: Int,
        val weakIntervals: List<String>
    ) : ExerciseSessionState()
}
