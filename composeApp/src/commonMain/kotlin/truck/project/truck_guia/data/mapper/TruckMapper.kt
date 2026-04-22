package truck.project.truck_guia.data.mapper

import truck.project.data.local.TruckEntity
import truck.project.truck_guia.domain.model.Truck

fun TruckEntity.toDomain(): Truck {
    return Truck(
        id = id,
        licensePlate = licensePlate,
        model = model,
        status = status,
        statusTranslated = statusTranslated,
        needsTranslation = needsTranslation
    )
}

fun Truck.toEntity(): TruckEntity {
    return TruckEntity(
        id = id,
        licensePlate = licensePlate,
        model = model,
        status = status,
        statusTranslated = statusTranslated,
        needsTranslation = needsTranslation
    )
}
