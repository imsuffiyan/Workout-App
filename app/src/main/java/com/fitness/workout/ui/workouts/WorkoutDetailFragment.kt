package com.fitness.workout.ui.workouts

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.workout.R
import com.fitness.workout.databinding.FragmentWorkoutDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

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

        // setup exercises RecyclerView & adapter
        exerciseAdapter = ExerciseAdapter { exercise ->
            // navigate via Safe Args generated directions
            val action = WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToExerciseDetailFragment(exercise)
            findNavController().navigate(action)
        }
        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExercises.adapter = exerciseAdapter

        viewModel.workout.observe(viewLifecycleOwner) { workout ->
            if (workout == null) return@observe

            binding.title.text = workout.title
            binding.description.text = workout.description
            // placeholder hero image
            binding.heroImage.setImageResource(R.drawable.ic_launcher_foreground)

            // set duration text
            remainingSec = workout.durationSec
            binding.timer.text = formatDuration(remainingSec)

            // difficulty
            val level = when {
                workout.durationSec <= 150 -> getString(com.fitness.workout.R.string.level_beginner)
                workout.durationSec <= 450 -> getString(com.fitness.workout.R.string.level_intermediate)
                else -> getString(com.fitness.workout.R.string.level_advanced)
            }
            binding.tvLevel.text = level

            // calories estimate (same naive calc used in ViewModel)
            val caloriesEst = workout.durationSec * 6 / 60 // per-minute-ish display
            binding.tvCalories.text = getString(com.fitness.workout.R.string.calories_format, caloriesEst)

            // load exercises for this workout
            viewModel.loadExercises(workout.id)
        }

        viewModel.exercises.observe(viewLifecycleOwner) { list ->
            exerciseAdapter.submitList(list)
            binding.tvExercisesCount.text = getString(R.string.exercises_count_format, list.size)
        }


        binding.startButton.setOnClickListener {
            // if a timer is running, stop it; otherwise start for remainingSec
            if (timer != null) {
                timer?.cancel()
                timer = null
                binding.startButton.isEnabled = true
                // reset display to full duration if workout loaded
                viewModel.workout.value?.let { binding.timer.text = formatDuration(it.durationSec) }
            } else {
                startTimer()
            }
        }

        // open player screen showing exercises (center play button)
        binding.btnOpenPlayer.setOnClickListener {
            val workoutId = args.workoutId
            val action = WorkoutDetailFragmentDirections.actionWorkoutDetailFragmentToWorkoutPlayerFragment(workoutId)
            findNavController().navigate(action)
            }
    }

    private fun startTimer() {
        val durationSec = viewModel.workout.value?.durationSec ?: 30
        binding.startButton.isEnabled = false
        // use remainingSec if it was set (allows resume), otherwise use duration
        if (remainingSec <= 0) remainingSec = durationSec
        timer = object : CountDownTimer(remainingSec * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingSec = (millisUntilFinished / 1000L).toInt()
                binding.timer.text = formatDuration(remainingSec)
            }

            override fun onFinish() {
                binding.timer.text = getString(com.fitness.workout.R.string.done)
                binding.startButton.isEnabled = true
                // log workout using the workout duration
                viewModel.workout.value?.let { viewModel.logWorkout(it.durationSec) }
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
