package com.fitness.workout.domain.usecase.profile

import com.fitness.workout.domain.repository.ProfileRepository

class UpdateUnits(private val repo: ProfileRepository) {
    suspend operator fun invoke(units: String) = repo.updateUnits(units)
}