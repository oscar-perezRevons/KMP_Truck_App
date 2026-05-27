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
    plateNumber = PlateNumber(plateNumber),
    model = model,
    capacity = capacity,
    imageUrl = imageUrl,
    status = TruckStatus.valueOf(status),
    statusTranslated = statusTranslated,
    needsTranslation = needsTranslation
)

fun Truck.toEntity() = TruckEntity(
    id = id,
    plateNumber = plateNumber.value,
    model = model,
    capacity = capacity,
    imageUrl = imageUrl,
    status = status.name,
    statusTranslated = statusTranslated,
    needsTranslation = needsTranslation
)

fun DriverEntity.toDomain() = Driver(
    id = id.toString(),
    name = fullName,
    dni = dni,
    licenseNumber = licenseNumber,
    pin = DriverPin(pin),
    isOnline = isActive,
    photoUrl = photoUrl
)

fun Driver.toEntity() = DriverEntity(
    id = id.toLongOrNull() ?: 0L,
    fullName = name,
    dni = dni,
    licenseNumber = licenseNumber,
    pin = pin.value,
    isActive = isOnline,
    photoUrl = photoUrl
)
