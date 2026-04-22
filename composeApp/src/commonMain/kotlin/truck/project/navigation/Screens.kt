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
}
