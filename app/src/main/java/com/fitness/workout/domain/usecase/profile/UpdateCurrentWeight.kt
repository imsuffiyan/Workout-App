package com.fitness.workout.domain.usecase.profile

import com.fitness.workout.domain.repository.ProfileRepository

class UpdateCurrentWeight(private val repo: ProfileRepository) {
    suspend operator fun invoke(value: Float) = repo.updateCurrentWeight(value)
}