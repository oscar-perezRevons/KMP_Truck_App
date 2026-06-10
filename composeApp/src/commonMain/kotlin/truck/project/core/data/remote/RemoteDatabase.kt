package truck.project.core.data.remote

import truck.project.features.fleet.data.local.TruckEntity
import truck.project.features.fleet.data.local.DriverEntity
import truck.project.features.trips.data.local.TripEntity
import truck.project.features.trips.data.local.ExpenseEntity
import truck.project.features.admin.data.local.AdminEntity
import kotlinx.coroutines.flow.Flow

interface RemoteDatabase {
    // Admins
    suspend fun saveAdmin(admin: AdminEntity)
    suspend fun getAdmins(): List<AdminEntity>

    // Trucks
    suspend fun saveTruck(truck: TruckEntity)
    suspend fun deleteTruck(adminId: String, truckId: String)
    suspend fun getAllTrucks(adminId: String): List<TruckEntity>
    
    // Drivers
    suspend fun saveDriver(driver: DriverEntity)
    suspend fun deleteDriver(adminId: String, driverId: String)
    suspend fun getAllDrivers(adminId: String): List<DriverEntity>
    suspend fun findDriverGlobally(email: String): DriverEntity?
    
    // Trips
    suspend fun saveTrip(trip: TripEntity)
    suspend fun getActiveTrips(adminId: String): List<TripEntity>
    
    // Expenses
    suspend fun saveExpense(expense: ExpenseEntity)
    suspend fun deleteExpense(adminId: String, tripId: String, expenseId: String)
    suspend fun getExpensesByTrip(adminId: String, tripId: String): List<ExpenseEntity>

    // Real-time synchronization listeners
    fun observeTrucks(adminId: String): Flow<List<TruckEntity>>
    fun observeTrips(adminId: String): Flow<List<TripEntity>>
}
