package com.tlapp.beertimemm.di

import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.tlapp.beertimemm.utils.CountDownClock
import com.tlapp.beertimemm.utils.CountDownClockImpl
import com.tlapp.beertimemm.utils.DisplayDateHelper
import com.tlapp.beertimemm.utils.DisplayDateHelperImpl
import drinkdb.Drink
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
actual val platformModule: Module = module {

    single<SqlDriver> {
        AndroidSqliteDriver(
            Drink.Schema,
            get(),
            "DrinkDb"
        )
    }

    single<Settings> {
        AndroidSettings(delegate = get())
    }

    factory<DisplayDateHelper> {
        DisplayDateHelperImpl()
    }

    factory<CountDownClock> {
        CountDownClockImpl()
    }
}
