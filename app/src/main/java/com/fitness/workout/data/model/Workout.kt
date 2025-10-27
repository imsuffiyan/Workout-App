package com.fitness.workout.data.model


data class Workout(
    val id: Int,
    val title: String,
    val description: String,
    val durationSec: Int,
    val exerciseCount: Int = 0,
    val category: String = ""
)
