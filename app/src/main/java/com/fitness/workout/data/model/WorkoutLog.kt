package com.fitness.workout.data.model


data class WorkoutLog(
    val id: Long = 0,
    val workoutId: Int,
    val timestamp: Long,
    val durationSec: Int,
    val caloriesBurned: Int
)

