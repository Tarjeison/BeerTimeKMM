package com.tlapp.beertimemm.di

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.tlapp.beertimemm.utils.DisplayDateHelper
import com.tlapp.beertimemm.utils.DisplayDateHelperImpl
import drinkdb.Drink
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults
import kotlin.time.ExperimentalTime


@ExperimentalTime
fun initKoinIos(
    userDefaults: NSUserDefaults
): KoinApplication = initKoin(
    module {
        single<Settings> { AppleSettings(userDefaults) }
    }
)

actual val platformModule: Module = module {
    single<SqlDriver> {
        NativeSqliteDriver(
            Drink.Schema,
            "DrinkDb"
        )
    }

    single<DisplayDateHelper> { DisplayDateHelperImpl() }
}
