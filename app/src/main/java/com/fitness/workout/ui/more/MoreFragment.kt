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

        // Observe profile and update the dashboard views
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profile.collectLatest { profile ->
                // dashboard stats (placeholders kept if profile doesn't provide values)
                binding.tvWorkoutsCount.text = "0"
                binding.tvCalories.text = "0.0"
                binding.tvMinutes.text = "0"
                // Water: show persisted water total (ml)
                val waterMl = profile.waterMl ?: 0
                val unit = profile.units ?: "lb/ft"
                binding.tvWaterMl.text = String.format(Locale.getDefault(), "%d", waterMl)
                binding.tvWeightValue.text = profile.currentWeight.toString()
                binding.tvWeightUnit.text = unit
                binding.tvMyProfileUnit.text = unit
            }
        }

        // My Profile row (in General Settings) opens the details screen
        binding.myProfile.setOnClickListener {
            // navigate directly to the destination id (action id may not be generated yet)
            findNavController().navigate(R.id.profileDetailsFragment)
        }

        // FAB: Add weight - show the same attractive edit dialog as ProfileDetailsFragment
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
                    viewModel.saveExtended(currentWeight = f)
                    binding.tvWeightValue.text = "$f"
                }
            }
        }

        // FAB: Add water - increment and persist the tvWaterMl value by 275
        binding.fabWaterAdd.setOnClickListener {
            val added = 275
            viewModel.incrementWater(added)
        }
        // other rows can be wired later (history, coach)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
