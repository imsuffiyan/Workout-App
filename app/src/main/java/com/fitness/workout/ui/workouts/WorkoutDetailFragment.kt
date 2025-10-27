// Workout detail screen: shows workout content and timer.
// Start/stop timer and open player.
package com.fitness.workout.ui.workouts

import android.os.Bundle
import android.os.CountDownTimer
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
import com.fitness.workout.R
import com.fitness.workout.databinding.FragmentWorkoutDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutDetailFragment : Fragment() {
    private var _binding: FragmentWorkoutDetailBinding? = null
    private val binding get() = _binding!!
    private val args: WorkoutDetailFragmentArgs by navArgs()
    private val viewModel: WorkoutDetailViewModel by activityViewModels()

    private var timer: CountDownTimer? = null
    private var remainingSec: Int = 0

    private lateinit var exerciseAdapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.workoutId
        viewModel.loadWorkout(id)

        exerciseAdapter = ExerciseAdapter { exercise ->
            val action = WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToExerciseDetailFragment(exercise)
            findNavController().navigate(action)
        }
        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExercises.adapter = exerciseAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detail.collect { detail ->
                    if (detail == null) {
                        exerciseAdapter.submitList(emptyList())
                        binding.tvExercisesCount.text = getString(R.string.exercises_count_format, 0)
                        return@collect
                    }

                    val workout = detail.workout
                    binding.title.text = workout.title
                    binding.description.text = workout.description
                    binding.heroImage.setImageResource(R.drawable.ic_launcher_foreground)

                    if (timer == null) {
                        remainingSec = workout.durationSec
                        binding.timer.text = formatDuration(remainingSec)
                    }

                    val level = when {
                        workout.durationSec <= 150 -> getString(R.string.level_beginner)
                        workout.durationSec <= 450 -> getString(R.string.level_intermediate)
                        else -> getString(R.string.level_advanced)
                    }
                    binding.tvLevel.text = level

                    val caloriesEst = workout.durationSec * 6 / 60
                    binding.tvCalories.text = getString(R.string.calories_format, caloriesEst)

                    val list = detail.exercises
                    exerciseAdapter.submitList(list)
                    binding.tvExercisesCount.text = getString(R.string.exercises_count_format, list.size)
                }
            }
        }

        binding.startButton.setOnClickListener {
            if (timer != null) {
                timer?.cancel()
                timer = null
                binding.startButton.isEnabled = true
                viewModel.detail.value?.workout?.let { binding.timer.text = formatDuration(it.durationSec) }
            } else {
                startTimer()
            }
        }

        binding.btnOpenPlayer.setOnClickListener {
            val workoutId = args.workoutId
            val action = WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToWorkoutPlayerFragment(workoutId)
            findNavController().navigate(action)
        }
    }

    private fun startTimer() {
        val durationSec = viewModel.detail.value?.workout?.durationSec ?: 30
        binding.startButton.isEnabled = false
        if (remainingSec <= 0) remainingSec = durationSec
        timer = object : CountDownTimer(remainingSec * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingSec = (millisUntilFinished / 1000L).toInt()
                binding.timer.text = formatDuration(remainingSec)
            }

            override fun onFinish() {
                binding.timer.text = getString(R.string.done)
                binding.startButton.isEnabled = true
                viewModel.detail.value?.workout?.let { viewModel.logWorkout(it.durationSec) }
                timer = null
                remainingSec = 0
            }
        }.start()
    }

    private fun formatDuration(sec: Int): String {
        val minutes = sec / 60
        val seconds = sec % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        _binding = null
    }
}
