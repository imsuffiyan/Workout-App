package com.fitness.workout.domain.usecase

import com.fitness.workout.domain.repository.WorkoutRepository

class GetProgramLength(private val repository: WorkoutRepository) {
    operator fun invoke(): Int = repository.getProgramLength()
}