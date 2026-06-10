package truck.project.core.data.remote

interface FirebaseConfig {
    fun getString(key: String): String
    fun fetchAndActivate(onComplete: (Boolean) -> Unit)
}
