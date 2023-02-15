package albayed.moamin.notesattach.screens.alarms

import albayed.moamin.notesattach.R
import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import okhttp3.internal.notify


class AlarmReceiver: BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {

        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val content = intent.getStringExtra("content")
        val channelId = intent.getIntExtra("channelId", 0)
        val requestCode = intent.getIntExtra("requestCode", 0)

        val notificationBuilder = NotificationCompat.Builder(context, channelId.toString())
            .setSmallIcon(R.mipmap.notesattach)
            .setContentTitle("Note Reminder")
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSound(ringtoneUri)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notes attach Alarm"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId.toString(), name, importance).apply {
                description = content
//                enableVibration(true)
//                vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            }
            // Register the channel with the system
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with (NotificationManagerCompat.from(context)){
//            notify(channelId, notificationBuilder.build())
            notify(channelId, notificationBuilder.build())
            Log.d("here", "onReceive: hereeeeeeeeeeeee")
        }

    }
}