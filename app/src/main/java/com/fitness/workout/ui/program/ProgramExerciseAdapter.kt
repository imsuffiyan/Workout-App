package com.fitness.workout.ui.program

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitness.workout.data.model.Exercise
import com.fitness.workout.databinding.ItemProgramBinding
import com.fitness.workout.util.toDurationString

class ProgramExerciseAdapter : ListAdapter<Exercise, ProgramExerciseAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Exercise>() {
            override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise) = oldItem == newItem
        }
    }

    inner class VH(private val binding: ItemProgramBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Exercise) {
            binding.tvTitle.text = item.title
            binding.tvDuration.text = item.durationSec.toDurationString()
            if (item.thumbnailRes != 0) binding.imgThumb.setImageResource(item.thumbnailRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemProgramBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
