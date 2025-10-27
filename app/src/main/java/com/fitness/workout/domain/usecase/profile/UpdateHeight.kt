package com.fitness.workout.domain.usecase.profile

import com.fitness.workout.domain.repository.ProfileRepository

class UpdateHeight(private val repo: ProfileRepository) {
    suspend operator fun invoke(height: String) = repo.updateHeight(height)
}