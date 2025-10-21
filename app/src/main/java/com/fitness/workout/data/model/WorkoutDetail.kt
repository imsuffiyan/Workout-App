package com.fitness.workout.data.model

/**
 * Aggregated representation of a workout alongside the generated exercises
 * used across detail/player screens. Bundling the two pieces keeps the UI
 * layer simple and avoids juggling multiple LiveData/Flow streams.
 */
data class WorkoutDetail(
    val workout: Workout,
    val exercises: List<Exercise>
) {
    val exerciseCount: Int get() = exercises.size
    val totalDurationSec: Int get() = workout.durationSec
}
