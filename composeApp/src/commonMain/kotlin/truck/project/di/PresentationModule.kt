package truck.project.di

import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import truck.project.truck_guia.presentation.viewmodel.TruckViewModel

val presentationModule = module {
    viewModelOf(::TruckViewModel)
}
