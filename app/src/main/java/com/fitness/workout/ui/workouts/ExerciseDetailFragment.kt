// Exercise detail: shows single exercise info.
// Title, description, and meta shown.
package com.fitness.workout.ui.workouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fitness.workout.R
import com.fitness.workout.databinding.FragmentExerciseDetailBinding
import com.fitness.workout.util.toDurationString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseDetailFragment : Fragment() {
    private var _binding: FragmentExerciseDetailBinding? = null
    private val binding get() = _binding!!

    private val args: ExerciseDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExerciseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exercise = args.exercise

        binding.tvTitle.text = exercise.title
        binding.tvMeta.text = getString(R.string.exercise_meta_format, exercise.durationSec.toDurationString(), exercise.calories)
        binding.tvDescription.text = exercise.description
        if (exercise.thumbnailRes != 0) binding.imgHero.setImageResource(exercise.thumbnailRes)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
