package com.fitness.workout.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.fitness.workout.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import com.fitness.workout.prefs.PrefsManager
import com.fitness.workout.util.Constant.PROGRAM_TOTAL_DAYS
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Focus area card clicks navigate to CategoryFragment with a category name
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

        // Program card click - open ProgramFragment
        binding.cardProgram.setOnClickListener {
            // navigate to program fragment
            findNavController().navigate(R.id.action_homeFragment_to_programFragment)
        }

        // Recipe card clicks - navigate to same CategoryFragment so they show related workouts
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

        // Observe program progress from PrefsManager (DataStore) and display it
        val prefsManager = PrefsManager(requireContext())
        lifecycleScope.launch {
            prefsManager.profile
                .map { it.programDay ?: 1 }
                .distinctUntilChanged()
                .collect { day ->
                    binding.tvDayCount.text = "$day/$PROGRAM_TOTAL_DAYS"
                }
        }

        // Handle system back press on HomeFragment to show exit confirmation
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backCallback)

    }

    private fun showExitDialog() {
        if (!isAdded) return
        val ctx = requireContext()
        val builder = AlertDialog.Builder(ctx)
        val dialogView = layoutInflater.inflate(R.layout.dialog_exit, null)
        builder.setView(dialogView)
        val dialog = builder.create()
        // make background transparent so our rounded drawable shows correctly
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancel = dialogView.findViewById<Button>(R.id.btnExitCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnExitConfirm)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            // Close the app
            requireActivity().finishAffinity()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
