package com.fitness.workout.di

import com.fitness.workout.domain.repository.WorkoutRepository
import com.fitness.workout.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides @Singleton
    fun provideGetWorkoutDetail(repo: WorkoutRepository) = GetWorkoutDetail(repo)

    @Provides @Singleton
    fun provideGetWorkouts(repo: WorkoutRepository) = GetWorkouts(repo)

    @Provides @Singleton
    fun provideGetProgramDay(repo: WorkoutRepository) = GetProgramDay(repo)

    @Provides @Singleton
    fun provideLogWorkout(repo: WorkoutRepository) = LogWorkout(repo)

    @Provides @Singleton
    fun provideGetLogs(repo: WorkoutRepository) = GetLogs(repo)

    @Provides @Singleton
    fun provideGetProgramLength(repo: WorkoutRepository) = GetProgramLength(repo)
}