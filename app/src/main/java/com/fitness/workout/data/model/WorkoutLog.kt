package com.fitness.workout.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutId: Int,
    val timestamp: Long,
    val durationSec: Int,
    val caloriesBurned: Int
)

