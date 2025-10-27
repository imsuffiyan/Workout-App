package com.fitness.workout.domain.usecase.profile

import com.fitness.workout.domain.repository.ProfileRepository

class UpdateAge(private val repo: ProfileRepository) {
    suspend operator fun invoke(age: Int) = repo.updateAge(age)
}