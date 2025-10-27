package com.fitness.workout.domain.usecase.profile

import com.fitness.workout.domain.repository.ProfileRepository

class UpdatePurpose(private val repo: ProfileRepository) {
    suspend operator fun invoke(purpose: String) = repo.updatePurpose(purpose)
}