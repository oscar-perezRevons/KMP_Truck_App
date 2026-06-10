package truck.project.data.remote

import truck.project.features.fleet.data.local.TruckEntity
import truck.project.features.fleet.data.local.DriverEntity
import truck.project.features.trips.data.local.TripEntity
import truck.project.features.trips.data.local.ExpenseEntity

interface RemoteDatabase {
    // Trucks
    suspend fun saveTruck(truck: TruckEntity)
    suspend fun deleteTruck(truckId: String)
    suspend fun getAllTrucks(): List<TruckEntity>
    
    // Drivers
    suspend fun saveDriver(driver: DriverEntity)
    suspend fun getAllDrivers(): List<DriverEntity>
    
    // Trips
    suspend fun saveTrip(trip: TripEntity)
    suspend fun getActiveTrips(): List<TripEntity>
    
    // Expenses
    suspend fun saveExpense(expense: ExpenseEntity)
    suspend fun getExpensesByTrip(tripId: String): List<ExpenseEntity>
}