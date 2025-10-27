package com.fitness.workout.data.repository

import com.fitness.workout.data.model.ProgramDayPlan
import com.fitness.workout.data.model.Workout
import com.fitness.workout.data.model.WorkoutDetail
import com.fitness.workout.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepositoryImpl @Inject constructor(
    private val delegate: WorkoutRepository
) : com.fitness.workout.domain.repository.WorkoutRepository {

    override fun getWorkouts(): Flow<List<Workout>> = delegate.getWorkouts()

    override suspend fun getWorkoutDetail(id: Int): WorkoutDetail? =
        delegate.getWorkoutDetail(id)

    override fun getProgramDay(day: Int): ProgramDayPlan? = delegate.getProgramDay(day)

    override suspend fun logWorkout(workoutId: Int, durationSec: Int, calories: Int) =
        delegate.logWorkout(workoutId, durationSec, calories)

    override fun getLogs(): Flow<List<WorkoutLog>> = delegate.getLogs()

    override fun getProgramLength(): Int = delegate.programLength

    override fun refreshCatalog() = delegate.refreshCatalog()
}