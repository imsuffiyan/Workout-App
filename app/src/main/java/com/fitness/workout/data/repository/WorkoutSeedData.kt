package com.fitness.workout.data.repository

import com.fitness.workout.data.model.Exercise
import com.fitness.workout.data.model.Workout
import kotlin.math.max

@Suppress("unused", "MemberVisibilityCanBePrivate")
object WorkoutSeedData {

    data class CategoryMeta(
        val titles: List<String>,
        val durations: List<Int>,
        val counts: List<Int>,
        val descriptions: List<String>
    )

    private data class ExerciseTemplate(
        val title: String,
        val durationSec: Int,
        val calories: Int,
        val description: String
    )

    val categoryMeta: Map<String, CategoryMeta> = mapOf(
        "Booty" to CategoryMeta(
            titles = listOf(
                "Glute Activation Warmup",
                "Hip Thrust Hypertrophy",
                "Banded Glute Blast",
                "Bridge Progression",
                "Single-Leg Strength",
                "Kickback & Pulse Mix",
                "Glute Power Plyos",
                "Weighted Glute Builder",
                "Glute Burnout Finisher",
                "Mobility & Release for Glutes"
            ),
            durations = listOf(300, 380, 350, 420, 250, 200, 150, 200, 300, 420),
            counts = listOf(6, 10, 8, 7, 9, 8, 7, 10, 6, 5),
            descriptions = listOf(
                "Band and bodyweight drills to wake up and activate the glute complex.",
                "Progressive hip thrust variations with ramped sets for hypertrophy.",
                "Banded pulses and higher-rep accessory work focused on glute medius.",
                "Bridge variations and tempo work to build posterior chain endurance.",
                "Unilateral strength work (split squats, step-ups) to correct imbalances.",
                "Controlled kickbacks and pulses to isolate glute max contraction.",
                "Plyometric hip-driven moves to develop posterior power and explosiveness.",
                "Heavier compound lifts paired with targeted accessory glute moves.",
                "Short, intense burnout sequence to finish the glute session.",
                "Mobility, soft-tissue and release to support recovery and range."
            )
        ),
        "Leg" to CategoryMeta(
            titles = listOf(
                "Squat Strength Builder",
                "Explosive Leg Power",
                "Lunge Ladder Progression",
                "Plyo Leg Circuit",
                "Hamstring & Glute Focus",
                "Quad Dominant Session",
                "Leg Endurance Circuit",
                "Calf & Ankle Stability",
                "Mixed Lower-Body Strength",
                "Speed-Strength Lower Body"
            ),
            durations = listOf(380, 200, 400, 350, 180, 240, 150, 360, 200, 250),
            counts = listOf(10, 8, 12, 9, 8, 9, 14, 6, 10, 9),
            descriptions = listOf(
                "Heavy squat work with tempo and pause variations for strength gains.",
                "Plyometric and Olympic-style drills to build leg power and rate of force.",
                "Ascending lunge sets to add unilateral volume and conditioning.",
                "High-intensity plyo circuit for power and metabolic stress.",
                "Hamstring-focused session with RDLs, curls and posterior-chain drills.",
                "Quad-targeting work (front squats, split squats) for muscular development.",
                "Long circuits aimed at muscular endurance and work capacity for legs.",
                "Calf raises, mobility and ankle stability for injury prevention and sprinting mechanics.",
                "Mixed compound lifts and accessory movements for a balanced lower body session.",
                "Speed-strength combos to improve sprint and jump performance."
            )
        ),
        "Abs" to CategoryMeta(
            titles = listOf(
                "Core Activation Flow",
                "Plank Progression Series",
                "Six-Pack Circuit",
                "Oblique Strength Focus",
                "Pilates Core Flow",
                "Anti-Rotation Stability",
                "Core & Cardio Intervals",
                "Deep Core Stability",
                "Dynamic Core Endurance",
                "Quick Ab Burner"
            ),
            durations = listOf(300, 220, 420, 360, 450, 220, 350, 200, 420, 240),
            counts = listOf(6, 6, 10, 8, 9, 7, 11, 6, 10, 5),
            descriptions = listOf(
                "Breath-driven core activation to establish neutral spine and connection.",
                "Plank progressions with instability and timed holds to build endurance.",
                "Classic abdominal circuit with leg raises, crunches and rotational work.",
                "Oblique-specific loading to improve trunk rotation control and aesthetics.",
                "Pilates-based flow emphasizing posture, breath and deep core recruitment.",
                "Pallof presses and anti-rotation holds to build functional stability.",
                "Core movements paired with short cardio bursts to raise metabolic load.",
                "Low-load, deep core activation to support spinal health and lifting mechanics.",
                "Endurance-focused core routine to hold tension under fatigue.",
                "Short, high-intensity ab cluster to quickly fatigue the rectus abdominis."
            )
        ),
        "Shoulder" to CategoryMeta(
            titles = listOf(
                "Shoulder Press Progression",
                "Rotator Cuff Prehab",
                "Deltoid Isolation Blast",
                "Stability & Overhead Strength",
                "Band Shoulder Activation",
                "Plyo Shoulder Power",
                "Unilateral Press & Stabilize",
                "Rear Delt Focus",
                "Mobility, Stability & Strength",
                "Light Shoulder Finisher"
            ),
            durations = listOf(200, 420, 150, 250, 420, 200, 180, 360, 300, 240),
            counts = listOf(8, 6, 10, 9, 6, 7, 9, 6, 5, 4),
            descriptions = listOf(
                "Progressive overhead pressing with accessory work for balanced strength.",
                "Rotator cuff and external-rotation routines to improve joint health.",
                "High-volume lateral and anterior deltoid work for shape and endurance.",
                "Stability-focused pressing to transfer strength into overhead positions.",
                "Band flows and activation for posture and scapular control.",
                "Explosive pushing and plyometric shoulder drills for power.",
                "Single-arm pressing with bracing to address imbalances.",
                "Rear-delt and upper-back emphasis to support posture and performance.",
                "Mobility-led session that still provides progressive loading.",
                "Light pump work to finish and flush the shoulder complex."
            )
        ),
        "1500kcal" to CategoryMeta(
            titles = listOf(
                "All-Out Metabolic Burn",
                "Tabata Ladder Blast",
                "Full-Body AMRAP Sprint",
                "Kettlebell & Cardio Crusher",
                "Row/Bike/Bodyweight Chipper",
                "Thruster + Burpee Metcon",
                "Explosive Lower-Body Intervals",
                "Upper-Lower Switch Circuit",
                "Core & Conditioning Surge",
                "Short Sharp Calorie Spike"
            ),
            durations = listOf(300, 150, 380, 200, 240, 250, 180, 350, 220, 360),
            counts = listOf(12, 10, 11, 9, 10, 9, 8, 9, 10, 7),
            descriptions = listOf(
                "Max-effort intervals combining compound lifts and cardio bursts to maximize calorie burn over an extended session.",
                "Structured Tabata ladder with escalating work periods to push anaerobic capacity and metabolic demand.",
                "AMRAP format mixing bodyweight and loaded movements for continuous high-output conditioning.",
                "Kettlebell complexes paired with short bike sprints to tax the posterior chain and cardiovascular system.",
                "Station-to-station chipper alternating rowing, cycling and bodyweight movements for sustained calorie output.",
                "Heavy thrusters integrated with burpee variations for repeated glycolytic spikes and full-body fatigue.",
                "High-power lower-body intervals using jumps, sleds or heavy carries to drive heart rate and strength endurance.",
                "Alternating upper and lower clusters to keep intensity high while balancing systemic fatigue.",
                "Core-driven conditioning session that mixes anti-extension and anti-rotation work with short cardio pushes.",
                "Compressed, very high-intensity blocks to rapidly elevate metabolic rate in a short time."
            )
        ),
        "Reduced-Fat" to CategoryMeta(
            titles = listOf(
                "Long Incline Walk",
                "Mixed Cardio Circuit",
                "Steady-State Bike Endurance",
                "Bodyweight Conditioning Flow",
                "Walk/Jog Interval Progression",
                "Band & Strength Endurance",
                "Rowing Endurance Session",
                "Low-Impact Plyo Modifications",
                "Mobility + Core Maintenance",
                "Daily Movement Short Walks"
            ),
            durations = listOf(180, 250, 300, 150, 380, 200, 180, 350, 220, 300),
            counts = listOf(14, 12, 12, 11, 10, 9, 8, 7, 6, 5),
            descriptions = listOf(
                "Extended incline treadmill or outdoor walk to promote steady calorie expenditure with minimal joint stress.",
                "Circuit alternating low-impact cardio and moderate resistance strength to raise daily energy output sustainably.",
                "Long-duration cycling session at conversational intensity to burn fat while preserving recovery.",
                "Bodyweight-focused circuit emphasizing volume, movement quality and low glycolytic demand for consistent fat loss.",
                "Alternating brisk walking and light jogging intervals to increase weekly energy expenditure without overreaching.",
                "Resistance-band circuits and higher-rep strength clusters to build lean mass and increase daily metabolic rate.",
                "Longer rowing at a steady pace to combine upper and lower body work with low impact and steady caloric burn.",
                "Modified plyometrics and low-impact conditioning to elevate heart rate safely for longer durations.",
                "Mobility and sub-maximal core work to support recovery and movement quality while contributing to activity totals.",
                "Short, frequent walks designed to increase NEAT and accumulate sustainable calorie deficit over the day."
            )
        ),
        "Carb-Lite" to CategoryMeta(
            titles = listOf(
                "Low-Glycemic Conditioning",
                "Steady Row Session",
                "Tempo Strength Circuit",
                "Moderate Interval Bike",
                "Kettlebell Flow & Stability",
                "Low-Volume Strength Complex",
                "Bodyweight Metcon (Low-Gly)",
                "Farmers Carry & Core Circuit",
                "Step-Up & Row Hybrid",
                "Breathwork and Recovery Core"
            ),
            durations = listOf(200, 240, 250, 180, 150, 350, 200, 220, 420, 360),
            counts = listOf(9, 9, 8, 8, 7, 7, 6, 6, 5, 5),
            descriptions = listOf(
                "Conditioning session designed to avoid high-glycolytic spikes: longer efforts at moderate intensity and stable pacing.",
                "Steady-state rowing with controlled power outputs to sustain energy without heavy carbohydrate reliance.",
                "Slow-tempo strength sets focused on time under tension rather than maximal speed or power.",
                "Short, moderate bike intervals that elevate heart rate without inducing deep glycogen depletion.",
                "Kettlebell sequences emphasizing continuous movement and metabolic efficiency for low-carb fueling.",
                "Compound strength complexes with fewer explosive reps and more controlled cadence to preserve energy balance.",
                "Bodyweight metcon programmed with steady efforts and longer rests to align with lower-carb fueling strategies.",
                "Loaded carries coupled with core stability work to build functional strength without glycolytic overload.",
                "Combined step-up and rowing stations to blend strength and steady cardio while keeping intensity sustainable.",
                "Guided breathwork, mobility and light core work to aid recovery and maintain metabolic flexibility."
            )
        ),
        "Veggie Bliss" to CategoryMeta(
            titles = listOf(
                "Gentle Mobility & Breath",
                "Pilates-Inspired Core Flow",
                "Low-Impact Strength Sequence",
                "Balance & Stability Flow",
                "Band-Assisted Total Body",
                "Light Cardio & Mobility Mix",
                "Upper-Lower Gentle Tone",
                "Quick Posture Reset",
                "Joint-Friendly Strength",
                "Recovery Stretch & Breath"
            ),
            durations = listOf(420, 360, 220, 300, 200, 220, 360, 300, 240, 180),
            counts = listOf(6, 7, 6, 5, 8, 7, 6, 5, 4, 3),
            descriptions = listOf(
                "Low-impact conditioning flows focused on mobility, breath and sustainable movement.",
                "Gentle strength and mobility sequence accessible for most fitness levels.",
                "Longer flow blending balance, mobility and light resistance for full-body tone.",
                "Quick restorative movement to energize the day without taxing glycogen stores.",
                "Moderate intervals with bodyweight and band work for sustainable conditioning.",
                "Accessible mixed circuits with mobility emphasis and breath cues.",
                "Blended upper-lower flow to build tone while preserving joint health.",
                "Short conditioning clusters for busy schedules focusing on movement quality.",
                "Low-impact strength and posture-focused work.",
                "Short mobility cool-down and breath restoration."
            )
        )
    )

