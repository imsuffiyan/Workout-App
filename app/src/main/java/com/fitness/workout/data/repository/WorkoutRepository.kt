package com.fitness.workout.data.repository

import com.fitness.workout.data.db.WorkoutDao
import com.fitness.workout.data.db.WorkoutLogDao
import com.fitness.workout.data.model.Exercise
import com.fitness.workout.data.model.Workout
import com.fitness.workout.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutLogDao: WorkoutLogDao
) {
    private val categoryMeta = WorkoutSeedData.categoryMeta

    fun getWorkouts(): Flow<List<Workout>> = workoutDao.getAll().map { list ->
        list.ifEmpty {
            val samples = mutableListOf<Workout>()
            var idCounter = 1
            for ((key, meta) in categoryMeta) {
                for (i in meta.titles.indices) {
                    val title = meta.titles[i]
                    val description = meta.descriptions.getOrElse(i) { "Effective workout." }
                    val duration = meta.durations.getOrElse(i) { 600 }
                    val exercises = meta.counts.getOrElse(i) { 8 }
                    samples.add(Workout(idCounter, title, description, duration, exercises, key))
                    idCounter++
                }
            }
            samples
        }
    }

    private fun detectCategoryFromTitle(title: String): String {
        val t = title.lowercase()
        return when {
            t.contains("glute") || t.contains("booty") || t.contains("hip thrust") -> "Booty"
            t.contains("leg") || t.contains("squat") || t.contains("lunge") || t.contains("calf") -> "Leg"
            t.contains("core") || t.contains("abs") || t.contains("plank") || t.contains("oblique") -> "Abs"
            t.contains("shoulder") || t.contains("deltoid") || t.contains("rotator") -> "Shoulder"
            t.contains("metabolic") || t.contains("1500") || t.contains("metabolic circuit") -> "1500kcal"
            t.contains("fat reduction") || t.contains("fat") || t.contains("reduced") -> "Reduced-Fat"
            t.contains("carb") -> "Carb-Lite"
            t.contains("veggie") || t.contains("veggie flow") -> "Veggie Bliss"
            else -> ""
        }
    }

    suspend fun getWorkoutById(id: Int) = workoutDao.getById(id)

    suspend fun seedIfEmpty() {
        val current = workoutDao.getAll()
        val first = current.first()
        if (first.isEmpty()) {
            val samples = mutableListOf<Workout>()
            var idCounter = 1
            for ((key, meta) in categoryMeta) {
                for (i in meta.titles.indices) {
                    val title = meta.titles[i]
                    val description = meta.descriptions.getOrElse(i) { "Effective workout." }
                    val duration = meta.durations.getOrElse(i) { 600 }
                    val exercises = meta.counts.getOrElse(i) { 8 }
                    samples.add(Workout(idCounter, title, description, duration, exercises, key))
                    idCounter++
                }
            }
            workoutDao.insertAll(samples)
        } else {
            val toUpdate = first.filter { it.category.isBlank() }
            if (toUpdate.isNotEmpty()) {
                val updated = first.map { w ->
                    if (w.category.isBlank()) {
                        val inferred = detectCategoryFromTitle(w.title)
                        if (inferred.isNotBlank()) w.copy(category = inferred) else w
                    } else w
                }
                workoutDao.insertAll(updated)
            }
        }
    }

    suspend fun logWorkout(workoutId: Int, durationSec: Int, calories: Int) {
        val log = WorkoutLog(workoutId = workoutId, timestamp = System.currentTimeMillis(), durationSec = durationSec, caloriesBurned = calories)
        workoutLogDao.insert(log)
    }

    fun getLogs(): Flow<List<WorkoutLog>> = workoutLogDao.getAll()


    suspend fun getExercisesForWorkout(workoutId: Int): List<Exercise> {
        val w = workoutDao.getById(workoutId) ?: return emptyList()
        val count = if (w.exerciseCount > 0) w.exerciseCount else 6
        return WorkoutSeedData.generateExercisesForWorkout(workoutId, w.title, count, w.category)
    }
}
