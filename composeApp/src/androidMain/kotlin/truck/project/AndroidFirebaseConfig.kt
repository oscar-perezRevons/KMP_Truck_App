package truck.project

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import truck.project.core.data.remote.FirebaseConfig

class AndroidFirebaseConfig : FirebaseConfig {
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    override fun getString(key: String): String {
        return remoteConfig.getString(key)
    }

    override fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}
