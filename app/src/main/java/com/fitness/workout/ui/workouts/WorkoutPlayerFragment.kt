package com.fitness.workout.ui.workouts

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.workout.databinding.FragmentWorkoutPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutPlayerFragment : Fragment() {
    private var _binding: FragmentWorkoutPlayerBinding? = null
    private val binding get() = _binding!!

    private val args: WorkoutPlayerFragmentArgs by navArgs()
    private val viewModel: WorkoutDetailViewModel by activityViewModels()

    private lateinit var exerciseAdapter: ExerciseAdapter
    private var currentIndex = 0

    private var mediaController: MediaController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup adapter
        exerciseAdapter = ExerciseAdapter { exercise ->
            // navigate to exercise detail using Safe Args
            val action = WorkoutPlayerFragmentDirections.actionWorkoutPlayerFragmentToExerciseDetailFragment(exercise)
            findNavController().navigate(action)
        }
        binding.rvPlayerExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlayerExercises.adapter = exerciseAdapter

        // Prepare a MediaController for the VideoView
        mediaController = MediaController(requireContext())
        mediaController?.setAnchorView(binding.videoPlayer)
        binding.videoPlayer.setMediaController(null)

        // load workout + exercises
        val workoutId = args.workoutId
        viewModel.loadWorkout(workoutId)
        viewModel.loadExercises(workoutId)

        viewModel.workout.observe(viewLifecycleOwner) { w ->
            if (w == null) return@observe
            binding.tvPlayerCalories.text = getString(com.fitness.workout.R.string.calories_format, (w.durationSec * 6 / 60))
        }

        viewModel.exercises.observe(viewLifecycleOwner) { list ->
            exerciseAdapter.submitList(list)
            // ensure index within bounds
            if (currentIndex >= list.size) currentIndex = (list.size - 1).coerceAtLeast(0)
            updateIndexDisplay()
            // set total index
            binding.tvPlayerIndex.text = formatIndex(currentIndex, list.size)
            // start playing current exercise's video if available
            playVideoForIndex(currentIndex)
        }

        // play button toggles playback
        binding.btnPlayerPlay.setOnClickListener {
            val vv: VideoView = binding.videoPlayer
            if (vv.visibility != View.VISIBLE) {
                // nothing to play; just toggle UI (no-op)
                return@setOnClickListener
            }
            if (vv.isPlaying) {
                vv.pause()
                binding.btnPlayerPlay.setImageResource(com.fitness.workout.R.drawable.ic_play)
            } else {
                vv.start()
                binding.btnPlayerPlay.setImageResource(com.fitness.workout.R.drawable.ic_play)
            }
        }

        binding.btnPrev.setOnClickListener {
            val list = viewModel.exercises.value ?: return@setOnClickListener
            if (list.isEmpty()) return@setOnClickListener
            currentIndex = (currentIndex - 1).coerceAtLeast(0)
            scrollToCurrent()
            playVideoForIndex(currentIndex)
        }

        binding.btnNext.setOnClickListener {
            val list = viewModel.exercises.value ?: return@setOnClickListener
            if (list.isEmpty()) return@setOnClickListener
            currentIndex = (currentIndex + 1).coerceAtMost(list.size - 1)
            scrollToCurrent()
            playVideoForIndex(currentIndex)
        }

    }

    private fun playVideoForIndex(idx: Int) {
        val list = viewModel.exercises.value ?: return
        if (list.isEmpty() || idx < 0 || idx >= list.size) return
        val exercise = list[idx]

        // Try raw resource first (videoResName), then fallback to videoUrl
        val resName = exercise.videoResName
        if (resName.isNotBlank()) {
            val resId = resources.getIdentifier(resName, "raw", requireContext().packageName)
            if (resId != 0) {
                val uri = Uri.parse("android.resource://${requireContext().packageName}/$resId")
                startVideo(uri)
                return
            }
        }

        // No video available: hide VideoView and show hero image
        stopAndHideVideo()
    }

    private fun startVideo(uri: Uri) {
        try {
            binding.videoPlayer.setVideoURI(uri)
            binding.videoPlayer.visibility = View.VISIBLE
            binding.videoPlayer.setOnPreparedListener { mp ->
                // start playback when ready
                binding.videoPlayer.start()
                // update play icon
                binding.btnPlayerPlay.setImageResource(com.fitness.workout.R.drawable.ic_play)
            }
            binding.videoPlayer.setOnCompletionListener {
                // Video finished: show hero or keep video stopped
                binding.btnPlayerPlay.setImageResource(com.fitness.workout.R.drawable.ic_play)
            }
        } catch (e: Exception) {
            // fallback to image
            stopAndHideVideo()
        }
    }

    private fun stopAndHideVideo() {
        try {
            binding.videoPlayer.stopPlayback()
        } catch (e: Exception) { /* ignore */ }
        binding.videoPlayer.visibility = View.GONE
    }

    private fun scrollToCurrent() {
        val list = viewModel.exercises.value ?: emptyList()
        if (list.isEmpty()) return
        binding.rvPlayerExercises.scrollToPosition(currentIndex)
        updateIndexDisplay()
    }

    private fun updateIndexDisplay() {
        val list = viewModel.exercises.value ?: emptyList()
        binding.tvPlayerIndex.text = formatIndex(currentIndex, list.size)
    }

    private fun formatIndex(idx: Int, total: Int): String = if (total <= 0) "0/0" else "${idx + 1}/$total"

    override fun onDestroyView() {
        super.onDestroyView()
        // ensure video playback stopped to free resources
        stopAndHideVideo()
        mediaController = null
        _binding = null
    }
}
