package com.fitness.workout.domain.usecase.profile

import com.fitness.workout.domain.repository.ProfileRepository

class UpdateTargetWeight(private val repo: ProfileRepository) {
    suspend operator fun invoke(value: Float) = repo.updateTargetWeight(value)
}