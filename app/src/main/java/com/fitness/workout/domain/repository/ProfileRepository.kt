// ProfileRepository: profile data operations.
// Observe and update profile values.
package com.fitness.workout.domain.repository

import com.fitness.workout.prefs.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<UserProfile>
    suspend fun saveProfile(profile: UserProfile)
    suspend fun updateUnits(units: String)
    suspend fun updatePurpose(purpose: String)
    suspend fun updateAge(age: Int)
    suspend fun updateHeight(height: String)
    suspend fun updateCurrentWeight(current: Float)
    suspend fun updateTargetWeight(target: Float)
    suspend fun incrementWater(add: Int)
}