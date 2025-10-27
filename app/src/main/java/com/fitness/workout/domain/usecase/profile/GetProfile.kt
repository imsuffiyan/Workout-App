package com.fitness.workout.domain.usecase.profile

import com.fitness.workout.domain.repository.ProfileRepository
import com.fitness.workout.prefs.UserProfile
import kotlinx.coroutines.flow.Flow

class GetProfile(private val repo: ProfileRepository) {
    operator fun invoke(): Flow<UserProfile> = repo.observeProfile()
}