package com.fitness.workout.domain.usecase

import com.fitness.workout.data.model.WorkoutLog
import com.fitness.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class GetLogs(private val repository: WorkoutRepository) {
    operator fun invoke(): Flow<List<WorkoutLog>> = repository.getLogs()
}