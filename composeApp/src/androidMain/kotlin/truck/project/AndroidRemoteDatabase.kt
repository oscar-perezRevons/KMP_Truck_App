package truck.project

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import truck.project.core.data.remote.RemoteDatabase
import truck.project.features.fleet.data.local.TruckEntity
import truck.project.features.fleet.data.local.DriverEntity
import truck.project.features.trips.data.local.TripEntity
import truck.project.features.trips.data.local.ExpenseEntity

class AndroidRemoteDatabase : RemoteDatabase {
    private val db = FirebaseDatabase.getInstance()
    private val trucksRef = db.getReference("trucks")
    private val driversRef = db.getReference("drivers")
    private val tripsRef = db.getReference("trips")
    private val expensesRef = db.getReference("expenses")

    // Trucks
    override suspend fun saveTruck(truck: TruckEntity) {
        trucksRef.child(truck.id).setValue(truck).await()
    }

    override suspend fun deleteTruck(truckId: String) {
        trucksRef.child(truckId).removeValue().await()
    }

    override suspend fun getAllTrucks(): List<TruckEntity> = try {
        trucksRef.get().await().children.mapNotNull { it.getValue(TruckEntity::class.java) }
    } catch (e: Exception) {
        emptyList()
    }

    // Drivers
    override suspend fun saveDriver(driver: DriverEntity) {
        driversRef.child(driver.id.toString()).setValue(driver).await()
    }

    override suspend fun getAllDrivers(): List<DriverEntity> = try {
        driversRef.get().await().children.mapNotNull { it.getValue(DriverEntity::class.java) }
    } catch (e: Exception) {
        emptyList()
    }

    // Trips
    override suspend fun saveTrip(trip: TripEntity) {
        tripsRef.child(trip.id.toString()).setValue(trip).await()
    }

    override suspend fun getActiveTrips(): List<TripEntity> = try {
        tripsRef.orderByChild("status").equalTo("EN_ROUTE").get().await()
            .children.mapNotNull { it.getValue(TripEntity::class.java) }
    } catch (e: Exception) {
        emptyList()
    }

    // Expenses
    override suspend fun saveExpense(expense: ExpenseEntity) {
        expensesRef.child(expense.id.toString()).setValue(expense).await()
    }

    override suspend fun getExpensesByTrip(tripId: String): List<ExpenseEntity> = try {
        expensesRef.orderByChild("tripId").equalTo(tripId).get().await()
            .children.mapNotNull { it.getValue(ExpenseEntity::class.java) }
    } catch (e: Exception) {
        emptyList()
    }
}
