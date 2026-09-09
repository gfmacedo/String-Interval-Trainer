package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.audio.PitchEvent
import com.example.data.model.ExerciseSessionState
import com.example.data.model.IntervalOrdering
import com.example.data.model.IntervallicFunction
import com.example.data.model.PitchClass
import com.example.data.model.TrainerModule
import com.example.ui.viewmodel.TrainerViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class WorkoutQueueCycleTest {

    @Test
    fun testIntervalSequenceAndDimming() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        val vm = TrainerViewModel(app)

        // Select Root, Major 3rd, Perfect 5th in ascending order
        val selectedIntervals = setOf(
            IntervallicFunction.ROOT,
            IntervallicFunction.MAJOR_THIRD,
            IntervallicFunction.PERFECT_FIFTH
        )
        vm.intervalSelectedFunctions.value = selectedIntervals
        vm.intervalFixedRoot.value = PitchClass.C
        vm.intervalOrdering.value = IntervalOrdering.ASCENDING_FORMULA

        // Start Interval Trainer workout
        vm.startWorkout(TrainerModule.INTERVAL_TRAINER)

        var state = vm.sessionState.value as ExerciseSessionState.Active
        assertEquals(PitchClass.C, state.targetRoot)
        assertEquals(3, state.cycleIntervals.size)
        assertEquals(0, state.cycleIndex)
        assertEquals(IntervallicFunction.ROOT, state.targetInterval)
        assertTrue("Initially no interval is dimmed", state.completedIntervalIndices.isEmpty())

        // Simulate detecting correct pitch for ROOT (C -> PitchClass.C)
        vm.handlePitchDetected(
            PitchEvent(
                frequency = 261.63f,
                midiNote = 60,
                pitchClass = PitchClass.C,
                confidence = 0.95f,
                rmsDb = -12f
            )
        )

        state = vm.sessionState.value as ExerciseSessionState.Active
        assertEquals(1, state.cycleIndex)
        assertEquals(IntervallicFunction.MAJOR_THIRD, state.targetInterval)
        assertTrue("First interval is now dimmed", state.completedIntervalIndices.contains(0))
        assertFalse("Second interval is not dimmed", state.completedIntervalIndices.contains(1))

        // Simulate detecting correct pitch for MAJOR_THIRD (E -> PitchClass.E)
        vm.handlePitchDetected(
            PitchEvent(
                frequency = 329.63f,
                midiNote = 64,
                pitchClass = PitchClass.E,
                confidence = 0.95f,
                rmsDb = -12f
            )
        )

        state = vm.sessionState.value as ExerciseSessionState.Active
        assertEquals(2, state.cycleIndex)
        assertEquals(IntervallicFunction.PERFECT_FIFTH, state.targetInterval)
        assertTrue("First and second intervals are dimmed", state.completedIntervalIndices.contains(0) && state.completedIntervalIndices.contains(1))
        assertFalse("Third interval is not dimmed", state.completedIntervalIndices.contains(2))
    }
}

