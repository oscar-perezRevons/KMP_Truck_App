package truck.project.features.fleet.domain.model

import truck.project.features.fleet.domain.vo.DriverPin

data class Driver(
    val id: String,
    val name: String,
    val dni: String,
    val licenseNumber: String,
    val pin: DriverPin,
    val isOnline: Boolean = false,
    val photoUrl: String? = null
)
