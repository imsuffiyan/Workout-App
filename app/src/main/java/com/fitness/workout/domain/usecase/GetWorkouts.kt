package com.fitness.workout.domain.usecase

import com.fitness.workout.data.model.Workout
import com.fitness.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class GetWorkouts(private val repository: WorkoutRepository) {
    operator fun invoke(): Flow<List<Workout>> = repository.getWorkouts()
}