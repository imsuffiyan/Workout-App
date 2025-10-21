package com.fitness.workout.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fitness.workout.data.model.Workout
import com.fitness.workout.data.model.WorkoutLog

@Database(entities = [Workout::class, WorkoutLog::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutLogDao(): WorkoutLogDao
}

