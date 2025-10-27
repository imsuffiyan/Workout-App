package com.fitness.workout.ui.workouts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitness.workout.databinding.ItemWorkoutBinding
import com.fitness.workout.data.model.Workout
import com.fitness.workout.util.formatDuration
import java.security.cert.Extension
import java.util.Locale

class WorkoutAdapter(private val onClick: (Workout) -> Unit) :
    ListAdapter<Workout, WorkoutAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Workout>() {
            override fun areItemsTheSame(oldItem: Workout, newItem: Workout) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Workout, newItem: Workout) = oldItem == newItem
        }
    }

    inner class VH(private val binding: ItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Workout) {
            binding.title.text = item.title
            binding.image.setImageResource(android.R.color.transparent)
            binding.image.setImageResource(android.R.drawable.ic_menu_gallery)

            binding.time.text = item.durationSec.formatDuration()

            val level = when {
                item.durationSec <= 150 -> "Beginner"
                item.durationSec <= 450 -> "Intermediate"
                else -> "Advanced"
            }
            binding.level.text = level

            try {
                binding.icFire.setColorFilter(binding.root.context.getColor(com.fitness.workout.R.color.accent_orange))
            } catch (_: Exception) {
            }

            binding.root.setOnClickListener { onClick(item) }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
