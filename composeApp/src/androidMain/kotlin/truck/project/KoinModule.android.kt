package truck.project

import org.koin.dsl.module
import truck.project.data.remote.RemoteDatabase

actual val platformModule = module {
    single { getDatabaseBuilder(get()) }
    single<RemoteDatabase> { AndroidRemoteDatabase() }
}
