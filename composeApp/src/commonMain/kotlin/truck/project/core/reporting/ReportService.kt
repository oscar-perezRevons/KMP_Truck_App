package truck.project.core.reporting

import truck.project.features.fleet.domain.model.Truck
import truck.project.features.fleet.domain.model.Driver
import truck.project.features.trips.data.local.TripEntity

data class ReportResult(
    val filePath: String,
    val summary: String
)

interface ReportService {
    suspend fun generateTripsReport(trips: List<TripEntity>): Result<ReportResult>
    suspend fun generateFleetReport(trucks: List<Truck>): Result<ReportResult>
    suspend fun generateDriversReport(drivers: List<Driver>): Result<ReportResult>
}
