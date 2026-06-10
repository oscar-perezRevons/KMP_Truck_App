package truck.project.features.fleet.data.mapper

import truck.project.features.fleet.domain.model.Driver
import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.model.TruckStatus
import truck.project.features.fleet.domain.vo.DriverPin
import truck.project.features.fleet.domain.vo.PlateNumber
import truck.project.features.fleet.data.local.DriverEntity
import truck.project.features.fleet.data.local.TruckEntity

fun TruckEntity.toDomain() = Truck(
    id = id,
    adminId = adminId,
    plateNumber = PlateNumber(plateNumber),
    model = model,
    origin = origin,
    capacity = capacity,
    imageUrl = imageUrl,
    imageUrls = imageUrls?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
    status = TruckStatus.valueOf(status),
    statusTranslated = statusTranslated,
    needsTranslation = needsTranslation
)

fun Truck.toEntity() = TruckEntity(
    id = id,
    adminId = adminId,
    plateNumber = plateNumber.value,
    model = model,
    origin = origin,
    capacity = capacity,
    imageUrl = imageUrl,
    imageUrls = if (imageUrls.isEmpty()) null else imageUrls.joinToString(","),
    status = status.name,
    statusTranslated = statusTranslated,
    needsTranslation = needsTranslation
)

fun DriverEntity.toDomain() = Driver(
    id = id,
    adminId = adminId,
    name = fullName,
    dni = dni,
    licenseNumber = licenseNumber,
    pin = pin?.let { DriverPin(it) },
    email = email,
    password = password,
    isOnline = isActive,
    photoUrl = photoUrl,
    photoUrls = photoUrls?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
)

fun Driver.toEntity() = DriverEntity(
    id = id,
    adminId = adminId,
    fullName = name,
    dni = dni,
    licenseNumber = licenseNumber,
    pin = pin?.value,
    email = email,
    password = password,
    isActive = isOnline,
    photoUrl = photoUrl,
    photoUrls = if (photoUrls.isEmpty()) null else photoUrls.joinToString(",")
)
