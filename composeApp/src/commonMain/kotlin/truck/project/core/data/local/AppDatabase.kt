package truck.project.core.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import truck.project.features.fleet.data.local.TruckDao
import truck.project.features.fleet.data.local.TruckEntity
import truck.project.features.fleet.data.local.DriverDao
import truck.project.features.fleet.data.local.DriverEntity
import truck.project.features.trips.data.local.TripDao
import truck.project.features.trips.data.local.TripEntity
import truck.project.features.trips.data.local.ExpenseDao
import truck.project.features.trips.data.local.ExpenseEntity
import truck.project.features.admin.data.local.AdminDao
import truck.project.features.admin.data.local.AdminEntity

@Database(
    entities = [
        TruckEntity::class,
        DriverEntity::class,
        TripEntity::class,
        ExpenseEntity::class,
        AdminEntity::class
    ],
    version = 15
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun truckDao(): TruckDao
    abstract fun driverDao(): DriverDao
    abstract fun tripDao(): TripDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun adminDao(): AdminDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .fallbackToDestructiveMigration(true)
        .build()
}
