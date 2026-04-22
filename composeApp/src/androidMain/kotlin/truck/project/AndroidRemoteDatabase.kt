package truck.project

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import truck.project.data.local.TruckEntity
import truck.project.data.remote.RemoteDatabase

class AndroidRemoteDatabase : RemoteDatabase {
    private val database = FirebaseDatabase.getInstance().getReference("trucks")

    override suspend fun saveTruck(truck: TruckEntity) {
        val truckKey = truck.id.toString()
        database.child(truckKey).setValue(truck).await()
    }

    override suspend fun deleteTruck(truckId: String) {
        database.child(truckId).removeValue().await()
    }

    override suspend fun getAllTrucks(): List<TruckEntity> {
        return try {
            val snapshot = database.get().await()
            snapshot.children.mapNotNull { it.getValue(TruckEntity::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
