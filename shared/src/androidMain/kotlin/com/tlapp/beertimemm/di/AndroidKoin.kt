package com.tlapp.beertimemm.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.tlapp.beertimemm.storage.ProfileStorage
import drinkdb.Drink
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    factory {
        ProfileStorage(sharedPreferences = get())
    }

    single<SqlDriver> {
        AndroidSqliteDriver(
            Drink.Schema,
            get(),
            "DrinkDb"
        )
    }
}
