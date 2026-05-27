package truck.project.truck_guia.data.mapper

import truck.project.data.local.TruckEntity
import truck.project.truck_guia.domain.model.Truck
import truck.project.truck_guia.domain.vo.Placa

fun TruckEntity.toDomain(): Truck {
    return Truck(
        id = id,
        licensePlate = Placa.create(licensePlate),
        model = model,
        status = status,
        statusTranslated = statusTranslated,
        needsTranslation = needsTranslation
    )
}

fun Truck.toEntity(): TruckEntity {
    return TruckEntity(
        id = id,
        licensePlate = licensePlate.value,
        model = model,
        status = status,
        statusTranslated = statusTranslated,
        needsTranslation = needsTranslation
    )
}
