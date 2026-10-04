package ma.fldm.englishstudies.updates

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ma.fldm.englishstudies.R

class EnglishStudiesFirebaseMessagingService :
    FirebaseMessagingService() {

    override fun onMessageReceived(
        remoteMessage: RemoteMessage
    ) {

        val title =
            remoteMessage.notification?.title
                ?: remoteMessage.data["title"]
                ?: "English Studies"

        val message =
            remoteMessage.notification?.body
                ?: remoteMessage.data["body"]
                ?: "Une nouvelle version est disponible."

        showNotification(
            title = title,
            message = message
        )
    }

    private fun showNotification(
        title: String,
        message: String
    ) {

        val channelId =
            "english_studies_updates"

        val manager =
            getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel =
                NotificationChannel(
                    channelId,
                    "Mises à jour",
                    NotificationManager.IMPORTANCE_DEFAULT
                )

            manager.createNotificationChannel(channel)
        }

        val notification =
            NotificationCompat.Builder(
                this,
                channelId
            )
                .setSmallIcon(
                    R.drawable.ic_launcher_foreground
                )
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(
                    NotificationCompat.PRIORITY_DEFAULT
                )
                .build()

        manager.notify(
            1001,
            notification
        )
    }
}

