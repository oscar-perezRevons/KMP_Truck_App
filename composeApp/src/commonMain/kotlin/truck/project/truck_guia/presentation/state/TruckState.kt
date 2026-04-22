package truck.project.truck_guia.presentation.state

import truck.project.truck_guia.domain.model.Truck

data class TruckState(
    val trucks: List<Truck> = emptyList(),
    val licensePlate: String = "",
    val model: String = "",
    val editingTruck: Truck? = null,
    val isLoading: Boolean = false
)
