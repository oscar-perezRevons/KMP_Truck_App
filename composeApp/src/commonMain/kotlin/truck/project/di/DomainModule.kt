package truck.project.di

import truck.project.truck_guia.domain.usecase.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetTrucksUseCase)
    factoryOf(::SaveTruckUseCase)
    factoryOf(::UpdateTruckUseCase)
    factoryOf(::DeleteTruckUseCase)
    factoryOf(::SyncTrucksUseCase)
}