    private val exerciseTemplates: Map<String, List<ExerciseTemplate>> = mapOf(
        "Booty" to listOf(
            ExerciseTemplate("Banded Glute Bridge", 45, 8, "Feet hip-width, band above knees, drive through heels to squeeze glutes at top."),
            ExerciseTemplate("Barbell Hip Thrust", 60, 12, "Heavy hip thrusts focusing on full hip extension and strong squeeze at top."),
            ExerciseTemplate("Bulgarian Split Squat", 50, 10, "Elevated-rear foot split squat to load quads and glutes unilaterally."),
            ExerciseTemplate("Cable Kickback", 40, 6, "Controlled rear kick focusing on glute max contraction."),
            ExerciseTemplate("Single-Leg Romanian Deadlift", 50, 9, "Hip-hinge single-leg RDL for posterior chain and balance."),
            ExerciseTemplate("Clamshells with Band", 30, 4, "Side-lying clams to target glute medius and hip stability."),
            ExerciseTemplate("Walking Lunges", 45, 8, "Step forwards with controlled knee drive, keep torso tall."),
            ExerciseTemplate("Curtsy Lunge", 40, 7, "Diagonal lunge pattern to emphasize gluteus medius and outer hips."),
            ExerciseTemplate("Kettlebell Swing", 45, 9, "Powerful hip hinge movement emphasizing explosive hip drive."),
            ExerciseTemplate("Pulse Bridge", 30, 5, "Short pulses at top of bridge to increase time under tension.")
        ),
        "Leg" to listOf(
            ExerciseTemplate("Back Squat", 90, 18, "Barbell back squat with controlled descent and drive to full depth."),
            ExerciseTemplate("Front Squat", 80, 16, "Front-loaded squat emphasizing quad engagement and upright torso."),
            ExerciseTemplate("Romanian Deadlift", 70, 12, "Hip hinge with hamstring loading, maintain neutral spine."),
            ExerciseTemplate("Walking Lunges", 50, 9, "Long stride walking lunges for unilateral strength and hip drive."),
            ExerciseTemplate("Box Jump", 30, 6, "Explosive vertical jump to a stable box with soft landing."),
            ExerciseTemplate("Goblet Squat", 45, 8, "Kettlebell front squat focusing on depth and core bracing."),
            ExerciseTemplate("Hamstring Curl (Machine)", 40, 6, "Isolated hamstring loading, slow eccentric control."),
            ExerciseTemplate("Step-Up (Weighted)", 50, 10, "Weighted step-ups to a bench to develop single-leg strength."),
            ExerciseTemplate("Calf Raise (Single-leg)", 30, 4, "Full range calf contraction with slow eccentric."),
            ExerciseTemplate("Plyo Lateral Bound", 30, 7, "Lateral explosive bounds to improve force transfer and stability.")
        ),
        "Abs" to listOf(
            ExerciseTemplate("Dead Bug", 30, 3, "Controlled alternating arm/leg extension to reinforce neutral spine."),
            ExerciseTemplate("Hollow Hold", 40, 4, "Maintain hollow body position with ribs pulled down and pelvis slightly tucked."),
            ExerciseTemplate("Plank Shoulder Taps", 45, 5, "High plank with alternating taps, maintain hip stability."),
            ExerciseTemplate("Russian Twist", 30, 4, "Torso rotation with or without weight to target obliques."),
            ExerciseTemplate("Leg Raises", 40, 5, "Control the descent to engage lower abs and hip flexors."),
            ExerciseTemplate("Bicycle Crunch", 30, 4, "Slow, controlled rotational crunch emphasizing full ROM."),
            ExerciseTemplate("Side Plank (each side)", 45, 4, "Support on forearm, stack feet, lift hips to create straight line."),
            ExerciseTemplate("Pallof Press", 30, 3, "Anti-rotation press to train transverse abdominis and stability."),
            ExerciseTemplate("Mountain Climbers (slow)", 30, 5, "Core-loaded knee drives that also increase heart rate."),
            ExerciseTemplate("Cable Crunch", 40, 6, "Weighted crunch movement focusing on maximal contraction of rectus abdominis.")
        ),
        "Shoulder" to listOf(
            ExerciseTemplate("Seated Dumbbell Press", 60, 12, "Neutral grip seated presses with full shoulder ROM and control."),
            ExerciseTemplate("Arnold Press", 50, 11, "Rotational press that hits all three deltoid heads."),
            ExerciseTemplate("Barbell Overhead Press", 80, 16, "Standing strict press to build raw overhead strength."),
            ExerciseTemplate("Lateral Raise", 30, 5, "Isolation move to shape medial deltoid—use controlled tempo."),
            ExerciseTemplate("Face Pull", 30, 4, "Rear-delt and upper-back movement to support shoulder health."),
            ExerciseTemplate("Reverse Fly (DB)", 35, 5, "Single-arm or double-arm reverse fly for posterior delts."),
            ExerciseTemplate("External Rotation (Band)", 20, 2, "Rotator cuff drill to improve external rotation strength."),
            ExerciseTemplate("Push Press", 45, 10, "Slight leg drive to overload the press and train power."),
            ExerciseTemplate("Scaption", 30, 3, "Diagonal raise to strengthen the rotator cuff and shoulder complex."),
            ExerciseTemplate("Cable Upright Row", 40, 6, "Trap and lateral deltoid coordination; keep elbows high and controlled.")
        ),
        "1500kcal" to listOf(
            ExerciseTemplate("Burpee to Half-Jump", 30, 10, "Full-body explosive burpee variant minimizing ground time."),
            ExerciseTemplate("Kettlebell Swing", 45, 9, "Hip hinge power move that elevates heart rate and posterior chain."),
            ExerciseTemplate("Rowing (Sprint)", 60, 15, "Short, powerful row intervals for high calorie burn."),
            ExerciseTemplate("Thruster", 45, 12, "Front squat into press for full-body metabolic load."),
            ExerciseTemplate("Jumping Lunge", 30, 8, "Plyo lunge maintaining soft landings and quick transitions."),
            ExerciseTemplate("Mountain Climbers (fast)", 30, 6, "High-cadence core and cardio move."),
            ExerciseTemplate("Battle Ropes (30s)", 30, 10, "Upper-body and core metabolic conditioning."),
            ExerciseTemplate("Box Jump", 30, 6, "Explosive lower-body movement to spike heart-rate."),
            ExerciseTemplate("Cycling Sprint (45s)", 45, 12, "Stationary bike all-out intervals for calorie burn."),
            ExerciseTemplate("High-Knee Drive", 30, 6, "Running-in-place explosive knee drives to lift intensity.")
        ),
        "Reduced-Fat" to listOf(
            ExerciseTemplate("Incline Walk (20-30 min)", 380, 150, "Steady-state incline walking for extended calorie burn and low joint impact."),
            ExerciseTemplate("Circuit: Row/Bike/Bodyweight", 300, 35, "Mixed-station circuit alternating cardio and bodyweight strength."),
            ExerciseTemplate("Steady-State Bike", 300, 220, "Moderate-intensity cycling to keep heart rate in fat-burning zone."),
            ExerciseTemplate("Circuit Strength (Bodyweight)", 350, 60, "Low-load circuit focusing on movement quality and volume."),
            ExerciseTemplate("Intervals: Walk/Jog", 200, 120, "Alternating brisk walk and jog intervals for sustainable conditioning."),
            ExerciseTemplate("Light Resistance Band Flow", 220, 40, "Band-based full-body work to increase daily NEAT and muscular endurance."),
            ExerciseTemplate("Rowing Steady Pace", 380, 160, "Longer rowing session at conversational pace."),
            ExerciseTemplate("Low-Impact Plyo Circuit", 350, 80, "Modified plyometrics to elevate calorie burn with low joint stress."),
            ExerciseTemplate("Mobility + Core Maintenance", 360, 20, "Supportive mobility work paired with core stability to aid recovery."),
            ExerciseTemplate("Walking Intervals", 180, 18, "Short brisk walks to add movement throughout the day.")
        ),
        "Carb-Lite" to listOf(
            ExerciseTemplate("Low-Volume Strength Circuit", 200, 140, "Moderate strength session focusing on compound lifts with controlled reps."),
            ExerciseTemplate("Steady-State Row", 200, 150, "Low-glycemic steady cardio supporting low-carb fueling."),
            ExerciseTemplate("Tempo Strength (Slow Eccentrics)", 180, 110, "Longer tempo sets that emphasize time under tension rather than maximal power."),
            ExerciseTemplate("Moderate Interval Bike", 350, 90, "Short intervals that avoid glycogen-spiking intensity but maintain conditioning."),
            ExerciseTemplate("Kettlebell Complex", 220, 80, "Small complexes of kettlebell moves that maintain energy and stability."),
            ExerciseTemplate("Bodyweight Metcon", 360, 60, "Low-equipment conditioning that preserves steady-state effort."),
            ExerciseTemplate("Mobility + Light Strength", 420, 45, "Combo session to support joint health while providing stimulus."),
            ExerciseTemplate("Farmers Carry", 60, 8, "Loaded carry to improve posture, core and grip without high glycolytic demand."),
            ExerciseTemplate("Circuit: Step-ups & Rows", 220, 70, "Combination circuit providing strength and steady conditioning."),
            ExerciseTemplate("Breathwork & Core Recovery", 240, 10, "Low-energy recovery that supports metabolic flexibility.")
        ),
        "Veggie Bliss" to listOf(
            ExerciseTemplate("Yin-Style Hip Openers", 60, 4, "Long-held hip stretches to open posterior chain and relieve tension."),
            ExerciseTemplate("Pilates Hundred Prep", 45, 3, "Controlled breathing-driven Pilates move to engage deep core."),
            ExerciseTemplate("Bodyweight Flow Sequence", 60, 5, "Gentle flow linking mobility, balance and light strength."),
            ExerciseTemplate("Standing Balance Drills", 45, 3, "Single-leg balance and stability to support functional movement."),
            ExerciseTemplate("Band-Assisted Squat to Reach", 50, 6, "Goblet-like band squat with reach to build accessible strength."),
            ExerciseTemplate("Low-Impact Step Touch Intervals", 40, 4, "Light intervals to increase heart-rate safely."),
            ExerciseTemplate("Thoracic Rotation Flow", 45, 3, "Upper-back mobility to improve posture and shoulder comfort."),
            ExerciseTemplate("Chair-Assisted Hip Hinge", 45, 4, "Safe hip-hinge patterning with support for beginners."),
            ExerciseTemplate("Breath & Core Reset", 30, 2, "Guided breathwork to reset the nervous system and core engagement."),
            ExerciseTemplate("Short Mobility Cooldown", 60, 3, "Gentle movements to conclude and restore tissue length.")
        )
    )

