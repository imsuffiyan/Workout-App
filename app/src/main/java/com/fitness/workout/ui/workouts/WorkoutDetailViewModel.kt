package com.fitness.workout.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.workout.data.model.WorkoutDetail
import com.fitness.workout.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    private val repo: WorkoutRepository
) : ViewModel() {
    private val _detail = MutableStateFlow<WorkoutDetail?>(null)
    val detail: StateFlow<WorkoutDetail?> = _detail.asStateFlow()

    fun loadWorkout(id: Int) {
        viewModelScope.launch {
            val data = repo.getWorkoutDetail(id)
            _detail.value = data
        }
    }

    fun logWorkout(durationSec: Int) {
        viewModelScope.launch {
            val w = _detail.value?.workout ?: return@launch
            val calories = (durationSec * 6) // naive cal calc
            repo.logWorkout(w.id, durationSec, calories)
        }
    }
}
