// ProfileUseCaseModule: provides profile use-cases for DI.
// Includes IncrementWater provider.
package com.fitness.workout.di

import com.fitness.workout.domain.repository.ProfileRepository
import com.fitness.workout.domain.usecase.profile.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileUseCaseModule {

    @Provides @Singleton fun provideGetProfile(repo: ProfileRepository) = GetProfile(repo)
    @Provides @Singleton fun provideUpdateUnits(repo: ProfileRepository) = UpdateUnits(repo)
    @Provides @Singleton fun provideUpdatePurpose(repo: ProfileRepository) = UpdatePurpose(repo)
    @Provides @Singleton fun provideUpdateAge(repo: ProfileRepository) = UpdateAge(repo)
    @Provides @Singleton fun provideUpdateHeight(repo: ProfileRepository) = UpdateHeight(repo)
    @Provides @Singleton fun provideUpdateCurrentWeight(repo: ProfileRepository) = UpdateCurrentWeight(repo)
    @Provides @Singleton fun provideUpdateTargetWeight(repo: ProfileRepository) = UpdateTargetWeight(repo)
    @Provides @Singleton fun provideIncrementWater(repo: ProfileRepository) = IncrementWater(repo)
}
