package com.fitness.workout.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.workout.prefs.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: PrefsManager
) : ViewModel() {
    val profile: StateFlow<com.fitness.workout.prefs.UserProfile> = prefs.profile
        .stateIn(viewModelScope, SharingStarted.Lazily, com.fitness.workout.prefs.UserProfile())

    fun saveProfile(name: String?, age: Int?, target: Float?, notifications: Boolean) {
        viewModelScope.launch {
            prefs.saveProfile(name, age, target, notifications)
        }
    }

    // Save extended fields
    fun saveExtended(purpose: String? = null, units: String? = null, height: String? = null, currentWeight: Float? = null) {
        viewModelScope.launch {
            prefs.saveProfileExtended(purpose, units, height, currentWeight)
        }
    }

    fun saveTargetWeight(target: Float?) {
        viewModelScope.launch {
            prefs.saveTargetWeight(target)
        }
    }

    // Save avatar URI string
    fun saveAvatarUri(uri: String?) {
        viewModelScope.launch {
            prefs.saveAvatarUri(uri)
        }
    }

    // Increment water total by `add` ml atomically
    fun incrementWater(add: Int) {
        viewModelScope.launch {
            prefs.incrementWaterMl(add)
        }
    }
}
