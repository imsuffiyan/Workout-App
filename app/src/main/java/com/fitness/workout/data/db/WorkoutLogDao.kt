package com.fitness.workout.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.fitness.workout.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutLogDao {
    @Insert
    suspend fun insert(log: WorkoutLog)

    @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC")
    fun getAll(): Flow<List<WorkoutLog>>
}

