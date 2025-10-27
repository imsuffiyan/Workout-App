package com.fitness.workout.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class WaterReminderWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        // Send the water reminder notification
        NotificationUtils.sendWaterNotification(applicationContext)
        return Result.success()
    }
}

