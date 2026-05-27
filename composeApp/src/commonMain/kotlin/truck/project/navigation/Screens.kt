package truck.project.navigation

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object TruckList : Screens() {
        const val route = "truck_list"
    }

    @Serializable
    data object Maintenance : Screens() {
        const val route = "maintenance"
    }

    @Serializable
    data object Selection : Screens() {
        const val route = "selection"
    }

    @Serializable
    data object AdminLogin : Screens() {
        const val route = "admin_login"
    }

    @Serializable
    data object AdminDashboard : Screens() {
        const val route = "admin_dashboard"
    }

    @Serializable
    data object AddTruck : Screens() {
        const val route = "add_truck"
    }

    @Serializable
    data object AddDriver : Screens() {
        const val route = "add_driver"
    }

    @Serializable
    data object AdminRegister : Screens() {
        const val route = "admin_register"
    }

    @Serializable
    data object AdminProfile : Screens() {
        const val route = "admin_profile"
    }

    @Serializable
    data object NewRoute : Screens() {
        const val route = "new_route"
    }

    @Serializable
    data object TripMonitor : Screens() {
        const val route = "trip_monitor"
    }

    @Serializable
    data object DriverPin : Screens() {
        const val route = "driver_pin"
    }

    @Serializable
    data object DriverAssignedTrip : Screens() {
        const val route = "driver_assigned_trip"
    }

    @Serializable
    data object DriverDispatch : Screens() {
        const val route = "driver_dispatch"
    }

    @Serializable
    data object DriverEnRoute : Screens() {
        const val route = "driver_en_route"
    }

    @Serializable
    data object DriverCloseTrip : Screens() {
        const val route = "driver_close_trip"
    }

    @Serializable
    data object DriverProfile : Screens() {
        const val route = "driver_profile"
    }
}
