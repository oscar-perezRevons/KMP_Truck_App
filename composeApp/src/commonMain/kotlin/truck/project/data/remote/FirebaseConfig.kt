package truck.project.data.remote

interface FirebaseConfig {
    fun fetchAndActivate(onComplete: (Boolean) -> Unit)
    fun getString(key: String): String
}
