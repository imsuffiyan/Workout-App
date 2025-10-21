package com.fitness.workout.ui.program

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.workout.data.model.ProgramDayPlan
import com.fitness.workout.data.repository.WorkoutRepository
import com.fitness.workout.prefs.PrefsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramViewModel @Inject constructor(
    private val prefs: PrefsManager,
    private val repo: WorkoutRepository
) : ViewModel() {

    private val initialState = ProgramUiState(
        day = 1,
        rawDay = 1,
        totalDays = repo.programLength,
        plan = repo.getProgramDay(1),
        isFinished = false
    )

    val uiState: StateFlow<ProgramUiState> = prefs.profile
        .map { profile ->
            val total = repo.programLength
            val rawDay = (profile.programDay ?: 1).coerceAtLeast(1)
            val finished = rawDay > total
            val dayToShow = rawDay.coerceAtMost(total)
            val plan = if (finished) null else repo.getProgramDay(dayToShow)
            ProgramUiState(
                day = dayToShow,
                rawDay = rawDay,
                totalDays = total,
                plan = plan,
                isFinished = finished
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialState)

    fun completeCurrentDay() {
        viewModelScope.launch {
            val state = uiState.value
            if (state.isFinished) return@launch
            val nextDay = if (state.day >= state.totalDays) state.totalDays + 1 else state.rawDay + 1
            prefs.saveProgramDay(nextDay)
        }
    }

    fun resetProgram() {
        viewModelScope.launch { prefs.saveProgramDay(1) }
    }
}

data class ProgramUiState(
    val day: Int,
    val rawDay: Int,
    val totalDays: Int,
    val plan: ProgramDayPlan?,
    val isFinished: Boolean
)
