package truck.project.di

import org.koin.dsl.module
import truck.project.core.data.local.AppDatabase
import truck.project.core.data.local.getRoomDatabase

val databaseModule = module {
    single { getRoomDatabase(get()) }
    single { get<AppDatabase>().truckDao() }
    single { get<AppDatabase>().driverDao() }
    single { get<AppDatabase>().tripDao() }
    single { get<AppDatabase>().expenseDao() }
    single { get<AppDatabase>().adminDao() }
}
