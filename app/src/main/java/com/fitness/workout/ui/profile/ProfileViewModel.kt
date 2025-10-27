// Profile ViewModel: exposes profile flows and updates.
// Handles weight and water updates via use-cases.
package com.fitness.workout.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.workout.domain.usecase.profile.*
import com.fitness.workout.prefs.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getProfile: GetProfile,
    private val updatePurpose: UpdatePurpose,
    private val updateUnits: UpdateUnits,
    private val updateAge: UpdateAge,
    private val updateHeight: UpdateHeight,
    private val updateCurrentWeight: UpdateCurrentWeight,
    private val updateTargetWeight: UpdateTargetWeight,
    private val incrementWaterUseCase: IncrementWater
) : ViewModel() {

    val profile = getProfile().stateIn(viewModelScope, SharingStarted.Lazily, UserProfile())

    fun setPurpose(value: String) = viewModelScope.launch { updatePurpose(value) }
    fun setUnits(value: String) = viewModelScope.launch { updateUnits(value) }
    fun setAge(value: Int) = viewModelScope.launch { updateAge(value) }
    fun setHeight(value: String) = viewModelScope.launch { updateHeight(value) }
    fun setCurrentWeight(value: Float) = viewModelScope.launch { updateCurrentWeight(value) }
    fun setTargetWeight(value: Float) = viewModelScope.launch { updateTargetWeight(value) }
    fun incrementWater(add: Int) = viewModelScope.launch { incrementWaterUseCase(add) }
}