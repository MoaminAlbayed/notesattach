package albayed.moamin.composenotesattach.screens.alarms

import albayed.moamin.composenotesattach.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri


class AlarmReceiver: BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val content = intent.getStringExtra("content")
        val channelId = intent.getIntExtra("channelId", 0)
        val requestCode = intent.getIntExtra("requestCode", 0)
        val noteId = intent. getStringExtra("noteId")

        val taskDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "myapp://notesattach/${false}/${true}/$noteId".toUri(),
        )

        val pendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(taskDetailIntent)
            getPendingIntent(requestCode, PendingIntent.FLAG_MUTABLE)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId.toString())
            .setSmallIcon(R.drawable.notesattach_notification)
            .setContentTitle("Note Reminder")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(ringtoneUri)

            val name = "notes attach Alarm"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId.toString(), name, importance).apply {
                description = content
                enableVibration(true)
            }
            // Register the channel with the system
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        with (NotificationManagerCompat.from(context)){
            notify(channelId, notificationBuilder.build())
        }

    }
}