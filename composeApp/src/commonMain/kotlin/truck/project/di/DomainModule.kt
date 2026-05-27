package truck.project.di

import truck.project.features.fleet.domain.usecase.*
import truck.project.features.admin.domain.usecase.*
import truck.project.features.driver.domain.usecase.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    // Fleet Use Cases
    factoryOf(::GetTrucksUseCase)
    factoryOf(::SaveTruckUseCase)
    factoryOf(::UpdateTruckUseCase)
    factoryOf(::DeleteTruckUseCase)
    factoryOf(::SyncTrucksUseCase)
    
    // Admin Use Cases
    factoryOf(::LoginAdminUseCase)
    factoryOf(::GetAdminStatsUseCase)
    factoryOf(::AddTruckUseCase)
    factoryOf(::AddDriverUseCase)

    // Driver Use Cases
    factoryOf(::DriverLoginUseCase)
    factoryOf(::GetAssignedTripUseCase)
    factoryOf(::StartTripUseCase)
    factoryOf(::RegisterExpenseUseCase)
    factoryOf(::FinishTripUseCase)
}
