package com.fitness.workout.data.repository

import com.fitness.workout.data.db.WorkoutDao
import com.fitness.workout.data.db.WorkoutLogDao
import com.fitness.workout.data.model.Workout
import com.fitness.workout.data.model.WorkoutDetail
import com.fitness.workout.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutLogDao: WorkoutLogDao
) {

    fun getWorkouts(): Flow<List<Workout>> = workoutDao.getAll().map { list ->
        if (list.isEmpty()) WorkoutSeedData.sampleWorkouts() else list
    }

    suspend fun seedIfEmpty() {
        val current = workoutDao.getAll()
        val first = current.first()
        if (first.isEmpty()) {
            workoutDao.insertAll(WorkoutSeedData.sampleWorkouts())
        } else {
            val updated = first.map { w ->
                if (w.category.isBlank()) {
                    val inferred = WorkoutSeedData.detectCategory(w.title)
                    if (inferred.isNotBlank()) w.copy(category = inferred) else w
                } else w
            }
            if (updated != first) {
                workoutDao.insertAll(updated)
            }
        }
    }

    suspend fun logWorkout(workoutId: Int, durationSec: Int, calories: Int) {
        val log = WorkoutLog(workoutId = workoutId, timestamp = System.currentTimeMillis(), durationSec = durationSec, caloriesBurned = calories)
        workoutLogDao.insert(log)
    }

    fun getLogs(): Flow<List<WorkoutLog>> = workoutLogDao.getAll()

    suspend fun getWorkoutDetail(workoutId: Int): WorkoutDetail? {
        val workout = workoutDao.getById(workoutId) ?: return null
        val count = if (workout.exerciseCount > 0) workout.exerciseCount else 6
        val exercises = WorkoutSeedData.generateExercisesForWorkout(workout.id, workout.title, count, workout.category)
        return WorkoutDetail(workout, exercises)
    }
}
