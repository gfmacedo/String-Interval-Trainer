package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.PitchDetectorEngine
import com.example.audio.PitchEvent
import com.example.audio.SynthesizerAudioEngine
import com.example.data.db.AppDatabase
import com.example.data.db.WorkoutSessionEntity
import com.example.data.db.WorkoutSessionRepository
import com.example.data.model.Chord
import com.example.data.model.ChangesCycleMode
import com.example.data.model.ChangesStartingNote
import com.example.data.model.ChangesStructurePreset
import com.example.data.model.CycleMode
import com.example.data.model.ExerciseSessionState
import com.example.data.model.FretCoordinate
import com.example.data.model.FretRange
import com.example.data.model.IntervalOrdering
import com.example.data.model.IntervallicFunction
import com.example.data.model.PitchClass
import com.example.data.model.Progression
import com.example.data.model.RootSelectionStrategy
import com.example.data.model.ScaleDefinition
import com.example.data.model.ScaleOrderingAlgorithm
import com.example.data.model.TheoryDatabase
import com.example.data.model.TrainerModule
import com.example.data.model.Tuning
import com.example.data.model.WorkoutTargetPrompt
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppScreen {
    DASHBOARD,
    NOTE_TRAINER_SETUP,
    INTERVAL_TRAINER_SETUP,
    CHANGES_TRAINER_SETUP,
    SCALE_TRAINER_SETUP,
    WORKOUT_HUD,
    AUDIO_CALIBRATION,
    PROGRESSION_BUILDER,
    WORKOUT_HISTORY
}

class TrainerViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = WorkoutSessionRepository(db.workoutSessionDao())

    val workoutHistory: StateFlow<List<WorkoutSessionEntity>> = repository.allSessions.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Navigation State
    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Instrument Tuning
    private val _currentTuning = MutableStateFlow(Tuning.GUITAR_STANDARD)
    val currentTuning: StateFlow<Tuning> = _currentTuning.asStateFlow()

    // Audio Engine & Hardware State
    private val _audioHealthStatus = MutableStateFlow(PitchDetectorEngine.AudioHealthStatus.ACTIVE)
    val audioHealthStatus: StateFlow<PitchDetectorEngine.AudioHealthStatus> = _audioHealthStatus.asStateFlow()

    private val _vuLevel = MutableStateFlow(0f)
    val vuLevel: StateFlow<Float> = _vuLevel.asStateFlow()

    private val _pitchReference = MutableStateFlow(440.0f)
    val pitchReference: StateFlow<Float> = _pitchReference.asStateFlow()

    private val _inputSensitivity = MutableStateFlow(2.2f) // Boosted default gain for quieter guitar
    val inputSensitivity: StateFlow<Float> = _inputSensitivity.asStateFlow()

    private val _noiseGateDb = MutableStateFlow(-45.0f)
    val noiseGateDb: StateFlow<Float> = _noiseGateDb.asStateFlow()

    private val _isBassFilterEnabled = MutableStateFlow(false)
    val isBassFilterEnabled: StateFlow<Boolean> = _isBassFilterEnabled.asStateFlow()

    private val _usbChannel = MutableStateFlow(1) // 1 or 2
    val usbChannel: StateFlow<Int> = _usbChannel.asStateFlow()

    private val _isMicListening = MutableStateFlow(true)
    val isMicListening: StateFlow<Boolean> = _isMicListening.asStateFlow()

    // Audio Calibration & Feedback Toggles (Defaulted to OFF as requested)
    private val _hapticFeedbackEnabled = MutableStateFlow(false)
    val hapticFeedbackEnabled: StateFlow<Boolean> = _hapticFeedbackEnabled.asStateFlow()

    private val _confirmationSoundsEnabled = MutableStateFlow(false)
    val confirmationSoundsEnabled: StateFlow<Boolean> = _confirmationSoundsEnabled.asStateFlow()

    // Fretboard & UI constraints
    private val _fretRange = MutableStateFlow(FretRange.ALL)
    val fretRange: StateFlow<FretRange> = _fretRange.asStateFlow()

    private val _allowedStrings = MutableStateFlow((0..5).toSet())
    val allowedStrings: StateFlow<Set<Int>> = _allowedStrings.asStateFlow()

    private val _selectedFret = MutableStateFlow<FretCoordinate?>(null)
    val selectedFret: StateFlow<FretCoordinate?> = _selectedFret.asStateFlow()

    // Workout State Machine
    private val _sessionState = MutableStateFlow<ExerciseSessionState>(ExerciseSessionState.Idle)
    val sessionState: StateFlow<ExerciseSessionState> = _sessionState.asStateFlow()

    // Setup Configurations
    // Screen 02: Note Trainer
    val noteFilterPitchClasses = MutableStateFlow(PitchClass.entries.toSet())
    val noteCycleMode = MutableStateFlow(CycleMode.PURE_RANDOM)

    // Screen 03: Interval Trainer
    val intervalRootStrategy = MutableStateFlow(RootSelectionStrategy.FIXED_ROOT)
    val intervalFixedRoot = MutableStateFlow(PitchClass.C)
    val intervalSelectedFunctions = MutableStateFlow(
        setOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH
        )
    )
    val intervalOrdering = MutableStateFlow(IntervalOrdering.RANDOMIZED)

    // Screen 04: Changes Trainer
    val customProgressions = MutableStateFlow<List<Progression>>(emptyList())
    val selectedProgression = MutableStateFlow<Progression>(TheoryDatabase.PROGRESSIONS.first())
    val changesTransposedKey = MutableStateFlow<PitchClass>(PitchClass.C)
    val changesTranspositionEnabled = MutableStateFlow(false) // Toggle: Transpose vs Original Key
    val changesCycleMode = MutableStateFlow(ChangesCycleMode.REPEAT) // 3 exclusive choices: Repeat, +5th, +4th
    val changesStructurePreset = MutableStateFlow(ChangesStructurePreset.CHORD_TONES) // Replaces difficulty slider
    val changesStartingNote = MutableStateFlow(ChangesStartingNote.FIRST) // 1st, 2nd, 3rd, 4th note sequence rotation
    val changesRandomizeIntervalOrder = MutableStateFlow(false) // Randomize interval order per chord

    // Screen 05: Scale Trainer
    val selectedScale = MutableStateFlow<ScaleDefinition>(TheoryDatabase.SCALES.first())
    val scaleRoot = MutableStateFlow<PitchClass>(PitchClass.C)
    val scaleOrdering = MutableStateFlow(ScaleOrderingAlgorithm.LINEAR_ASCENDING)

    // Audio Engines
    private val synth = SynthesizerAudioEngine()
    private val pitchDetector = PitchDetectorEngine(
        onPitchDetected = { event -> handlePitchDetected(event) },
        onVuLevelChanged = { level -> _vuLevel.value = level },
        onStatusChanged = { status -> _audioHealthStatus.value = status }
    )

    private var workoutTimerJob: Job? = null
    private var workoutPromptsQueue: List<WorkoutTargetPrompt> = emptyList()
    private var currentPromptIndex: Int = 0
    private var promptStartTime: Long = 0L
    private val responseLatenciesMs = mutableListOf<Long>()
    private var totalPromptsPresented = 0
    private var correctCount = 0
    private var currentStreak = 0
    private var maxStreak = 0
    private var currentScore = 0
    private val weakIntervalsTally = mutableMapOf<String, Int>()

    init {
        // Apply initial audio engine settings
        syncPitchDetectorSettings()
    }

    private fun syncPitchDetectorSettings() {
        pitchDetector.pitchReference = _pitchReference.value
        pitchDetector.inputSensitivity = _inputSensitivity.value
        pitchDetector.noiseGateThresholdDb = _noiseGateDb.value
        pitchDetector.isBassFilterEnabled = _isBassFilterEnabled.value
        pitchDetector.usbChannel = _usbChannel.value
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setTuning(tuning: Tuning) {
        _currentTuning.value = tuning
        _allowedStrings.value = (0 until tuning.stringCount).toSet()
    }

    fun toggleStringAllowed(stringIdx: Int) {
        val current = _allowedStrings.value.toMutableSet()
        if (current.contains(stringIdx) && current.size > 1) {
            current.remove(stringIdx)
        } else {
            current.add(stringIdx)
        }
        _allowedStrings.value = current
    }

    fun setFretRange(range: FretRange) {
        _fretRange.value = range
    }

    fun setPitchReference(referenceHz: Float) {
        _pitchReference.value = referenceHz
        pitchDetector.pitchReference = referenceHz
    }

    fun setInputSensitivity(sensitivity: Float) {
        _inputSensitivity.value = sensitivity
        pitchDetector.inputSensitivity = sensitivity
    }

    fun setNoiseGateDb(db: Float) {
        _noiseGateDb.value = db
        pitchDetector.noiseGateThresholdDb = db
    }

    fun setIsBassFilterEnabled(enabled: Boolean) {
        _isBassFilterEnabled.value = enabled
        pitchDetector.isBassFilterEnabled = enabled
    }

    fun setUsbChannel(channel: Int) {
        _usbChannel.value = channel
        pitchDetector.usbChannel = channel
    }

    fun setHapticFeedbackEnabled(enabled: Boolean) {
        _hapticFeedbackEnabled.value = enabled
    }

    fun setConfirmationSoundsEnabled(enabled: Boolean) {
        _confirmationSoundsEnabled.value = enabled
    }

    fun clearWorkoutHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }

    fun toggleMicListening(enabled: Boolean) {
        _isMicListening.value = enabled
        if (enabled) {
            pitchDetector.start(viewModelScope)
        } else {
            pitchDetector.stop()
        }
    }

    fun startListeningAudio() {
        if (_isMicListening.value) {
            syncPitchDetectorSettings()
            pitchDetector.start(viewModelScope)
        }
    }

    fun stopListeningAudio() {
        pitchDetector.stop()
    }

    // ==========================================
    // Workout State Machine Execution Loop
    // ==========================================

    fun startWorkout(module: TrainerModule) {
        totalPromptsPresented = 0
        correctCount = 0
        currentStreak = 0
        maxStreak = 0
        currentScore = 0
        responseLatenciesMs.clear()
        weakIntervalsTally.clear()
        currentPromptIndex = 0

        // Step 1: Queue Generation
        workoutPromptsQueue = generateWorkoutQueue(module)
        if (workoutPromptsQueue.isEmpty()) return

        val firstPrompt = workoutPromptsQueue[0]
        promptStartTime = System.currentTimeMillis()

        _sessionState.value = ExerciseSessionState.Active(
            module = module,
            promptIndex = 0,
            totalQueueSize = workoutPromptsQueue.size,
            currentChord = firstPrompt.activeChord,
            targetRoot = firstPrompt.targetRoot,
            targetInterval = firstPrompt.targetInterval,
            requiredPitchClass = firstPrompt.requiredPitchClass,
            timeRemainingSeconds = 0, // Elapsed time starts at 0
            isEndless = true,
            score = 0,
            streak = 0,
            maxStreak = 0,
            showFretboardDiagram = false,
            revealNoteName = false,
            isPaused = false,
            lastPitchEvaluation = null,
            cycleIntervals = firstPrompt.cycleIntervals,
            cycleIndex = firstPrompt.cycleIndex,
            completedIntervalIndices = firstPrompt.completedIntervalIndices
        )

        // Step 2 & 3: Presentation and Audio Capture
        pitchDetector.clearTransientState()
        startListeningAudio()
        _currentScreen.value = AppScreen.WORKOUT_HUD

        // Start session elapsed time counter (Endless mode)
        workoutTimerJob?.cancel()
        workoutTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val current = _sessionState.value
                if (current is ExerciseSessionState.Active && !current.isPaused) {
                    _sessionState.value = current.copy(timeRemainingSeconds = current.timeRemainingSeconds + 1)
                }
            }
        }
    }

    private fun generateWorkoutQueue(module: TrainerModule): List<WorkoutTargetPrompt> {
        val list = mutableListOf<WorkoutTargetPrompt>()

        when (module) {
            TrainerModule.NOTE_TRAINER -> {
                val selectedPcs = noteFilterPitchClasses.value.toList().ifEmpty { PitchClass.entries }
                val cycle = noteCycleMode.value

                val orderedNotes: List<PitchClass> = when (cycle) {
                    CycleMode.PURE_RANDOM -> {
                        (0 until 60).map { selectedPcs.random() }
                    }
                    CycleMode.CYCLE_OF_4THS -> {
                        // Circle of 4ths: C -> F -> Bb -> Eb -> Ab -> Db -> Gb -> B -> E -> A -> D -> G
                        val circle4ths = listOf(
                            PitchClass.C, PitchClass.F, PitchClass.Bb, PitchClass.Eb,
                            PitchClass.Ab, PitchClass.Db, PitchClass.Gb, PitchClass.B,
                            PitchClass.E, PitchClass.A, PitchClass.D, PitchClass.G
                        ).filter { it in selectedPcs }
                        (0 until 5).flatMap { circle4ths }
                    }
                    CycleMode.CYCLE_OF_5THS -> {
                        // Circle of 5ths: C -> G -> D -> A -> E -> B -> Gb -> Db -> Ab -> Eb -> Bb -> F
                        val circle5ths = listOf(
                            PitchClass.C, PitchClass.G, PitchClass.D, PitchClass.A,
                            PitchClass.E, PitchClass.B, PitchClass.Gb, PitchClass.Db,
                            PitchClass.Ab, PitchClass.Eb, PitchClass.Bb, PitchClass.F
                        ).filter { it in selectedPcs }
                        (0 until 5).flatMap { circle5ths }
                    }
                    CycleMode.CHROMATIC -> {
                        (0 until 5).flatMap { selectedPcs.sortedBy { it.semitoneOffset } }
                    }
                }

                orderedNotes.forEach { pc ->
                    list.add(
                        WorkoutTargetPrompt(
                            targetRoot = pc,
                            targetInterval = IntervallicFunction.ROOT,
                            requiredPitchClass = pc,
                            noteContextLabel = "Find note ${pc.standardLabel}",
                            cycleIntervals = listOf(IntervallicFunction.ROOT),
                            cycleIndex = 0,
                            completedIntervalIndices = emptySet()
                        )
                    )
                }
            }

            TrainerModule.INTERVAL_TRAINER -> {
                val rawIntervals = intervalSelectedFunctions.value.toList().ifEmpty { listOf(IntervallicFunction.ROOT) }
                val isDynamic = intervalRootStrategy.value == RootSelectionStrategy.DYNAMIC_RANDOM_ROOT
                val ordering = intervalOrdering.value

                val orderedIntervals = when (ordering) {
                    IntervalOrdering.ASCENDING_FORMULA -> rawIntervals.sortedBy { it.semitones }
                    IntervalOrdering.DESCENDING_FORMULA -> rawIntervals.sortedByDescending { it.semitones }
                    IntervalOrdering.RANDOMIZED -> rawIntervals
                }

                val cycleCount = if (isDynamic) 25 else 35
                for (cycle in 0 until cycleCount) {
                    val root = if (isDynamic) PitchClass.entries.random() else intervalFixedRoot.value
                    val intervalsInCycle = if (ordering == IntervalOrdering.RANDOMIZED) {
                        orderedIntervals.shuffled()
                    } else {
                        orderedIntervals
                    }

                    intervalsInCycle.forEachIndexed { idx, interval ->
                        val requiredMidi = (root.semitoneOffset + interval.semitones) % 12
                        val requiredPc = PitchClass.fromMidi(requiredMidi)
                        val completedSet = (0 until idx).toSet()

                        list.add(
                            WorkoutTargetPrompt(
                                targetRoot = root,
                                targetInterval = interval,
                                requiredPitchClass = requiredPc,
                                noteContextLabel = "${interval.label} of ${root.standardLabel}",
                                cycleIntervals = intervalsInCycle,
                                cycleIndex = idx,
                                completedIntervalIndices = completedSet
                            )
                        )
                    }
                }
            }

            TrainerModule.CHANGES_TRAINER -> {
                val prog = selectedProgression.value
                val isTranspositionOn = changesTranspositionEnabled.value
                val baseKey = if (isTranspositionOn) changesTransposedKey.value else prog.defaultKey
                val cycleMode = changesCycleMode.value
                val preset = changesStructurePreset.value

                // Exclusive cycle progression once the whole cadence is complete:
                // 1. Repeat (repeats cadence in baseKey)
                // 2. Transposed a perfect 5th higher (+7 semitones)
                // 3. Transposed a perfect 4th higher (+5 semitones)
                val keySequence: List<PitchClass> = when (cycleMode) {
                    ChangesCycleMode.REPEAT -> List(8) { baseKey }
                    ChangesCycleMode.CYCLE_OF_5THS -> {
                        (0 until 12).map { step ->
                            PitchClass.fromMidi(baseKey.semitoneOffset + step * 7)
                        }
                    }
                    ChangesCycleMode.CYCLE_OF_4THS -> {
                        (0 until 12).map { step ->
                            PitchClass.fromMidi(baseKey.semitoneOffset + step * 5)
                        }
                    }
                }

                keySequence.forEachIndexed { _, currentCadenceKey ->
                    val semitoneShift = (currentCadenceKey.semitoneOffset - prog.defaultKey.semitoneOffset + 12) % 12

                    prog.bars.forEachIndexed { barIdx, chordList ->
                        chordList.forEach { chord ->
                            val transposedRoot = PitchClass.fromMidi(chord.root.semitoneOffset + semitoneShift)
                            val transposedChord = Chord(transposedRoot, chord.qualityLabel, chord.intervals)

                            val baseIntervals = when (preset) {
                                ChangesStructurePreset.CHORD_TONES -> chord.intervals
                                ChangesStructurePreset.ROOTS_ONLY -> listOf(IntervallicFunction.ROOT)
                                ChangesStructurePreset.ROOTS_AND_FIFTHS -> listOf(IntervallicFunction.ROOT, IntervallicFunction.PERFECT_FIFTH)
                                ChangesStructurePreset.TRIAD_TONES -> chord.intervals.filter {
                                    it in listOf(IntervallicFunction.ROOT, IntervallicFunction.MAJOR_THIRD, IntervallicFunction.MINOR_THIRD, IntervallicFunction.PERFECT_FIFTH)
                                }.ifEmpty { listOf(IntervallicFunction.ROOT) }
                                ChangesStructurePreset.GUIDE_TONES -> chord.intervals.filter {
                                    it in listOf(IntervallicFunction.MAJOR_THIRD, IntervallicFunction.MINOR_THIRD, IntervallicFunction.MAJOR_SEVENTH, IntervallicFunction.MINOR_SEVENTH)
                                }.ifEmpty { listOf(IntervallicFunction.ROOT) }
                                ChangesStructurePreset.IONIAN_MODE -> listOf(
                                    IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
                                    IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
                                    IntervallicFunction.MAJOR_SEVENTH
                                )
                                ChangesStructurePreset.DORIAN_MODE -> listOf(
                                    IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MINOR_THIRD,
                                    IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
                                    IntervallicFunction.MINOR_SEVENTH
                                )
                                ChangesStructurePreset.PHRYGIAN_MODE -> listOf(
                                    IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
                                    IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SIXTH,
                                    IntervallicFunction.MINOR_SEVENTH
                                )
                                ChangesStructurePreset.LYDIAN_MODE -> listOf(
                                    IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
                                    IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
                                    IntervallicFunction.MAJOR_SEVENTH
                                )
                                ChangesStructurePreset.MIXOLYDIAN_MODE -> listOf(
                                    IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MAJOR_THIRD,
                                    IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MAJOR_SIXTH,
                                    IntervallicFunction.MINOR_SEVENTH
                                )
                                ChangesStructurePreset.AEOLIAN_MODE -> listOf(
                                    IntervallicFunction.ROOT, IntervallicFunction.MAJOR_SECOND, IntervallicFunction.MINOR_THIRD,
                                    IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.PERFECT_FIFTH, IntervallicFunction.MINOR_SIXTH,
                                    IntervallicFunction.MINOR_SEVENTH
                                )
                                ChangesStructurePreset.LOCRIAN_MODE -> listOf(
                                    IntervallicFunction.ROOT, IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
                                    IntervallicFunction.PERFECT_FOURTH, IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SIXTH,
                                    IntervallicFunction.MINOR_SEVENTH
                                )
                                ChangesStructurePreset.ALTERED_TENSIONS -> chord.intervals + listOf(
                                    IntervallicFunction.MINOR_SECOND, IntervallicFunction.MINOR_THIRD,
                                    IntervallicFunction.DIMINISHED_FIFTH, IntervallicFunction.MINOR_SIXTH
                                )
                            }.distinct().ifEmpty { listOf(IntervallicFunction.ROOT) }

                            // Apply starting note cyclic rotation (starts from 2nd, 3rd, or 4th note, playing whole sequence)
                            // Or randomize target interval order if toggle is enabled
                            val isRandomized = changesRandomizeIntervalOrder.value
                            val startNote = changesStartingNote.value
                            val targetIntervals = if (isRandomized) {
                                baseIntervals.shuffled()
                            } else {
                                if (baseIntervals.size <= 1) {
                                    baseIntervals
                                } else {
                                    val offset = startNote.offset % baseIntervals.size
                                    baseIntervals.drop(offset) + baseIntervals.take(offset)
                                }
                            }

                            targetIntervals.forEachIndexed { idx, interval ->
                                val requiredMidi = (transposedRoot.semitoneOffset + interval.semitones) % 12
                                val requiredPc = PitchClass.fromMidi(requiredMidi)
                                val keyInfo = if (isTranspositionOn || cycleMode != ChangesCycleMode.REPEAT) " [Key: ${currentCadenceKey.standardLabel}]" else ""
                                list.add(
                                    WorkoutTargetPrompt(
                                        targetRoot = transposedRoot,
                                        targetInterval = interval,
                                        requiredPitchClass = requiredPc,
                                        activeChord = transposedChord,
                                        barIndex = barIdx + 1,
                                        noteContextLabel = "${transposedChord.displayName}$keyInfo ► ${interval.label}",
                                        cycleIntervals = targetIntervals,
                                        cycleIndex = idx,
                                        completedIntervalIndices = (0 until idx).toSet()
                                    )
                                )
                            }
                        }
                    }
                }
            }

            TrainerModule.SCALE_TRAINER -> {
                val scale = selectedScale.value
                val root = scaleRoot.value
                val intervals = scale.intervals

                val sequence: List<IntervallicFunction> = when (scaleOrdering.value) {
                    ScaleOrderingAlgorithm.LINEAR_ASCENDING -> intervals.sortedBy { it.semitones }
                    ScaleOrderingAlgorithm.LINEAR_DESCENDING -> intervals.sortedByDescending { it.semitones }
                    ScaleOrderingAlgorithm.BROKEN_THIRDS -> {
                        // 1-3-2-4-3-5-4-6...
                        val sorted = intervals.sortedBy { it.semitones }
                        val n = sorted.size
                        val seq = mutableListOf<IntervallicFunction>()
                        for (i in 0 until n) {
                            seq.add(sorted[i])
                            seq.add(sorted[(i + 2) % n])
                        }
                        seq
                    }
                    ScaleOrderingAlgorithm.RANDOM_FUNCTIONAL -> intervals.shuffled()
                }

                for (cycle in 0 until 10) {
                    sequence.forEachIndexed { idx, interval ->
                        val requiredMidi = (root.semitoneOffset + interval.semitones) % 12
                        val requiredPc = PitchClass.fromMidi(requiredMidi)
                        list.add(
                            WorkoutTargetPrompt(
                                targetRoot = root,
                                targetInterval = interval,
                                requiredPitchClass = requiredPc,
                                noteContextLabel = "${scale.name} in ${root.standardLabel} ► ${interval.label}",
                                cycleIntervals = sequence,
                                cycleIndex = idx,
                                completedIntervalIndices = (0 until idx).toSet()
                            )
                        )
                    }
                }
            }
        }

        return list
    }

    // Step 4: Pitch Class Comparison & Validation
    fun handlePitchDetected(event: PitchEvent) {
        val state = _sessionState.value
        if (state !is ExerciseSessionState.Active || state.isPaused) return

        validatePitch(event.pitchClass, confidence = event.confidence)
    }

    fun onFretTappedInWorkout(coord: FretCoordinate) {
        _selectedFret.value = coord

        // Play synthesized guitar pluck tone
        synth.playPluck(coord.pitchClass.frequency(octave = 3, pitchReference = _pitchReference.value), scope = viewModelScope)

        val state = _sessionState.value
        if (state is ExerciseSessionState.Active && !state.isPaused) {
            validatePitch(coord.pitchClass, confidence = 1.0f)
        }
    }

    private fun validatePitch(detectedPitchClass: PitchClass, confidence: Float) {
        val state = _sessionState.value
        if (state !is ExerciseSessionState.Active || state.isPaused) return

        totalPromptsPresented++
        val now = System.currentTimeMillis()
        val responseTime = (now - promptStartTime).coerceAtLeast(100L)

        if (detectedPitchClass == state.requiredPitchClass) {
            // Correct note hit!
            correctCount++
            currentStreak++
            if (currentStreak > maxStreak) maxStreak = currentStreak
            currentScore += (100 + currentStreak * 10)
            responseLatenciesMs.add(responseTime)

            // Audio & Haptic Feedback
            if (_confirmationSoundsEnabled.value) {
                synth.playSuccessChime(viewModelScope)
            }
            triggerHapticFeedback(success = true)

            // Step 4.2: Advance to next prompt in queue
            val nextIndex = currentPromptIndex + 1
            if (nextIndex >= workoutPromptsQueue.size) {
                // In Endless mode, loop queue seamlessly
                currentPromptIndex = 0
                advanceToPrompt(0, state, detectedPitchClass, isCorrect = true, confidence)
            } else {
                currentPromptIndex = nextIndex
                advanceToPrompt(nextIndex, state, detectedPitchClass, isCorrect = true, confidence)
            }
        } else {
            // Incorrect note hit
            currentStreak = 0
            val intervalName = state.targetInterval.label
            weakIntervalsTally[intervalName] = (weakIntervalsTally[intervalName] ?: 0) + 1

            if (_confirmationSoundsEnabled.value) {
                synth.playIncorrectTone(viewModelScope)
            }
            triggerHapticFeedback(success = false)

            // Update evaluation status badge, but DO NOT advance!
            _sessionState.value = state.copy(
                streak = 0,
                lastPitchEvaluation = ExerciseSessionState.PitchEvaluated(
                    detectedPitchClass = detectedPitchClass,
                    isCorrect = false,
                    confidence = confidence
                )
            )
        }
    }

    private fun advanceToPrompt(
        index: Int,
        prevState: ExerciseSessionState.Active,
        detectedPitch: PitchClass,
        isCorrect: Boolean,
        confidence: Float
    ) {
        pitchDetector.clearTransientState()
        val nextPrompt = workoutPromptsQueue[index]
        promptStartTime = System.currentTimeMillis()

        _sessionState.value = prevState.copy(
            promptIndex = index,
            currentChord = nextPrompt.activeChord,
            targetRoot = nextPrompt.targetRoot,
            targetInterval = nextPrompt.targetInterval,
            requiredPitchClass = nextPrompt.requiredPitchClass,
            score = currentScore,
            streak = currentStreak,
            maxStreak = maxStreak,
            cycleIntervals = nextPrompt.cycleIntervals,
            cycleIndex = nextPrompt.cycleIndex,
            completedIntervalIndices = nextPrompt.completedIntervalIndices,
            lastPitchEvaluation = ExerciseSessionState.PitchEvaluated(
                detectedPitchClass = detectedPitch,
                isCorrect = isCorrect,
                confidence = confidence
            )
        )
    }

    fun toggleFretboardDiagram() {
        val state = _sessionState.value
        if (state is ExerciseSessionState.Active) {
            _sessionState.value = state.copy(showFretboardDiagram = !state.showFretboardDiagram)
        }
    }

    fun toggleRevealNoteName() {
        val state = _sessionState.value
        if (state is ExerciseSessionState.Active) {
            _sessionState.value = state.copy(revealNoteName = !state.revealNoteName)
        }
    }

    fun togglePauseWorkout() {
        val state = _sessionState.value
        if (state is ExerciseSessionState.Active) {
            _sessionState.value = state.copy(isPaused = !state.isPaused)
        }
    }

    // Step 5: Session Finalization & Room persistence
    fun finishWorkout() {
        workoutTimerJob?.cancel()
        workoutTimerJob = null
        stopListeningAudio()

        val state = _sessionState.value
        val module = if (state is ExerciseSessionState.Active) state.module else TrainerModule.NOTE_TRAINER

        val accuracy = if (totalPromptsPresented > 0) {
            (correctCount.toFloat() / totalPromptsPresented.toFloat()) * 100f
        } else {
            0f
        }

        val avgLatency = if (responseLatenciesMs.isNotEmpty()) {
            responseLatenciesMs.average().toLong()
        } else {
            0L
        }

        val sortedWeak = weakIntervalsTally.entries
            .sortedByDescending { it.value }
            .map { "${it.key} (${it.value} misses)" }

        // Automatically log workout info into Room database (accessible in Home screen at bottom)
        viewModelScope.launch {
            repository.saveSession(
                WorkoutSessionEntity(
                    moduleName = module.title,
                    score = currentScore,
                    totalNotes = totalPromptsPresented,
                    accuracyPercent = accuracy,
                    streakMax = maxStreak,
                    avgLatencyMs = avgLatency,
                    details = sortedWeak.joinToString(", ")
                )
            )
        }

        // Cleanly finish without displaying popup dialog, directly navigating to Home/Dashboard
        workoutTimerJob?.cancel()
        workoutTimerJob = null
        stopListeningAudio()
        _sessionState.value = ExerciseSessionState.Idle
        _currentScreen.value = AppScreen.DASHBOARD
    }

    fun exitWorkoutToDashboard() {
        workoutTimerJob?.cancel()
        workoutTimerJob = null
        stopListeningAudio()
        _sessionState.value = ExerciseSessionState.Idle
        _currentScreen.value = AppScreen.DASHBOARD
    }

    private fun triggerHapticFeedback(success: Boolean) {
        if (!_hapticFeedbackEnabled.value) return
        try {
            val context = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = if (success) {
                        VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE)
                    } else {
                        VibrationEffect.createWaveform(longArrayOf(0, 30, 40, 30), -1)
                    }
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(if (success) 40L else 80L)
                }
            }
        } catch (e: Exception) {
            // Ignore haptic errors on emulators/test environments
        }
    }

    // Progression Builder Helper
    fun addCustomProgression(progression: Progression) {
        customProgressions.value = customProgressions.value + progression
    }

    override fun onCleared() {
        super.onCleared()
        workoutTimerJob?.cancel()
        pitchDetector.stop()
    }
}
