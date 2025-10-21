package com.fitness.workout.data.catalog

import com.fitness.workout.data.model.Exercise
import com.fitness.workout.data.model.ProgramDayPlan
import com.fitness.workout.data.model.Workout
import com.fitness.workout.data.model.WorkoutDetail

object WorkoutCatalog {

    data class WorkoutEntry(
        val workout: Workout,
        val exercises: List<Exercise>
    )

    private fun exercise(
        workoutId: Int,
        order: Int,
        title: String,
        durationSec: Int,
        calories: Int,
        description: String
    ): Exercise = Exercise(
        id = workoutId * 100 + order,
        title = title,
        durationSec = durationSec,
        calories = calories,
        description = description,
        thumbnailRes = 0,
        videoResName = ""
    )

    private fun entry(
        id: Int,
        title: String,
        category: String,
        description: String,
        exercises: List<Exercise>
    ): WorkoutEntry {
        val workout = Workout(
            id = id,
            title = title,
            description = description,
            durationSec = exercises.sumOf { it.durationSec },
            exerciseCount = exercises.size,
            category = category
        )
        return WorkoutEntry(workout, exercises)
    }

    val workouts: List<WorkoutEntry> = listOf(
        entry(
            id = 1,
            title = "Glute Strength Foundations",
            category = "Booty",
            description = "Low-impact glute session focusing on activation and foundational strength.",
            exercises = listOf(
                exercise(1, 1, "Mini-Band Glute Bridge", 120, 20, "Drive through the heels and pause for two seconds at the top."),
                exercise(1, 2, "Bodyweight Hip Thrust", 150, 28, "Rest shoulders on a bench, keep ribs down and squeeze glutes hard."),
                exercise(1, 3, "Single-Leg Romanian Deadlift", 150, 32, "Hinge from the hips with a flat back and reach long through the rear leg."),
                exercise(1, 4, "Quadruped Kickback", 120, 18, "Keep core braced while extending the heel to the ceiling."),
                exercise(1, 5, "Standing Abduction Pulses", 120, 16, "Hold onto a wall and pulse the working leg slightly behind the body.")
            )
        ),
        entry(
            id = 2,
            title = "Athletic Hip Power",
            category = "Booty",
            description = "Explosive lower-body work to build power through the posterior chain.",
            exercises = listOf(
                exercise(2, 1, "Barbell Hip Thrust", 180, 45, "Drive the barbell up quickly and lower on a controlled tempo."),
                exercise(2, 2, "Banded Lateral Walk", 120, 20, "Maintain athletic posture and tension on the band the entire time."),
                exercise(2, 3, "Curtsy Lunge to Knee Drive", 150, 35, "Drop the back knee diagonally then drive through the front heel."),
                exercise(2, 4, "Dumbbell Step-Up", 150, 38, "Step to a knee-height box and lock out the hip at the top."),
                exercise(2, 5, "Power Kettlebell Swing", 120, 40, "Snap the hips and keep shoulders packed as the bell floats to chest height.")
            )
        ),
        entry(
            id = 3,
            title = "Leg Day Pyramid",
            category = "Leg",
            description = "Strength pyramid alternating squat patterns with hinge work.",
            exercises = listOf(
                exercise(3, 1, "Goblet Squat Ladder", 180, 42, "Increase reps each round while keeping heels grounded."),
                exercise(3, 2, "Reverse Lunge Matrix", 180, 40, "Alternate legs and angles to challenge hip control."),
                exercise(3, 3, "Romanian Deadlift", 180, 38, "Slide hips back until hamstrings stretch, then stand tall."),
                exercise(3, 4, "Heel-Elevated Squat", 150, 34, "Shift weight slightly forward to feel a strong quad burn."),
                exercise(3, 5, "Walking Lunge Finisher", 150, 36, "Stay upright and keep strides long for a final conditioning push.")
            )
        ),
        entry(
            id = 4,
            title = "Speed and Stability Drills",
            category = "Leg",
            description = "Quick footwork and unilateral strength to stabilise knees and ankles.",
            exercises = listOf(
                exercise(4, 1, "Skater Bounds", 120, 32, "Bound laterally, landing softly with hips back and chest tall."),
                exercise(4, 2, "Single-Leg Box Squat", 150, 30, "Control the descent to the box and drive through the planted heel."),
                exercise(4, 3, "Lateral Step-Down", 150, 26, "Tap the heel to the floor lightly while keeping knee tracking mid-foot."),
                exercise(4, 4, "Sled March", 180, 48, "Drive knees high and maintain tension through the entire body."),
                exercise(4, 5, "Calf Raise Ladder", 120, 18, "Work through full range with a strong squeeze at the top.")
            )
        ),
        entry(
            id = 5,
            title = "Core Control Circuit",
            category = "Abs",
            description = "Focused core sequence pairing anti-rotation work with controlled flexion.",
            exercises = listOf(
                exercise(5, 1, "Dead Bug with Hold", 120, 12, "Press low back to the floor and reach long through opposite limbs."),
                exercise(5, 2, "Tall Kneeling Pallof Press", 120, 15, "Brace the trunk as you press the cable or band straight ahead."),
                exercise(5, 3, "Slow Mountain Climber", 150, 18, "Move one knee at a time keeping shoulders stacked over wrists."),
                exercise(5, 4, "Side Plank Reach Through", 150, 20, "Rotate under the torso, then open chest to the ceiling."),
                exercise(5, 5, "Hollow Body Rock", 120, 16, "Stay glued to the floor with ribs tucked down and legs long.")
            )
        ),
        entry(
            id = 6,
            title = "Bulletproof Shoulders",
            category = "Shoulder",
            description = "Strengthen rotator cuff and deltoids with mindful tempo work.",
            exercises = listOf(
                exercise(6, 1, "Seated Dumbbell Press", 180, 36, "Lower to 90 degrees, press overhead without arching the back."),
                exercise(6, 2, "Face Pull with Pause", 120, 18, "Pull to the forehead while squeezing shoulder blades together."),
                exercise(6, 3, "Half-Kneeling Landmine Press", 150, 28, "Stay tall through the torso and drive the bar up and slightly forward."),
                exercise(6, 4, "Incline Y Raise", 150, 22, "Lift thumbs to the sky and control the lowering phase."),
                exercise(6, 5, "Band External Rotation", 120, 12, "Keep elbow pinned to the ribs while rotating the hand outward.")
            )
        ),
        entry(
            id = 7,
            title = "Metabolic Gauntlet",
            category = "1500kcal",
            description = "High-output interval conditioning mixing bodyweight and kettlebell work.",
            exercises = listOf(
                exercise(7, 1, "Assault Bike Sprint", 90, 45, "Drive through the pedals fast while keeping shoulders relaxed."),
                exercise(7, 2, "Burpee Broad Jump", 120, 40, "Explode out of the burpee and land softly with knees tracking toes."),
                exercise(7, 3, "Kettlebell Thruster Ladder", 150, 50, "Clean the bells, squat deep and drive overhead in one motion."),
                exercise(7, 4, "Row Machine Push", 150, 48, "Hold a strong posture as you pull quickly through the drive."),
                exercise(7, 5, "Battle Rope Waves", 120, 42, "Alternate arms with aggressive snaps for maximum output."),
                exercise(7, 6, "Jump Lunge Flurry", 120, 38, "Switch legs mid-air while keeping torso upright and soft landings.")
            )
        ),
        entry(
            id = 8,
            title = "Low Impact Endurance",
            category = "Reduced-Fat",
            description = "Steady-state cardio pairing treadmill hiking with bodyweight mobility.",
            exercises = listOf(
                exercise(8, 1, "Incline Walk", 300, 80, "Use a brisk pace and keep hands off the rails."),
                exercise(8, 2, "Bodyweight Flow", 180, 24, "Move smoothly between lunges, squats and plank walkouts."),
                exercise(8, 3, "Resistance Band Circuit", 180, 30, "Cycle band rows, presses and pulls without resting."),
                exercise(8, 4, "Cooldown Mobility", 120, 12, "Breathe slowly through light stretches for hips and back.")
            )
        ),
        entry(
            id = 9,
            title = "Steady State Sculpt",
            category = "Carb-Lite",
            description = "Controlled strength work matched with breathing-focused cardio.",
            exercises = listOf(
                exercise(9, 1, "Tempo Goblet Squat", 150, 30, "Lower for three seconds, hold briefly, then stand powerfully."),
                exercise(9, 2, "Row Erg Moderate Pace", 240, 48, "Maintain a long spine and smooth rhythm."),
                exercise(9, 3, "Single-Leg Romanian Deadlift", 150, 28, "Focus on balance and squeezing glutes at the top."),
                exercise(9, 4, "Loaded Carry March", 180, 32, "Walk slow steps while keeping core braced and shoulders down.")
            )
        ),
        entry(
            id = 10,
            title = "Gentle Mobility Flow",
            category = "Veggie Bliss",
            description = "Ground-based flow to improve mobility and posture with calm breathing.",
            exercises = listOf(
                exercise(10, 1, "Cat Cow Breathing", 120, 8, "Match movement to breath to mobilise the spine."),
                exercise(10, 2, "Pilates Hundred", 150, 14, "Pump the arms while keeping core tight and breathing steadily."),
                exercise(10, 3, "Supported Lunge Stretch", 150, 12, "Sink into the front hip and reach arms overhead."),
                exercise(10, 4, "Thoracic Rotation Flow", 150, 10, "Rotate through upper back while keeping hips square."),
                exercise(10, 5, "Guided Breath Downshift", 120, 6, "Lie on the floor and take slow nasal breaths to recover.")
            )
        )
    )

