package com.fitness.workout.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.workout.data.model.WorkoutLog
import com.fitness.workout.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    repo: WorkoutRepository
) : ViewModel() {
    val logs: StateFlow<List<WorkoutLog>> = repo.getLogs().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

