package com.mdproject.dicodingevent.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mdproject.dicodingevent.R
import com.mdproject.dicodingevent.data.EventRepository
import com.mdproject.dicodingevent.di.Injection
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private val eventRepository: EventRepository = Injection.provideRepository(context)

    override suspend fun doWork(): Result {
        return try {
            val event = eventRepository.getNearestEvent()?.listEvents?.firstOrNull()
            if (event != null) {
                showNotification(
                    title = "[Important] Event Reminder: ${formatEventDate(event.beginTime)}",
                    description = event.name,
                    link = event.link
                )
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun formatEventDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("d MMM, HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            "Unknown date"
        }
    }

    private fun showNotification(title: String, description: String, link: String) {
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0,
            Intent(Intent.ACTION_VIEW, Uri.parse(link)),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_upcoming_black_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(description))
            .setAutoCancel(true)
            .build()

        with(applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager) {
            createNotificationChannelIfNecessary()
            notify(NOTIFICATION_ID, notification)
        }
    }

    private fun NotificationManager.createNotificationChannelIfNecessary() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for event reminders"
            }
            createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "event_reminder_channel"
        const val CHANNEL_NAME = "Event Reminder Channel"
    }
}