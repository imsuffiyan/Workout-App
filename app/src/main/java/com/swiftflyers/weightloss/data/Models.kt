package com.swiftflyers.weightloss.data

data class Workout(
    val id: String,
    val name: String,
    val description: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val level: String,
    val equipmentRequired: List<String>,
    val exercises: List<WorkoutExercise>
)

data class WorkoutExercise(
    val name: String,
    val durationSeconds: Int,
    val intensity: String
)

data class WorkoutCategory(
    val id: String,
    val title: String,
    val workouts: List<Workout>
)

data class ProgramDay(
    val id: String,
    val title: String,
    val focus: String,
    val exercises: List<String>
)
