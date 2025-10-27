// Workout player: play exercises and videos.
// Controls play, previous, next actions.
package com.fitness.workout.ui.workouts

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.fitness.workout.data.model.Exercise
import com.fitness.workout.databinding.FragmentWorkoutPlayerBinding
import com.fitness.workout.R
import com.fitness.workout.util.toDurationString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutPlayerFragment : Fragment() {
    private var _binding: FragmentWorkoutPlayerBinding? = null
    private val binding get() = _binding!!

    private val args: WorkoutPlayerFragmentArgs by navArgs()
    private val viewModel: WorkoutDetailViewModel by activityViewModels()

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

        viewModel.loadWorkout(args.workoutId)

        setupObservers()
        setupControls()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detail.collect { detail ->
                    val exercises = detail?.exercises.orEmpty()
                    if (exercises.isEmpty()) {
                        currentIndex = 0
                        renderExercise(null, 0)
                        return@collect
                    }
                    if (currentIndex > exercises.lastIndex) {
                        currentIndex = exercises.lastIndex
                    }
                    renderExercise(exercises[currentIndex], exercises.size)
                }
            }
        }
    }

    private fun setupControls() = with(binding) {
        btnPlayerPlay.setOnClickListener { togglePlayPause() }

        btnPrev.setOnClickListener {
            val list = currentExercises
            if (list.isEmpty()) return@setOnClickListener
            currentIndex = (currentIndex - 1).coerceAtLeast(0)
            renderExercise(list[currentIndex], list.size)
        }

        btnNext.setOnClickListener {
            val list = currentExercises
            if (list.isEmpty()) return@setOnClickListener
            currentIndex = (currentIndex + 1).coerceAtMost(list.lastIndex)
            renderExercise(list[currentIndex], list.size)
        }
    }

    private fun renderExercise(exercise: Exercise?, total: Int) = with(binding) {
        if (exercise == null || total <= 0) {
            tvPlayerTimer.text = getString(com.fitness.workout.R.string.timer_default)
            tvPlayerCalories.text = getString(com.fitness.workout.R.string.calories_format, 0)
            tvPlayerIndex.text = getString(R.string.player_index_format, 0, 0)
            btnPlayerPlay.isEnabled = false
            stopAndHideVideo()
            return
        }

        tvPlayerTimer.text = exercise.durationSec.toDurationString()
        tvPlayerCalories.text = getString(com.fitness.workout.R.string.calories_format, exercise.calories)
        tvPlayerIndex.text = getString(R.string.player_index_format, currentIndex + 1, total)
        btnPlayerPlay.isEnabled = true

        setVideoForExercise(exercise)
    }

    @SuppressLint("DiscouragedApi")
    private fun setVideoForExercise(exercise: Exercise) = with(binding) {
        val videoName = exercise.videoResName.takeIf { it.isNotBlank() } ?: run {
            stopAndHideVideo()
            return@run
        }

        val resId = resources.getIdentifier(videoName.toString(), "raw", requireContext().packageName)
        if (resId == 0) {
            stopAndHideVideo()
            return@with
        }

        val uri = Uri.parse("android.resource://${requireContext().packageName}/$resId")
        videoPlayer.apply {
            visibility = View.VISIBLE
            setVideoURI(uri)
            setMediaController(null)
            setOnPreparedListener { mp ->
                mp.isLooping = true
                mp.start()
            }
        }
    }

    private fun togglePlayPause() = with(binding) {
        if (videoPlayer.visibility != View.VISIBLE) return
        if (videoPlayer.isPlaying) videoPlayer.pause() else videoPlayer.start()
    }

    private fun stopAndHideVideo() = runCatching {
        binding.videoPlayer.stopPlayback()
    }.also {
        binding.videoPlayer.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        if (_binding != null && binding.videoPlayer.isPlaying) binding.videoPlayer.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        runCatching { binding.videoPlayer.stopPlayback() }
        _binding = null
    }
}
