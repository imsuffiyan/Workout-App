package com.fitness.workout.ui.program

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.workout.R
import com.fitness.workout.data.repository.WorkoutSeedData
import com.fitness.workout.databinding.FragmentProgramBinding
import com.fitness.workout.prefs.PrefsManager
import com.fitness.workout.util.Constant.PROGRAM_TOTAL_DAYS
import com.fitness.workout.util.toDurationString
import com.fitness.workout.util.toast
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProgramFragment : Fragment() {

    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProgramExerciseAdapter
    private var currentDay = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val prefsManager = PrefsManager(requireContext())

        adapter = ProgramExerciseAdapter()

        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExercises.adapter = adapter

        lifecycleScope.launch {
            prefsManager.profile
                .map { it.programDay ?: 1 }
                .distinctUntilChanged()
                .collect { day ->
                    currentDay = day

                    binding.tvProgramDay.text = getString(R.string.program_day_header, currentDay)
                    binding.tvProgramSub.text = getString(R.string.program_exercises_for_today)

                    val exercises = WorkoutSeedData.generateExercisesForWorkout(currentDay, "Program Day $currentDay", 5)
                    adapter.submitList(exercises)

                    val totalSeconds = exercises.sumOf { it.durationSec }
                    binding.timer.text = totalSeconds.toDurationString()

                    if (currentDay >= PROGRAM_TOTAL_DAYS) {
                        binding.btnCompleteDay.text = getString(R.string.program_finished)
                    } else {
                        binding.btnCompleteDay.text = getString(R.string.complete_day)
                    }
                    binding.btnCompleteDay.isEnabled = adapter.allCompleted()
                }
        }

        binding.btnCompleteDay.setOnClickListener {
            if (adapter.allCompleted()) {
                lifecycleScope.launch {
                    if (currentDay < PROGRAM_TOTAL_DAYS) {
                        prefsManager.incrementProgramDay()
                        requireContext().toast("Day completed — progress updated")
                    } else {
                        requireContext().toast("Congratulations — program complete!", android.widget.Toast.LENGTH_LONG)
                    }
                    findNavController().navigateUp()
                }
            } else {
                requireContext().toast("Please complete all exercises for the day")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
