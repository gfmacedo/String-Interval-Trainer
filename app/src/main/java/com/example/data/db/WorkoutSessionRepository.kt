package com.example.data.db

import kotlinx.coroutines.flow.Flow

class WorkoutSessionRepository(private val dao: WorkoutSessionDao) {
    val allSessions: Flow<List<WorkoutSessionEntity>> = dao.getAllSessions()
    val recentSessions: Flow<List<WorkoutSessionEntity>> = dao.getRecentSessions(5)

    suspend fun saveSession(session: WorkoutSessionEntity): Long {
        return dao.insertSession(session)
    }

    suspend fun deleteSession(id: Long) {
        dao.deleteSession(id)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
