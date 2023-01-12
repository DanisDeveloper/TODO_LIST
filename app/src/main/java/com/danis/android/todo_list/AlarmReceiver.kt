package com.danis.android.todo_list

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.danis.android.todo_list.presentation.activities.MainActivity


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        createNotificationChannel(context)

        val i = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, (Math.random()* MAX_RANDOM_VALUE).toInt(), i, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(intent?.getStringExtra(CONTENT_TEXT_KEY))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setFullScreenIntent(pendingIntent, true)
            .build()
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify((Math.random()* MAX_RANDOM_VALUE).toInt(), notification)
    }

    private fun createNotificationChannel(context: Context?) {
        if (Build.VERSION.SDK_INT >= O) {
            context?.let {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, context.getString(R.string.push_notification), importance)
                val notificationManager = it.getSystemService(NotificationManager::class.java) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val CONTENT_TEXT_KEY = "CONTENT_TEXT_KEY"
        private const val MAX_RANDOM_VALUE = 10000000
    }
}

