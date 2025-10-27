package com.fitness.workout.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WaterReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        when (action) {
            NotificationUtils.ACTION_WATER_REMINDER -> {
                NotificationUtils.sendWaterNotification(context)
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                // re-schedule alarm after boot
                NotificationUtils.scheduleWaterReminder(context)
            }
        }
    }
}
