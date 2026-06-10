package truck.project

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.koin.android.ext.android.inject
import truck.project.core.data.remote.FirebaseConfig
import truck.project.worker.TranslationWorker

class MainActivity : ComponentActivity() {

    private val firebaseConfig: FirebaseConfig by inject()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM", "Permiso de notificaciones concedido")
        } else {
            Log.w("FCM", "Permiso de notificaciones denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        askNotificationPermission()
        scheduleTranslationWork()

        // Obtener el token de FCM manualmente al iniciar
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Error al obtener el token", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("FCM", "Token actual: $token")
            
            // Guardar en Realtime Database
            val database = FirebaseDatabase.getInstance().getReference("tokens")
            database.child("android_device_token").setValue(token)
                .addOnSuccessListener {
                    Log.d("DatabaseTest", "Token guardado correctamente en Realtime Database")
                }
                .addOnFailureListener {
                    Log.e("DatabaseTest", "Error al guardar token: ${it.message}")
                }

            // Prueba de escritura adicional para verificar Realtime Database
            val testRef = FirebaseDatabase.getInstance().getReference("verification")
            testRef.child("last_connection").setValue(System.currentTimeMillis().toString())
                .addOnSuccessListener {
                    Log.d("DatabaseTest", "Prueba de escritura exitosa en 'verification'")
                }
                .addOnFailureListener {
                    Log.e("DatabaseTest", "Prueba de escritura fallida: ${it.message}")
                }
        }

        // Verificar Firebase Remote Config
        firebaseConfig.fetchAndActivate { success ->
            if (success) {
                val welcomeMessage = firebaseConfig.getString("welcome_message")
                val isMaintenance = firebaseConfig.getString("is_maintenance")
                Log.d("RemoteConfig", "Fetch exitoso. Mensaje: $welcomeMessage, Mantenimiento: $isMaintenance")
            } else {
                Log.e("RemoteConfig", "Error al sincronizar Remote Config")
            }
        }

        setContent {
            App()
        }
    }

    private fun scheduleTranslationWork() {
        val workRequest = OneTimeWorkRequestBuilder<TranslationWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun askNotificationPermission() {
        // Esto solo es necesario para API level >= 33 (Android 13)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Ya tenemos el permiso
            } else {
                // Solicitar el permiso
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}