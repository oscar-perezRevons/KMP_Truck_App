package truck.project

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import truck.project.core.data.remote.RemoteDatabase
import truck.project.features.admin.data.local.AdminEntity
import truck.project.features.fleet.data.local.TruckEntity
import truck.project.features.fleet.data.local.DriverEntity
import truck.project.features.trips.data.local.TripEntity
import truck.project.features.trips.data.local.ExpenseEntity

class AndroidRemoteDatabase : RemoteDatabase {
    private val db = FirebaseDatabase.getInstance()
    private val adminsRef = db.getReference("admins")
    private val companiesRef = db.getReference("companies")

    // Admins
    override suspend fun saveAdmin(admin: AdminEntity) {
        adminsRef.child(admin.id).setValue(admin).await()
    }

    override suspend fun getAdmins(): List<AdminEntity> = try {
        adminsRef.get().await().children.mapNotNull { it.getValue(AdminEntity::class.java) }
    } catch (e: Exception) {
        emptyList()
    }

    // Trucks
    override suspend fun saveTruck(truck: TruckEntity) {
        companiesRef.child(truck.adminId).child("trucks").child(truck.id).setValue(truck).await()
    }

    override suspend fun deleteTruck(adminId: String, truckId: String) {
        companiesRef.child(adminId).child("trucks").child(truckId).removeValue().await()
    }

    override suspend fun getAllTrucks(adminId: String): List<TruckEntity> = try {
        companiesRef.child(adminId).child("trucks").get().await().children.mapNotNull { it.getValue(TruckEntity::class.java) }
    } catch (e: Exception) {
        emptyList()
    }

    // Drivers
    override suspend fun saveDriver(driver: DriverEntity) {
        companiesRef.child(driver.adminId).child("drivers").child(driver.id).setValue(driver).await()
    }

    override suspend fun deleteDriver(adminId: String, driverId: String) {
        companiesRef.child(adminId).child("drivers").child(driverId).removeValue().await()
    }

    override suspend fun getAllDrivers(adminId: String): List<DriverEntity> = try {
        companiesRef.child(adminId).child("drivers").get().await().children.mapNotNull { it.getValue(DriverEntity::class.java) }
    } catch (e: Exception) {
        emptyList()
    }

    override suspend fun findDriverGlobally(email: String): DriverEntity? = try {
        // This is a bit heavy but works for small-medium number of companies
        // A better way would be an index of email -> {adminId, driverId}
        val snapshot = companiesRef.get().await()
        var found: DriverEntity? = null
        for (companySnap in snapshot.children) {
            val driversSnap = companySnap.child("drivers")
            for (driverSnap in driversSnap.children) {
                val driver = driverSnap.getValue(DriverEntity::class.java)
                if (driver?.email == email) {
                    found = driver
                    break
                }
            }
            if (found != null) break
        }
        found
    } catch (e: Exception) {
        null
    }

    // Trips
    override suspend fun saveTrip(trip: TripEntity) {
        companiesRef.child(trip.adminId).child("trips").child(trip.id).setValue(trip).await()
    }

    override suspend fun getActiveTrips(adminId: String): List<TripEntity> = try {
        companiesRef.child(adminId).child("trips").get().await().children
            .mapNotNull { it.getValue(TripEntity::class.java) }
            .filter { it.status == "EN_ROUTE" }
    } catch (e: Exception) {
        emptyList()
    }

    // Expenses
    override suspend fun saveExpense(expense: ExpenseEntity) {
        companiesRef.child(expense.adminId).child("expenses").child(expense.id.toString()).setValue(expense).await()
    }

    override suspend fun deleteExpense(adminId: String, tripId: String, expenseId: String) {
        companiesRef.child(adminId).child("expenses").child(expenseId).removeValue().await()
    }

    override suspend fun getExpensesByTrip(adminId: String, tripId: String): List<ExpenseEntity> = try {
        companiesRef.child(adminId).child("expenses").get().await().children
            .mapNotNull { it.getValue(ExpenseEntity::class.java) }
            .filter { it.tripId == tripId }
    } catch (e: Exception) {
        emptyList()
    }

    // Real-time synchronization listeners
    override fun observeTrucks(adminId: String): Flow<List<TruckEntity>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trucks = snapshot.children.mapNotNull { it.getValue(TruckEntity::class.java) }
                trySend(trucks)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = companiesRef.child(adminId).child("trucks")
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override fun observeTrips(adminId: String): Flow<List<TripEntity>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trips = snapshot.children.mapNotNull { it.getValue(TripEntity::class.java) }
                trySend(trips)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val ref = companiesRef.child(adminId).child("trips")
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
}
