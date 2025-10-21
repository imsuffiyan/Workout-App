package com.fitness.workout.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val durationSec: Int,
    val exerciseCount: Int = 0,
    val category: String = ""
)
