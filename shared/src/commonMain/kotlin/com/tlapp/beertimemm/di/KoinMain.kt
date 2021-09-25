package com.tlapp.beertimemm.di

import com.tlapp.beertimemm.drinking.DrinkCoordinator
import com.tlapp.beertimemm.drinking.DrinkCoordinatorImpl
import com.tlapp.beertimemm.sqldelight.DatabaseHelper
import com.tlapp.beertimemm.storage.DrinkStorage
import com.tlapp.beertimemm.storage.ProfileStorage
import com.tlapp.beertimemm.viewmodels.CountDownModel
import com.tlapp.beertimemm.viewmodels.ProfileModel
import com.tlapp.beertimemm.viewmodels.StartDrinkingModel
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun initKoin(appModule: Module): KoinApplication {
    val koinApplication = startKoin {
        modules(
            appModule,
            coreModule,
            platformModule
        )
    }

    // Dummy initialization logic, making use of appModule declarations for demonstration purposes.
//    val koin = koinApplication.koin
//    val doOnStartup = koin.get<() -> Unit>() // doOnStartup is a lambda which is implemented in Swift on iOS side
//    doOnStartup.invoke()

    return koinApplication
}


internal inline fun <reified T> Scope.getWith(vararg params: Any?): T {
    return get(parameters = { parametersOf(*params) })
}

@ExperimentalTime
@ExperimentalSerializationApi
private val coreModule = module {
    single {
        DatabaseHelper(
            get(),
            Dispatchers.Default
        )
    }

    single<DrinkCoordinator> {
        DrinkCoordinatorImpl()
    }

    single {
        DrinkStorage(settings = get())
    }

    single {
        ProfileStorage(settings = get())
    }

    factory<Clock> {
        Clock.System
    }

    factory {
        StartDrinkingModel()
    }

    factory {
        ProfileModel()
    }

    factory {
        CountDownModel()
    }
}

expect val platformModule: Module
