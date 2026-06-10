package truck.project

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle Notification message
        remoteMessage.notification?.let {
            showNotification(it.title ?: "TruckFlow", it.body ?: "")
        }

        // Handle Data message for Real-time Expenses
        if (remoteMessage.data.isNotEmpty()) {
            val type = remoteMessage.data["type"]
            if (type == "NEW_EXPENSE") {
                val monto = remoteMessage.data["amount"]
                val chofer = remoteMessage.data["driver"]
                showNotification(
                    "Nuevo Gasto Registrado",
                    "$chofer ha registrado un gasto de $$monto"
                )
            }
        }
    }

    private fun showNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, "default_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        // Guardar el token en Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().getReference("tokens")
        database.child("android_device_token").setValue(token)
            .addOnSuccessListener {
                println("Token de FCM guardado exitosamente en Firebase")
            }
            .addOnFailureListener {
                println("Error al guardar el token de FCM: ${it.message}")
            }
    }
}
