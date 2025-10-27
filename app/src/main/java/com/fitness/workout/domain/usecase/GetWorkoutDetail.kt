package com.fitness.workout.domain.usecase

import com.fitness.workout.data.model.WorkoutDetail
import com.fitness.workout.domain.repository.WorkoutRepository

class GetWorkoutDetail(private val repository: WorkoutRepository) {
    suspend operator fun invoke(id: Int): WorkoutDetail? = repository.getWorkoutDetail(id)
}