    fun sampleWorkouts(): List<Workout> {
        var idCounter = 1
        return buildList {
            categoryMeta.forEach { (category, meta) ->
                meta.titles.forEachIndexed { index, title ->
                    val description = meta.descriptions.getOrElse(index) { "Effective workout." }
                    val duration = meta.durations.getOrElse(index) { 600 }
                    val count = meta.counts.getOrElse(index) { 8 }
                    add(
                        Workout(
                            id = idCounter++,
                            title = title,
                            description = description,
                            durationSec = duration,
                            exerciseCount = count,
                            category = category
                        )
                    )
                }
            }
        }
    }

    fun generateExercisesForWorkout(workoutId: Int, title: String, count: Int, category: String = ""): List<Exercise> {
        val cat = category.ifBlank { detectCategory(title) }
        val templates = exerciseTemplates[cat] ?: exerciseTemplates.values.firstOrNull() ?: emptyList()
        if (templates.isEmpty()) return emptyList()

        val meta = categoryMeta[cat]
        val titleIndex = meta?.titles?.indexOf(title) ?: -1
        val targetTotalSec: Int? = if (titleIndex >= 0) meta?.durations?.get(titleIndex) else null

        val resolvedCount = if (count > 0) count else (meta?.counts?.getOrNull(titleIndex) ?: 0)
        if (resolvedCount <= 0) return emptyList()

        val offset = meta?.titles?.indexOf(title)?.coerceAtLeast(0) ?: 0
        val chosen = pickTemplates(templates, offset, resolvedCount)

        val finalDurations = if (targetTotalSec != null && targetTotalSec > 0) {
            val base = targetTotalSec / resolvedCount
            var remainder = targetTotalSec % resolvedCount
            List(resolvedCount) { _ ->
                val add = if (remainder > 0) { remainder--; 1 } else 0
                base + add
            }
        } else {
            chosen.mapIndexed { i, tmpl -> tmpl.durationSec + (i % 3) * 5 }
        }

        return chosen.mapIndexed { i, tmpl ->
            val exId = workoutId * 100 + (i + 1)
            val duration = finalDurations.getOrElse(i) { tmpl.durationSec }
            val calories = calcCalories(tmpl, duration)
            val titleVariant = formatTitleVariant(cat, tmpl)
            val desc = tmpl.description + " Reps/time: follow the workout plan and maintain good form."
            val sampleRawName = makeCategoryResourceName(cat, i + 1)
            Exercise(exId, titleVariant, duration, calories, desc, 0, sampleRawName)
        }
    }

