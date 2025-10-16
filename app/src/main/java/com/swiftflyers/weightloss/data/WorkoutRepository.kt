package com.swiftflyers.weightloss.data

class WorkoutRepository {
    private val warmupExercises = listOf(
        WorkoutExercise("Jumping Jacks", 45, "Medium"),
        WorkoutExercise("Arm Circles", 30, "Low"),
        WorkoutExercise("High Knees", 45, "High"),
        WorkoutExercise("Bodyweight Squats", 45, "Medium")
    )

    private val hiitExercises = listOf(
        WorkoutExercise("Burpees", 40, "High"),
        WorkoutExercise("Mountain Climbers", 40, "High"),
        WorkoutExercise("Plank Jacks", 35, "Medium"),
        WorkoutExercise("Skaters", 40, "High"),
        WorkoutExercise("Glute Bridges", 45, "Medium")
    )

    private val yogaExercises = listOf(
        WorkoutExercise("Sun Salutations", 60, "Low"),
        WorkoutExercise("Warrior Flow", 75, "Medium"),
        WorkoutExercise("Glute Stretch", 45, "Low"),
        WorkoutExercise("Child's Pose", 60, "Low")
    )

    private val strengthExercises = listOf(
        WorkoutExercise("Push Ups", 45, "High"),
        WorkoutExercise("Lunges", 60, "Medium"),
        WorkoutExercise("Plank", 60, "High"),
        WorkoutExercise("Tricep Dips", 45, "Medium")
    )

    private val workouts = listOf(
        Workout(
            id = "ignite-hiit",
            name = "Ignite HIIT",
            description = "Fat-burning bodyweight interval session designed for small spaces.",
            durationMinutes = 24,
            caloriesBurned = 280,
            level = "Intermediate",
            equipmentRequired = emptyList(),
            exercises = warmupExercises + hiitExercises
        ),
        Workout(
            id = "gentle-yoga",
            name = "Gentle Yoga Stretch",
            description = "Mobility and flexibility flow to support recovery days.",
            durationMinutes = 30,
            caloriesBurned = 120,
            level = "Beginner",
            equipmentRequired = listOf("Yoga Mat"),
            exercises = warmupExercises.take(2) + yogaExercises
        ),
        Workout(
            id = "strength-burn",
            name = "Strength Burn",
            description = "Strength-focused sequence targeting full body toning.",
            durationMinutes = 32,
            caloriesBurned = 310,
            level = "Advanced",
            equipmentRequired = listOf("Resistance Bands"),
            exercises = warmupExercises + strengthExercises
        )
    )

    private val categories = listOf(
        WorkoutCategory(
            id = "quick-start",
            title = "Quick Start",
            workouts = listOf(workouts[0])
        ),
        WorkoutCategory(
            id = "flexibility",
            title = "Flexibility & Recovery",
            workouts = listOf(workouts[1])
        ),
        WorkoutCategory(
            id = "strength",
            title = "Strength & Tone",
            workouts = listOf(workouts[2])
        )
    )

    private val programDays = listOf(
        ProgramDay(
            id = "day1",
            title = "Day 1 · HIIT Blast",
            focus = "Cardio",
            exercises = listOf(
                "Warm up flow",
                "Ignite HIIT session",
                "Cool down stretch"
            )
        ),
        ProgramDay(
            id = "day2",
            title = "Day 2 · Active Recovery",
            focus = "Mobility",
            exercises = listOf(
                "Gentle yoga stretch",
                "Breathing practice",
                "Mindfulness journaling"
            )
        ),
        ProgramDay(
            id = "day3",
            title = "Day 3 · Strength Builder",
            focus = "Strength",
            exercises = listOf(
                "Warm up flow",
                "Strength burn workout",
                "Core finisher"
            )
        )
    )

    fun getCategories(): List<WorkoutCategory> = categories

    fun getProgram(): List<ProgramDay> = programDays

    fun getWorkoutById(id: String): Workout? = workouts.find { it.id == id }
}
