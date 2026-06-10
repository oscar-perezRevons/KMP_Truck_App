package truck.project.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import truck.project.features.admin.presentation.dashboard.AdminDashboardViewModel
import truck.project.features.auth.presentation.login.AdminLoginViewModel
import truck.project.features.auth.presentation.selection.SelectionViewModel
import truck.project.features.fleet.presentation.viewmodel.TruckViewModel
import truck.project.features.admin.presentation.forms.NewTruckViewModel
import truck.project.features.admin.presentation.forms.NewDriverViewModel
import truck.project.features.admin.presentation.forms.NewRouteViewModel
import truck.project.features.admin.presentation.profile.AdminProfileViewModel
import truck.project.features.driver.presentation.trip.DriverTripViewModel
import truck.project.features.auth.presentation.register.AdminRegisterViewModel

val presentationModule = module {
    viewModelOf(::TruckViewModel)
    viewModelOf(::SelectionViewModel)
    viewModelOf(::AdminLoginViewModel)
    viewModelOf(::AdminDashboardViewModel)
    viewModelOf(::NewTruckViewModel)
    viewModelOf(::NewDriverViewModel)
    viewModelOf(::NewRouteViewModel)
    viewModelOf(::DriverTripViewModel)
    viewModelOf(::AdminRegisterViewModel)
    viewModelOf(::AdminProfileViewModel)
}
