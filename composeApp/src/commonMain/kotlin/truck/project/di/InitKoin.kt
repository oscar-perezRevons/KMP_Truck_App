package truck.project.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import truck.project.platformModule

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        databaseModule,
        dataModule,
        domainModule,
        presentationModule,
        platformModule
    )
}

fun initKoin() = initKoin {}
