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
import kotlin.math.roundToInt

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
            val detail = _detail.value ?: return@launch
            val workout = detail.workout
            val actualDuration = durationSec.takeIf { it > 0 } ?: workout.durationSec
            val totalCalories = detail.exercises.sumOf { it.calories }
                .takeIf { it > 0 }
                ?: ((actualDuration / 60.0) * 8.0).roundToInt().coerceAtLeast(12)
            repo.logWorkout(workout.id, actualDuration, totalCalories)
        }
    }
}
