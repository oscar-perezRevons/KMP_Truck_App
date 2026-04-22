import java.util.Properties
import java.net.URL

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "truck.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "truck.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.uiToolingPreview)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.google.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.accompanist.permissions)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.material.icons.extended)
    implementation(libs.google.places)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.database)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.work.runtime)
    debugImplementation(libs.compose.uiTooling)
}

tasks.register("downloadTranslations") {
    description = "Descarga los archivos strings.xml desde Loco"
    group = "localization"

    doLast {
        val locales = mapOf(
            "en" to "values",
            "es" to "values-es"
        )

        val props = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            props.load(localPropertiesFile.inputStream())
        }
        
        val apiKey = props.getProperty("loco.apiKey")
        val resPath = "src/main/res"

        if (apiKey == null || apiKey == "tu_key_de_la_captura_aqui") {
            throw GradleException("No se encontró una loco.apiKey válida en local.properties")
        }

        locales.forEach { (lang, folder) ->
            println("Descargando traducción para [$lang] en res/$folder...")

            val folderFile = file("$resPath/$folder")
            if (!folderFile.exists()) folderFile.mkdirs()

            val stringsFile = File(folderFile, "strings.xml")
            val url = "https://localise.biz/api/export/locale/$lang.xml?key=$apiKey"

            try {
                val connection = URL(url).openConnection()
                connection.connect()
                val text = connection.getInputStream().bufferedReader().use { it.readText() }
                stringsFile.writeText(text)
                println("¡Éxito! Archivo guardado en ${stringsFile.path}")
            } catch (e: Exception) {
                println("Error descargando [$lang]: ${e.message}")
            }
        }
    }
}
