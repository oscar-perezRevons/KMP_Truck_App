package truck.project.features.fleet.domain.model

import truck.project.features.fleet.domain.vo.DriverPin

data class Driver(
    val id: String,
    val adminId: String = "",
    val name: String,
    val dni: String,
    val licenseNumber: String,
    val pin: DriverPin? = null,
    val email: String? = null,
    val password: String? = null,
    val isOnline: Boolean = false,
    val isActive: Boolean = true,
    val photoUrl: String? = null,
    val photoUrls: List<String> = emptyList()
)
