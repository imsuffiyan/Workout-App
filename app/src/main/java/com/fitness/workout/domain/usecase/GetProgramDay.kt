package com.fitness.workout.domain.usecase

import com.fitness.workout.data.model.ProgramDayPlan
import com.fitness.workout.domain.repository.WorkoutRepository

class GetProgramDay(private val repository: WorkoutRepository) {
    operator fun invoke(day: Int): ProgramDayPlan? = repository.getProgramDay(day)
}