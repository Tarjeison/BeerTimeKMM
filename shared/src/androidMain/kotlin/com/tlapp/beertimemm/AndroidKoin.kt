package com.tlapp.beertimemm

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
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