    private fun pickTemplates(templates: List<ExerciseTemplate>, offset: Int, count: Int): List<ExerciseTemplate> {
        val result = ArrayList<ExerciseTemplate>(count)
        for (i in 0 until count) {
            result.add(templates[(offset + i) % templates.size])
        }
        return result
    }

    private fun calcCalories(tmpl: ExerciseTemplate, duration: Int): Int {
        return max(1, (tmpl.calories * duration) / max(30, tmpl.durationSec))
    }

    private fun formatTitleVariant(cat: String, tmpl: ExerciseTemplate): String {
        return when {
            cat == "Booty" && tmpl.title.contains("Bridge", ignoreCase = true) -> "${tmpl.title} - Single-Leg Option"
            cat == "Leg" && tmpl.title.contains("Squat", ignoreCase = true) -> "${tmpl.title} - Tempo Set"
            cat == "Abs" && tmpl.title.contains("Plank", ignoreCase = true) -> "${tmpl.title} - 3x Holds"
            else -> tmpl.title
        }
    }

    // Helper: create a valid Android resource-style slug from a category or other input
    private fun slugify(input: String, maxLen: Int = 50): String {
        var s = input.lowercase()
            .replace(Regex("[^a-z0-9]+"), "_")
            .replace(Regex("_+"), "_")
            .trim('_')
        if (s.isEmpty() || s[0] !in 'a'..'z') s = "r_$s"
        if (s.length > maxLen) s = s.substring(0, maxLen).trimEnd('_')
        return s
    }

    // Build category-based raw resource name like `booty_1`. Falls back to `exercise_1` when category is blank.
    private fun makeCategoryResourceName(category: String, indexOneBased: Int): String {
        val prefix = if (category.isBlank()) "exercise" else slugify(category)
        val idx = if (indexOneBased <= 0) 1 else indexOneBased
        return "${prefix}_$idx"
    }

    fun detectCategory(title: String): String {
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
}
