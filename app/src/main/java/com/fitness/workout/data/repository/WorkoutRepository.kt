package com.fitness.workout.data.repository

import com.fitness.workout.data.catalog.WorkoutCatalog
import com.fitness.workout.data.model.ProgramDayPlan
import com.fitness.workout.data.model.Workout
import com.fitness.workout.data.model.WorkoutDetail
import com.fitness.workout.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor() {

    private val workoutEntries = WorkoutCatalog.workouts
    private val workoutsFlow = MutableStateFlow(workoutEntries.map { it.workout })

    private val logsFlow = MutableStateFlow<List<WorkoutLog>>(emptyList())
    private val logIdCounter = AtomicLong(1L)

    val programLength: Int = WorkoutCatalog.programDays.size

    fun getWorkouts(): Flow<List<Workout>> = workoutsFlow.asStateFlow()

    suspend fun getWorkoutDetail(workoutId: Int): WorkoutDetail? =
        WorkoutCatalog.findById(workoutId)

    suspend fun logWorkout(workoutId: Int, durationSec: Int, calories: Int) {
        val log = WorkoutLog(
            id = logIdCounter.getAndIncrement(),
            workoutId = workoutId,
            timestamp = System.currentTimeMillis(),
            durationSec = durationSec,
            caloriesBurned = calories
        )
        logsFlow.update { current -> listOf(log) + current }
    }

    fun getLogs(): Flow<List<WorkoutLog>> = logsFlow.asStateFlow()

    fun getProgramDay(day: Int): ProgramDayPlan? =
        WorkoutCatalog.programDays.getOrNull(day - 1)

    fun refreshCatalog() {
        workoutsFlow.value = workoutEntries.map { it.workout }
    }
}
