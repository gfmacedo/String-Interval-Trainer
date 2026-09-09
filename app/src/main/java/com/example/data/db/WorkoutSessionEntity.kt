package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val moduleName: String,
    val score: Int,
    val totalNotes: Int,
    val accuracyPercent: Float,
    val streakMax: Int,
    val avgLatencyMs: Long,
    val details: String = ""
)
