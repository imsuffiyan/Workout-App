package com.fitness.workout.domain.usecase

import com.fitness.workout.domain.repository.WorkoutRepository

class LogWorkout(private val repository: WorkoutRepository) {
    suspend operator fun invoke(workoutId: Int, durationSec: Int, calories: Int) =
        repository.logWorkout(workoutId, durationSec, calories)
}