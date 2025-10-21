package com.fitness.workout.data.model

data class ProgramDayPlan(
    val day: Int,
    val title: String,
    val focus: String,
    val exercises: List<Exercise>
) {
    val totalDurationSec: Int get() = exercises.sumOf { it.durationSec }
}
