package com.tlapp.beertimemm.di

import com.tlapp.beertimemm.sqldelight.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

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

private val coreModule = module {
    single {
        DatabaseHelper(
            get(),
            Dispatchers.Default
        )
    }
}

expect val platformModule: Module
