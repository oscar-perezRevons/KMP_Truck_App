package truck.project.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import truck.project.Platform
import truck.project.getPlatform
import truck.project.core.data.remote.RemoteDatabase
import truck.project.core.data.remote.TranslationService
import truck.project.features.auth.data.remote.AuthApi
import truck.project.features.auth.data.remote.KtorAuthApi
import truck.project.features.auth.data.repository.AuthRepositoryImpl
import truck.project.features.auth.domain.repository.AuthRepository
import truck.project.features.fleet.data.repository.TruckRepositoryImpl
import truck.project.features.fleet.domain.repository.TruckRepository
import truck.project.features.admin.data.repository.AdminRepositoryImpl
import truck.project.features.admin.domain.repository.AdminRepository
import truck.project.features.driver.data.repository.DriverTripRepositoryImpl
import truck.project.features.driver.domain.repository.DriverTripRepository
import truck.project.core.reporting.ReportService
import truck.project.core.reporting.ReportServiceImpl

val dataModule = module {
    single<ReportService> { ReportServiceImpl(get()) }

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
    
    single<AuthApi> { KtorAuthApi(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    
    single<AdminRepository> { 
        AdminRepositoryImpl(get(), get(), get(), get(), get(), get(), get())
    }
    
    single<DriverTripRepository> {
        DriverTripRepositoryImpl(get(), get(), get(), get(), get(), get())
    }
}
