// More screen: dashboard and quick actions.
// Adds water and updates weight via ViewModel.
package com.fitness.workout.ui.more

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fitness.workout.R
import com.fitness.workout.databinding.FragmentMoreBinding
import com.fitness.workout.notifications.NotificationUtils
import com.fitness.workout.ui.common.DialogUtils
import com.fitness.workout.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MoreFragment : Fragment() {
    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MoreFragment", "onViewCreated")

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profile.collectLatest { profile ->
                binding.tvWorkoutsCount.text = "0"
                binding.tvCalories.text = "0.0"
                binding.tvMinutes.text = "0"
                val waterMl = profile.waterMl ?: 0
                val unit = profile.units ?: "lb/ft"
                binding.tvWaterMl.text = String.format(Locale.getDefault(), "%d", waterMl)
                binding.tvWeightValue.text = profile.currentWeight.toString()
                binding.tvWeightUnit.text = unit
                binding.tvMyProfileUnit.text = unit
            }
        }

        binding.myProfile.setOnClickListener {
            findNavController().navigate(R.id.profileDetailsFragment)
        }

        binding.fabWeightAdd.setOnClickListener {
            val currentValue = binding.tvWeightValue.text.toString()
            DialogUtils.showEditDialog(
                this,
                getString(R.string.weight),
                currentValue,
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            ) { newValue ->
                val numeric = newValue.filter { it.isDigit() || it == '.' || it == '-' }
                val f = numeric.toFloatOrNull()
                if (f != null) {
                    viewModel.setCurrentWeight(f)
                }
            }
        }

        binding.fabWaterAdd.setOnClickListener {
            onWaterAdd()
        }
    }

    private fun onWaterAdd() {
        val added = 275
        val currentText = binding.tvWaterMl.text.toString().filter { it.isDigit() }
        val current = currentText.toIntOrNull() ?: 0
        val newTotal = current + added
        viewModel.incrementWater(added)
        val context = requireContext()
        if (newTotal <= 0) {
            NotificationUtils.cancelWaterRemindersWork(context)
        } else {
            val count = (newTotal / 275).coerceAtLeast(1)
            NotificationUtils.scheduleOrUpdateWaterRemindersWork(context, count)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
