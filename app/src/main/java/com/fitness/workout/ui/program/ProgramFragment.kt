// Program screen: current program day UI.
// Shows exercises and completion action.
package com.fitness.workout.ui.program

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitness.workout.R
import com.fitness.workout.databinding.FragmentProgramBinding
import com.fitness.workout.util.toDurationString
import com.fitness.workout.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProgramFragment : Fragment() {

    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProgramViewModel by viewModels()

    private lateinit var adapter: ProgramExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ProgramExerciseAdapter()
        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExercises.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    renderState(state)
                }
            }
        }

        binding.btnCompleteDay.setOnClickListener {
            val state = viewModel.uiState.value
            if (state.plan != null) {
                viewModel.completeCurrentDay()
                requireContext().toast(getString(R.string.program_day_completed_toast))
                findNavController().navigateUp()
            }
        }
    }

    private fun renderState(state: ProgramUiState) {
        binding.tvProgramDay.text = getString(R.string.program_day_header, state.day.coerceAtLeast(1))
        val plan = state.plan
        if (plan != null) {
            binding.tvProgramSub.text = plan.focus
            adapter.submitList(plan.exercises)
            binding.timer.text = plan.totalDurationSec.toDurationString()
            binding.btnCompleteDay.text = if (state.day >= state.totalDays) {
                getString(R.string.program_finish_cta)
            } else {
                getString(R.string.complete_day)
            }
            binding.btnCompleteDay.isEnabled = true
        } else {
            adapter.submitList(emptyList())
            binding.tvProgramSub.text = getString(R.string.program_complete_message)
            binding.timer.text = 0.toDurationString()
            binding.btnCompleteDay.text = getString(R.string.program_finished)
            binding.btnCompleteDay.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
