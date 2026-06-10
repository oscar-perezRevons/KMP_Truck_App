package truck.project

import org.koin.dsl.module
import truck.project.core.data.remote.RemoteDatabase
import truck.project.core.platform.AndroidFileOpener
import truck.project.core.platform.FileOpener
import truck.project.core.storage.AndroidImageStorage
import truck.project.core.storage.ImageStorage

actual val platformModule = module {
    single { getDatabaseBuilder(get()) }
    single<RemoteDatabase> { AndroidRemoteDatabase() }
    single<ImageStorage> { AndroidImageStorage(get()) }
    single<FileOpener> { AndroidFileOpener(get()) }
    single<Platform> { AndroidPlatform(get()) }
}
