// Home screen: dashboard and quick navigation.
// Shows program progress and categories.
package com.fitness.workout.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AlertDialog
import com.fitness.workout.R
import com.fitness.workout.data.repository.WorkoutRepository
import com.fitness.workout.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import com.fitness.workout.prefs.PrefsManager
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var prefsManager: PrefsManager
    @Inject lateinit var workoutRepository: WorkoutRepository

    private val requestNotification = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // handle the result if needed
        Log.d("TAG123", ": isGranted = $isGranted")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgBooty.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_categoryFragment,
                bundleOf("categoryName" to "Booty")
            )
        }
        binding.imgLeg.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_categoryFragment,
                bundleOf("categoryName" to "Leg")
            )
        }
        binding.imgAbs.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_categoryFragment,
                bundleOf("categoryName" to "Abs")
            )
        }
        binding.imgShoulder.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_categoryFragment,
                bundleOf("categoryName" to "Shoulder")
            )
        }

        binding.cardProgram.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_programFragment)
        }

        binding.imgPlan1500.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_categoryFragment,
                bundleOf("categoryName" to "1500kcal")
            )
        }
        binding.imgReducedFat.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_categoryFragment,
                bundleOf("categoryName" to "Reduced-Fat")
            )
        }
        binding.imgCarbLite.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_categoryFragment,
                bundleOf("categoryName" to "Carb-Lite")
            )
        }
        binding.imgVeggieBliss.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_categoryFragment,
                bundleOf("categoryName" to "Veggie Bliss")
            )
        }

        val totalDays = workoutRepository.programLength.coerceAtLeast(1)
        viewLifecycleOwner.lifecycleScope.launch {
            prefsManager.profile
                .map { it.programDay ?: 1 }
                .distinctUntilChanged()
                .collect { rawDay ->
                    val safeDay = rawDay.coerceAtMost(totalDays).coerceAtLeast(1)
                    val progressText = if (rawDay > totalDays) {
                        "$totalDays/$totalDays"
                    } else {
                        "$safeDay/$totalDays"
                    }
                    binding.tvDayCount.text = progressText
                }
        }

        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)

        requestNotificationPermission()
    }

    private fun showExitDialog() {
        if (!isAdded) return
        val ctx = requireContext()
        val builder = AlertDialog.Builder(ctx)
        val dialogView = layoutInflater.inflate(R.layout.dialog_exit, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancel = dialogView.findViewById<Button>(R.id.btnExitCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnExitConfirm)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            requireActivity().finishAffinity()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
                return
            }
            requestNotification.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
