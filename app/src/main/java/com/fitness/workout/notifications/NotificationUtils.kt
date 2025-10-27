package com.fitness.workout.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.fitness.workout.R
import java.util.concurrent.TimeUnit

object NotificationUtils {
    private const val CHANNEL_ID = "water_reminder_channel"
    private const val CHANNEL_NAME = "Water Reminders"
    private const val CHANNEL_DESC = "Reminds user to drink water"
    private const val NOTIF_ID = 1001
    const val ACTION_WATER_REMINDER = "com.fitness.workout.ACTION_WATER_REMINDER"

    private const val DAY_MS = 24L * 60L * 60L * 1000L
    private const val WORK_TAG = "water_reminder_work"
    private const val WORK_NAME_PREFIX = "water_reminder_"
    private const val MAX_REMINDERS_PER_DAY = 24

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = CHANNEL_DESC
        }
        val nm = context.getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)
    }

    fun sendWaterNotification(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val pending: PendingIntent? = context.packageManager.getLaunchIntentForPackage(context.packageName)?.let { launchIntent ->
            PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time to drink water")
            .setContentText("Stay hydrated — take a sip now.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        pending?.let { builder.setContentIntent(it) }

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIF_ID, builder.build())
        }
    }

    fun scheduleWaterReminder(context: Context, intervalMs: Long = AlarmManager.INTERVAL_HOUR * 2) {
        createNotificationChannel(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, WaterReminderReceiver::class.java).apply {
            action = ACTION_WATER_REMINDER
        }
        val pending = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val triggerAt = System.currentTimeMillis() + intervalMs
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAt, intervalMs, pending)
    }


    private fun scheduleWaterRemindersWork(context: Context, count: Int) {
        createNotificationChannel(context)
        cancelWaterRemindersWork(context)

        val safeCount = count.coerceIn(0, MAX_REMINDERS_PER_DAY)
        if (safeCount <= 0) return

        val intervalMs = DAY_MS / safeCount.toLong()

        val wm = WorkManager.getInstance(context)

        for (i in 0 until safeCount) {
            val initialDelayMs = if (i == 0) intervalMs else intervalMs * i.toLong()
            val workRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(initialDelayMs, TimeUnit.MILLISECONDS)
                .addTag(WORK_TAG)
                .build()

            val uniqueName = WORK_NAME_PREFIX + i
            wm.enqueueUniquePeriodicWork(uniqueName, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
        }
    }

    fun cancelWaterRemindersWork(context: Context) {
        val wm = WorkManager.getInstance(context)
        wm.cancelAllWorkByTag(WORK_TAG)
        for (i in 0 until MAX_REMINDERS_PER_DAY) {
            wm.cancelUniqueWork(WORK_NAME_PREFIX + i)
        }
    }

    private fun getScheduledRemindersCount(context: Context): Int {
        return try {
            val wm = WorkManager.getInstance(context)
            val infos = wm.getWorkInfosByTag(WORK_TAG).get()
            infos.size
        } catch (e: Exception) {
            0
        }
    }

    fun scheduleOrUpdateWaterRemindersWork(context: Context, count: Int) {
        val safeCount = count.coerceIn(0, MAX_REMINDERS_PER_DAY)
        val scheduled = getScheduledRemindersCount(context)

        if (safeCount == scheduled) return

        if (safeCount <= 0) {
            cancelWaterRemindersWork(context)
            return
        }
        scheduleWaterRemindersWork(context, safeCount)
    }
}
