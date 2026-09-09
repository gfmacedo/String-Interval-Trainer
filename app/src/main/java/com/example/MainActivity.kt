package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ActiveWorkoutScreen
import com.example.ui.screens.AudioCalibrationScreen
import com.example.ui.screens.ChangesTrainerSetupScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.IntervalTrainerSetupScreen
import com.example.ui.screens.NoteTrainerSetupScreen
import com.example.ui.screens.ProgressionBuilderScreen
import com.example.ui.screens.ScaleTrainerSetupScreen
import com.example.ui.screens.WorkoutHistoryScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TrainerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BinoStringTrainerApp()
            }
        }
    }
}

@Composable
fun BinoStringTrainerApp(
    viewModel: TrainerViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    // Handle Back button intelligently
    BackHandler(enabled = currentScreen != AppScreen.DASHBOARD) {
        if (currentScreen == AppScreen.WORKOUT_HUD) {
            val state = viewModel.sessionState.value
            if (state is com.example.data.model.ExerciseSessionState.Active && (state.score > 0 || state.promptIndex > 0)) {
                viewModel.finishWorkout()
            } else {
                viewModel.exitWorkoutToDashboard()
            }
        } else {
            viewModel.navigateTo(AppScreen.DASHBOARD)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        when (currentScreen) {
            AppScreen.DASHBOARD -> {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateTo = { screen -> viewModel.navigateTo(screen) },
                    modifier = screenModifier
                )
            }
            AppScreen.NOTE_TRAINER_SETUP -> {
                NoteTrainerSetupScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    modifier = screenModifier
                )
            }
            AppScreen.INTERVAL_TRAINER_SETUP -> {
                IntervalTrainerSetupScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    modifier = screenModifier
                )
            }
            AppScreen.CHANGES_TRAINER_SETUP -> {
                ChangesTrainerSetupScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    modifier = screenModifier
                )
            }
            AppScreen.SCALE_TRAINER_SETUP -> {
                ScaleTrainerSetupScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    modifier = screenModifier
                )
            }
            AppScreen.WORKOUT_HUD -> {
                ActiveWorkoutScreen(
                    viewModel = viewModel,
                    onExit = { viewModel.exitWorkoutToDashboard() },
                    modifier = screenModifier
                )
            }
            AppScreen.AUDIO_CALIBRATION -> {
                AudioCalibrationScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    modifier = screenModifier
                )
            }
            AppScreen.PROGRESSION_BUILDER -> {
                ProgressionBuilderScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.CHANGES_TRAINER_SETUP) },
                    modifier = screenModifier
                )
            }
            AppScreen.WORKOUT_HISTORY -> {
                WorkoutHistoryScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    modifier = screenModifier
                )
            }
        }
    }
}
