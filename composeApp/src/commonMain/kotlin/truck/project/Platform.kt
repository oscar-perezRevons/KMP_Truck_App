package truck.project

interface Platform {
    val name: String
    val downloadFolder: String
    fun writeFile(path: String, content: String)
    fun getLogoBase64(): String
}

expect fun getPlatform(): Platform