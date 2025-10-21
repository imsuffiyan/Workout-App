package com.fitness.workout.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.workout.R
import com.fitness.workout.databinding.FragmentCategoryBinding
import com.fitness.workout.ui.workouts.WorkoutAdapter
import com.fitness.workout.ui.workouts.WorkoutsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutsViewModel by viewModels()
    private lateinit var adapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = arguments?.getString("categoryName") ?: getString(R.string.category_default)
        binding.tvCategoryTitle.text = category

        val headerRes = when (category.lowercase()) {
            "booty" -> R.drawable.ic_launcher_foreground
            "leg" -> R.drawable.ic_launcher_foreground
            "abs" -> R.drawable.ic_launcher_foreground
            "shoulder" -> R.drawable.ic_launcher_foreground
            "1500kcal" -> R.drawable.ic_launcher_foreground
            "reduced-fat" -> R.drawable.ic_launcher_foreground
            "carb-lite" -> R.drawable.ic_launcher_foreground
            "veggie bliss" -> R.drawable.ic_launcher_foreground
            else -> R.drawable.ic_launcher_foreground
        }
        binding.headerImage.setImageResource(headerRes)

        adapter = WorkoutAdapter { workout ->
            // navigate to workout detail, pass workoutId
            findNavController().navigate(
                R.id.action_categoryFragment_to_workoutDetailFragment,
                bundleOf("workoutId" to workout.id)
            )
        }

        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWorkouts.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.workouts.collectLatest { list ->
                val filtered = if (category == getString(R.string.category_default)) {
                    list
                } else {
                    val key = category.trim()
                    list.filter { it.category.equals(key, ignoreCase = true) }
                }
                adapter.submitList(filtered)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
