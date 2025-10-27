package com.fitness.workout.domain.repository

import com.fitness.workout.data.model.ProgramDayPlan
import com.fitness.workout.data.model.Workout
import com.fitness.workout.data.model.WorkoutDetail
import com.fitness.workout.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getWorkouts(): Flow<List<Workout>>
    suspend fun getWorkoutDetail(id: Int): WorkoutDetail?
    fun getProgramDay(day: Int): ProgramDayPlan?
    suspend fun logWorkout(workoutId: Int, durationSec: Int, calories: Int)
    fun getLogs(): Flow<List<WorkoutLog>>
    fun getProgramLength(): Int
    fun refreshCatalog()
}