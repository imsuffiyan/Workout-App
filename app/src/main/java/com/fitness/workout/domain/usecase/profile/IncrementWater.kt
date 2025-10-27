// IncrementWater use-case: increase water total.
// Calls repository to persist addition.
package com.fitness.workout.domain.usecase.profile

import com.fitness.workout.domain.repository.ProfileRepository

class IncrementWater(private val repo: ProfileRepository) {
    suspend operator fun invoke(add: Int) = repo.incrementWater(add)
}
