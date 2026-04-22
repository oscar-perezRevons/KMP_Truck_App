package truck.project.di

import org.koin.dsl.module
import truck.project.data.local.AppDatabase
import truck.project.data.local.getRoomDatabase

val databaseModule = module {
    single { getRoomDatabase(get()) }
    single { get<AppDatabase>().truckDao() }
}
