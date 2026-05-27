package truck.project.features.admin.presentation.dashboard

data class AdminDashboardState(
    val truckCount: Int = 0,
    val driverCount: Int = 0,
    val activeTripsCount: Int = 0,
    val isLoading: Boolean = false
)

sealed interface AdminDashboardIntent {
    data object LoadStats : AdminDashboardIntent
    data object NavigateToFleet : AdminDashboardIntent
    data object NavigateToDrivers : AdminDashboardIntent
    data object NavigateToTrips : AdminDashboardIntent
    data object Logout : AdminDashboardIntent
}

sealed interface AdminDashboardEffect {
    data object NavigateToFleet : AdminDashboardEffect
    data object NavigateToDrivers : AdminDashboardEffect
    data object NavigateToTrips : AdminDashboardEffect
    data object NavigateToLogin : AdminDashboardEffect
}
