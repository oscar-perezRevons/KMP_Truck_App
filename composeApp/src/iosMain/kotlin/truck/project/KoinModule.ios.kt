package truck.project

import org.koin.dsl.module

actual val platformModule = module {
    single { getDatabaseBuilder() }
}
