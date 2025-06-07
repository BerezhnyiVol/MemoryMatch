package com.example.memorymatch.notification
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.content.Context
    import android.os.Build
    import androidx.core.app.NotificationCompat
    import androidx.work.Worker
    import androidx.work.WorkerParameters

    class DailyReminderWorker(
        context: Context,
        workerParams: WorkerParameters
    ) : Worker(context, workerParams) {

        companion object {
            private const val CHANNEL_ID = "daily_reminder_channel"
            private const val NOTIFICATION_ID = 1
        }

        override fun doWork(): Result {
            createNotificationChannel()
            showNotification()
            return Result.success()
        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Daily Reminder"
                val descriptionText = "Daily MemoryMatch reminder"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                val notificationManager: NotificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }

        private fun showNotification() {
            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("MemoryMatch")
                .setContentText("Пора тренировать память — зайди сыграть!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }
