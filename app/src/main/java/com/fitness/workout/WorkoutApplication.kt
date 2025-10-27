package com.fitness.workout

import android.app.Application
import com.fitness.workout.notifications.NotificationUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WorkoutApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationUtils.createNotificationChannel(this)
    }
}
