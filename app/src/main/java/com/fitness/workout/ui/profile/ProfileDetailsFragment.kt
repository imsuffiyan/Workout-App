package com.fitness.workout.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.fitness.workout.R
import com.fitness.workout.databinding.FragmentProfileDetailsBinding
import com.fitness.workout.prefs.UserProfile
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import com.fitness.workout.ui.common.DialogUtils

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment() {
    private var _binding: FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    // keep latest profile snapshot to preserve fields when saving partial updates
    private var currentProfile: UserProfile? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Resolve views explicitly from binding.root to avoid any generated-binding mismatch
        val rowAge = binding.root.findViewById<MaterialCardView>(R.id.rowAge)
        val rowPurpose = binding.root.findViewById<MaterialCardView>(R.id.rowPurpose)
        val rowUnits = binding.root.findViewById<MaterialCardView>(R.id.rowUnits)
        val rowHeight = binding.root.findViewById<MaterialCardView>(R.id.rowHeight)
        val rowCurrent = binding.root.findViewById<MaterialCardView>(R.id.rowCurrentWeight)
        val rowTarget = binding.root.findViewById<MaterialCardView>(R.id.rowTargetWeight)

        val tvAgeValue = binding.root.findViewById<TextView>(R.id.tvAgeValue)
        val tvPurposeValue = binding.root.findViewById<TextView>(R.id.tvPurposeValue)
        val tvUnitsValue = binding.root.findViewById<TextView>(R.id.tvUnitsValue)
        val tvHeightValue = binding.root.findViewById<TextView>(R.id.tvHeightValue)
        val tvCurrentValue = binding.root.findViewById<TextView>(R.id.tvCurrentValue)
        val tvTargetValue = binding.root.findViewById<TextView>(R.id.tvTargetValue)

        // Observe profile
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profile.collectLatest { profile ->
                currentProfile = profile
                tvAgeValue.text = profile.age?.toString() ?: ""
                tvPurposeValue.text = profile.purpose ?: ""
                tvUnitsValue.text = profile.units ?: "lb/ft"
                tvHeightValue.text = profile.height ?: ""
                tvCurrentValue.text =
                    profile.currentWeight?.let { formatWeight(it, profile.units) } ?: ""
                tvTargetValue.text =
                    profile.targetWeight?.let { formatWeight(it, profile.units) } ?: ""
            }
        }

        // Click listeners to edit fields
        rowAge.setOnClickListener {
            DialogUtils.showEditDialog(
                this,
                getString(R.string.profile_age),
                tvAgeValue.text.toString(),
                InputType.TYPE_CLASS_NUMBER
            ) { newValue ->
                val intVal = newValue.toIntOrNull()
                if (intVal != null) {
                    // preserve other fields by passing current values
                    viewModel.saveProfile(
                        name = currentProfile?.name,
                        age = intVal,
                        target = currentProfile?.targetWeight,
                        notifications = currentProfile?.notificationsEnabled ?: false
                    )
                    showSaved()
                }
            }
        }

        rowPurpose.setOnClickListener {
            DialogUtils.showEditDialog(
                this,
                getString(R.string.profile_purpose),
                tvPurposeValue.text.toString(),
                InputType.TYPE_CLASS_TEXT
            ) { newValue ->
                viewModel.saveExtended(purpose = newValue)
                showSaved()
            }
        }

        rowUnits.setOnClickListener { showUnitsDialog() }

        rowHeight.setOnClickListener {
            DialogUtils.showEditDialog(
                this,
                getString(R.string.profile_height),
                tvHeightValue.text.toString(),
                InputType.TYPE_CLASS_TEXT
            ) { newValue ->
                viewModel.saveExtended(height = newValue)
                showSaved()
            }
        }

        rowCurrent.setOnClickListener {
            DialogUtils.showEditDialog(
                this,
                getString(R.string.profile_current_weight),
                tvCurrentValue.text.toString(),
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            ) { newValue ->
                // strip non-digits except dot and minus
                val numeric = newValue.filter { it.isDigit() || it == '.' || it == '-' }
                val f = numeric.toFloatOrNull()
                if (f != null) {
                    viewModel.saveExtended(currentWeight = f)
                    showSaved()
                }
            }
        }

        rowTarget.setOnClickListener {
            DialogUtils.showEditDialog(
                this,
                getString(R.string.profile_target_weight_label),
                tvTargetValue.text.toString(),
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            ) { newValue ->
                val numeric = newValue.filter { it.isDigit() || it == '.' || it == '-' }
                val f = numeric.toFloatOrNull()
                if (f != null) {
                    // use saveTargetWeight convenience method
                    viewModel.saveTargetWeight(f)
                    showSaved()
                }
            }
        }
    }

    private fun formatWeight(value: Float, units: String?): String {
        val suffix = if (units == null || units.contains("lb", ignoreCase = true)) "lbs" else "kg"
        return String.format(Locale.US, "%.1f %s", value, suffix)
    }

    private fun showUnitsDialog() {
        // Inflate custom attractive units dialog
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_units, null)
        val rbLb = dialogView.findViewById<android.widget.RadioButton>(R.id.rbLbFt)
        val rbKg = dialogView.findViewById<android.widget.RadioButton>(R.id.rbKgM)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnUnitsCancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btnUnitsSave)

        // Determine current selection from the displayed units text
        val current = binding.root.findViewById<TextView>(R.id.tvUnitsValue).text.toString()
        if (current.contains("kg", ignoreCase = true) || current.contains(
                "kg/m",
                ignoreCase = true
            )
        ) {
            rbKg.isChecked = true
        } else {
            rbLb.isChecked = true
        }

        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnSave.setOnClickListener {
            val selected = if (rbKg.isChecked) "kg/m" else "lb/ft"
            viewModel.saveExtended(units = selected)
            showSaved()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSaved() {
        Snackbar.make(binding.root, getString(R.string.saved), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
