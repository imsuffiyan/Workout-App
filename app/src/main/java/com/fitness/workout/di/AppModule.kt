package com.fitness.workout.di

import android.content.Context
import androidx.room.Room
import com.fitness.workout.data.db.AppDatabase
import com.fitness.workout.data.db.WorkoutDao
import com.fitness.workout.data.db.WorkoutLogDao
import com.fitness.workout.data.repository.WorkoutRepository
import com.fitness.workout.prefs.PrefsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "workout_db").build()
    }

    @Provides
    fun provideWorkoutDao(db: AppDatabase): WorkoutDao = db.workoutDao()

    @Provides
    fun provideWorkoutLogDao(db: AppDatabase): WorkoutLogDao = db.workoutLogDao()

    @Provides
    @Singleton
    fun provideRepository(workoutDao: WorkoutDao, logDao: WorkoutLogDao): WorkoutRepository {
        return WorkoutRepository(workoutDao, logDao)
    }

    @Provides
    @Singleton
    fun provideAppScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun providePrefsManager(@ApplicationContext context: Context): PrefsManager = PrefsManager(context)
}