    private val entryMap: Map<Int, WorkoutEntry> = workouts.associateBy { it.workout.id }

    private fun pick(day: Int, order: Int, workoutId: Int, index: Int): Exercise? {
        val base = entryMap[workoutId]?.exercises?.getOrNull(index) ?: return null
        return base.copy(id = day * 1000 + order)
    }

    private fun customExercise(day: Int, order: Int, title: String, duration: Int, description: String): Exercise =
        Exercise(
            id = day * 1000 + order,
            title = title,
            durationSec = duration,
            calories = 8,
            description = description,
            thumbnailRes = 0,
            videoResName = ""
        )

    val programDays: List<ProgramDayPlan> = listOf(
        ProgramDayPlan(
            day = 1,
            title = "Full Body Primer",
            focus = "Ease into the week with mobility and light strength work.",
            exercises = listOfNotNull(
                pick(1, 1, 10, 0),
                pick(1, 2, 10, 3),
                pick(1, 3, 5, 0),
                pick(1, 4, 3, 1),
                pick(1, 5, 8, 3)
            )
        ),
        ProgramDayPlan(
            day = 2,
            title = "Posterior Chain Power",
            focus = "Build strength through the hips with focused glute work.",
            exercises = listOfNotNull(
                pick(2, 1, 2, 0),
                pick(2, 2, 2, 2),
                pick(2, 3, 1, 2),
                pick(2, 4, 2, 4),
                pick(2, 5, 1, 4)
            )
        ),
        ProgramDayPlan(
            day = 3,
            title = "Lower Body Conditioning",
            focus = "Alternate leg strength with fast-paced intervals to spike the heart rate.",
            exercises = listOfNotNull(
                pick(3, 1, 3, 0),
                pick(3, 2, 3, 4),
                pick(3, 3, 7, 1),
                pick(3, 4, 7, 5),
                pick(3, 5, 4, 0)
            )
        ),
        ProgramDayPlan(
            day = 4,
            title = "Core and Mobility Reset",
            focus = "Stabilise the trunk then open up hips and thoracic spine.",
            exercises = listOfNotNull(
                pick(4, 1, 5, 0),
                pick(4, 2, 5, 3),
                pick(4, 3, 10, 1),
                pick(4, 4, 10, 2),
                pick(4, 5, 10, 4)
            )
        ),
        ProgramDayPlan(
            day = 5,
            title = "Upper Body Focus",
            focus = "Deltoid and rotator cuff work with quality reps and controlled tempo.",
            exercises = listOfNotNull(
                pick(5, 1, 6, 0),
                pick(5, 2, 6, 1),
                pick(5, 3, 6, 2),
                pick(5, 4, 6, 3),
                pick(5, 5, 6, 4)
            )
        ),
        ProgramDayPlan(
            day = 6,
            title = "Engine Builder",
            focus = "Steady conditioning to keep output high without heavy impact.",
            exercises = listOfNotNull(
                pick(6, 1, 8, 0),
                pick(6, 2, 8, 2),
                pick(6, 3, 9, 1),
                pick(6, 4, 9, 3)
            )
        ),
        ProgramDayPlan(
            day = 7,
            title = "Active Recovery",
            focus = "Drop intensity, breathe deeply and prepare for the next training block.",
            exercises = listOf(
                customExercise(7, 1, "Breath-Led Box Breathing", 180, "Inhale for four counts, hold for four, exhale for four and repeat."),
                customExercise(7, 2, "Foam Roll Sweep", 180, "Gently sweep along quads, hamstrings and lats to release tension."),
                customExercise(7, 3, "Supine Hip Opener", 150, "Use a band or towel to mobilise the hips without forcing range."),
                customExercise(7, 4, "Shoulder CAR Flow", 150, "Perform controlled articular rotations to maintain shoulder health."),
                customExercise(7, 5, "Five-Minute Journal", 300, "Reflect on wins and set intentions before closing the session.")
            )
        )
    )

    fun findById(id: Int): WorkoutDetail? =
        entryMap[id]?.let { entry -> WorkoutDetail(entry.workout, entry.exercises) }
}
