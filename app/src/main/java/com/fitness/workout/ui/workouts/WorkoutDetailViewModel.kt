package com.fitness.workout.ui.workouts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitness.workout.data.model.Exercise
import com.fitness.workout.data.model.Workout
import com.fitness.workout.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailViewModel @Inject constructor(
    private val repo: WorkoutRepository
) : ViewModel() {
    private val _workout = MutableLiveData<Workout?>()
    val workout: LiveData<Workout?> = _workout

    private val _exercises = MutableLiveData<List<Exercise>>(emptyList())
    val exercises: LiveData<List<Exercise>> = _exercises

    fun loadWorkout(id: Int) {
        viewModelScope.launch {
            val w = repo.getWorkoutById(id)
            _workout.postValue(w)
        }
    }

    fun loadExercises(workoutId: Int) {
        viewModelScope.launch {
            val list = repo.getExercisesForWorkout(workoutId)
            _exercises.postValue(list)
        }
    }

    fun logWorkout(durationSec: Int) {
        viewModelScope.launch {
            val w = _workout.value ?: return@launch
            val calories = (durationSec * 6) // naive cal calc
            repo.logWorkout(w.id, durationSec, calories)
        }
    }
}
