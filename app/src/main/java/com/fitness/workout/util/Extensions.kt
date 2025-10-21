package com.fitness.workout.util

import android.content.Context
import android.widget.Toast
import java.util.Locale

// Convert seconds into H:MM:SS or MM:SS
fun Int.toDurationString(): String {
    val totalSeconds = this
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    else String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}

// Convenience toast extension
fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// For fragments/views, callers can use requireContext().toast(...) or view.context.toast(...)
