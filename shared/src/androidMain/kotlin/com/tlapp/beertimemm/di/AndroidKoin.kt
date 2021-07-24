package com.tlapp.beertimemm.di

import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import drinkdb.Drink
import org.koin.core.module.Module
import org.koin.dsl.module

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
}
