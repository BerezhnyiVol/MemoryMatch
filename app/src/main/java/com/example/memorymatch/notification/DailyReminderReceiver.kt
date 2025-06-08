package com.example.memorymatch.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.memorymatch.MainActivity
import com.example.memorymatch.R

// Popis: BroadcastReceiver pre zobrazovanie denného pripomenutia pomocou notifikácie
class DailyReminderReceiver : BroadcastReceiver() {

    // Popis: Identifikátory kanála a notifikácie
    companion object {
        private const val CHANNEL_ID = "daily_reminder_channel"
        private const val NOTIFICATION_ID = 1
    }

    // Popis: Spracovanie prijatej udalosti a zobrazenie notifikácie
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            showNotification(context)
        }
    }

    // Popis: Zobrazenie notifikácie pre používateľa
    private fun showNotification(context: Context) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(context.getString(R.string.memory_match))
            .setContentText(context.getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    // Popis: Vytvorenie notifikačného kanála pre Android 8.0 a vyššie
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily Reminder"
            val descriptionText = "Daily MemoryMatch reminder"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
