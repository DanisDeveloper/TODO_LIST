package com.danis.android.todo_list

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.danis.android.todo_list.model.CaseTODO
import com.danis.android.todo_list.model.Repository
import com.danis.android.todo_list.view.MainActivity

private const val CHANNEL_ID = "CHANNEL_ID"
private const val NOTIFICATION_ID = 101
private const val CONTENT_TEXT_KEY = "CONTENT_TEXT_KEY"
private const val CASE_ID = "CASE_ID"


class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context,0,i, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context!!,CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(intent?.getStringExtra(CONTENT_TEXT_KEY))
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID,builder.build())
    }

}

