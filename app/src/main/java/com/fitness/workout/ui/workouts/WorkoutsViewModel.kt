package com.fitness.workout.ui.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.workout.data.model.Workout
import com.fitness.workout.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val repo: WorkoutRepository
) : ViewModel() {

    private val initialSamples: List<Workout> = emptyList()

    val workouts: StateFlow<List<Workout>> = repo.getWorkouts()
        .stateIn(viewModelScope, SharingStarted.Lazily, initialSamples)

    init {
        viewModelScope.launch {
            repo.seedIfEmpty()
        }
    }
}
