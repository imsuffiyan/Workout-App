package com.fitness.workout.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.workout.data.model.Exercise
import com.fitness.workout.databinding.FragmentWorkoutPlayerBinding
import com.fitness.workout.util.toDurationString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutPlayerFragment : Fragment() {
    private var _binding: FragmentWorkoutPlayerBinding? = null
    private val binding get() = _binding!!

    private val args: WorkoutPlayerFragmentArgs by navArgs()
    private val viewModel: WorkoutDetailViewModel by activityViewModels()

    private lateinit var exerciseAdapter: ExerciseAdapter
    private var currentIndex = 0

    private val currentExercises: List<Exercise>
        get() = viewModel.detail.value?.exercises.orEmpty()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.videoPlayer.visibility = View.GONE

        exerciseAdapter = ExerciseAdapter { exercise ->
            val action = WorkoutPlayerFragmentDirections.actionWorkoutPlayerFragmentToExerciseDetailFragment(exercise)
            findNavController().navigate(action)
        }
        binding.rvPlayerExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlayerExercises.adapter = exerciseAdapter

        val workoutId = args.workoutId
        viewModel.loadWorkout(workoutId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detail.collect { detail ->
                    if (detail == null) {
                        exerciseAdapter.submitList(emptyList())
                        renderExercise(null, 0)
                        return@collect
                    }
                    val exercises = detail.exercises
                    exerciseAdapter.submitList(exercises)
                    if (exercises.isEmpty()) {
                        currentIndex = 0
                        renderExercise(null, 0)
                        return@collect
                    }
                    if (currentIndex >= exercises.size) {
                        currentIndex = exercises.lastIndex
                    }
                    renderExercise(exercises[currentIndex], exercises.size)
                }
            }
        }

        binding.btnPlayerPlay.setOnClickListener {
            val exercise = currentExercises.getOrNull(currentIndex) ?: return@setOnClickListener
            val action = WorkoutPlayerFragmentDirections.actionWorkoutPlayerFragmentToExerciseDetailFragment(exercise)
            findNavController().navigate(action)
        }

        binding.btnPrev.setOnClickListener {
            val list = currentExercises
            if (list.isEmpty()) return@setOnClickListener
            currentIndex = (currentIndex - 1).coerceAtLeast(0)
            renderExercise(list[currentIndex], list.size)
            binding.rvPlayerExercises.scrollToPosition(currentIndex)
        }

        binding.btnNext.setOnClickListener {
            val list = currentExercises
            if (list.isEmpty()) return@setOnClickListener
            currentIndex = (currentIndex + 1).coerceAtMost(list.lastIndex)
            renderExercise(list[currentIndex], list.size)
            binding.rvPlayerExercises.scrollToPosition(currentIndex)
        }
    }

    private fun renderExercise(exercise: Exercise?, total: Int) {
        if (exercise == null || total <= 0) {
            binding.tvPlayerTimer.text = getString(com.fitness.workout.R.string.timer_default)
            binding.tvPlayerCalories.text = getString(com.fitness.workout.R.string.calories_format, 0)
            binding.tvPlayerIndex.text = "0/0"
            binding.btnPlayerPlay.isEnabled = false
            return
        }
        binding.tvPlayerTimer.text = exercise.durationSec.toDurationString()
        binding.tvPlayerCalories.text = getString(com.fitness.workout.R.string.calories_format, exercise.calories)
        binding.tvPlayerIndex.text = "${currentIndex + 1}/$total"
        binding.btnPlayerPlay.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
