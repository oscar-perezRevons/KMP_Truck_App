package truck.project.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import truck.project.data.remote.TranslationService
import truck.project.truck_guia.data.repository.TruckRepositoryImpl
import truck.project.truck_guia.domain.repository.TruckRepository

val dataModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }
    
    singleOf(::TranslationService)
    singleOf(::TruckRepositoryImpl).bind<TruckRepository>()
}
