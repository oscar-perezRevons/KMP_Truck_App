package truck.project.features.admin.presentation.dashboard

import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.admin.domain.model.Admin
import truck.project.features.trips.data.local.TripEntity

data class AdminDashboardState(
    val admin: Admin? = null,
    val truckCount: Int = 0,
    val driverCount: Int = 0,
    val activeTripsCount: Int = 0,
    val todayExpenses: Double = 0.0,
    val trucks: List<Truck> = emptyList(),
    val drivers: List<Driver> = emptyList(),
    val activeTrips: List<TripEntity> = emptyList(),
    val plannedTrips: List<TripEntity> = emptyList(),
    val selectedTripExpenses: List<truck.project.features.driver.domain.model.Expense> = emptyList(),
    val isLoading: Boolean = false,
    val isGeneratingReport: Boolean = false,
    val reportMessage: String? = null,
    val showReportDialog: Boolean = false,
    val reportData: String? = null
)

sealed interface AdminDashboardIntent {
    data object LoadStats : AdminDashboardIntent
    data object NavigateToFleet : AdminDashboardIntent
    data object NavigateToDrivers : AdminDashboardIntent
    data object NavigateToTrips : AdminDashboardIntent
    data object NavigateToProfile : AdminDashboardIntent
    data object Logout : AdminDashboardIntent
    data class DeactivateDriver(val driverId: String) : AdminDashboardIntent
    data class DeleteDriver(val driverId: String) : AdminDashboardIntent
    data class EditDriver(val driver: Driver) : AdminDashboardIntent
    data class DeleteTruck(val truckId: String) : AdminDashboardIntent
    data class EditTruck(val truck: Truck) : AdminDashboardIntent
    data class DeleteTrip(val tripId: String) : AdminDashboardIntent
    data class EditTrip(val trip: TripEntity) : AdminDashboardIntent
    data object GenerateTripsReport : AdminDashboardIntent
    data object GenerateFleetReport : AdminDashboardIntent
    data object GenerateDriversReport : AdminDashboardIntent
    data object DismissReportDialog : AdminDashboardIntent
    data class OpenReport(val path: String) : AdminDashboardIntent
    data class SelectTripForMonitor(val tripId: String) : AdminDashboardIntent
}

sealed interface AdminDashboardEffect {
    data object NavigateToFleet : AdminDashboardEffect
    data object NavigateToDrivers : AdminDashboardEffect
    data object NavigateToTrips : AdminDashboardEffect
    data object NavigateToProfile : AdminDashboardEffect
    data object NavigateToLogin : AdminDashboardEffect
    data class NavigateToEditTruck(val truck: Truck) : AdminDashboardEffect
    data class NavigateToEditDriver(val driver: Driver) : AdminDashboardEffect
    data class NavigateToEditTrip(val trip: TripEntity) : AdminDashboardEffect
}
