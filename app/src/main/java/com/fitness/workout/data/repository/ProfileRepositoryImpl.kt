// ProfileRepositoryImpl: DataStore-backed repository.
// Delegates profile persistence to PrefsManager.
package com.fitness.workout.data.repository

import com.fitness.workout.domain.repository.ProfileRepository
import com.fitness.workout.prefs.PrefsManager
import com.fitness.workout.prefs.UserProfile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val prefsManager: PrefsManager
) : ProfileRepository {

    override fun observeProfile(): Flow<UserProfile> = prefsManager.profile

    override suspend fun saveProfile(profile: UserProfile) {
        prefsManager.saveProfile(profile.name, profile.age, profile.targetWeight, profile.notificationsEnabled)
    }

    override suspend fun updateUnits(units: String) {
        prefsManager.saveProfileExtended(units = units)
    }

    override suspend fun updateAge(age: Int) {
        prefsManager.saveAge(age)
    }

    override suspend fun updateHeight(height: String) {
        prefsManager.saveProfileExtended(height = height)
    }

    override suspend fun updateCurrentWeight(current: Float) {
        prefsManager.saveProfileExtended(current = current)
    }

    override suspend fun updateTargetWeight(target: Float) {
        prefsManager.saveTargetWeight(target)
    }

    override suspend fun updatePurpose(purpose: String) {
        prefsManager.saveProfileExtended(purpose = purpose)
    }

    override suspend fun incrementWater(add: Int) {
        prefsManager.incrementWaterMl(add)
    }
}
