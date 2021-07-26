package com.tlapp.beertimemm

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import drinkdb.Drink

actual fun createTestSqlDriver(): SqlDriver {
    val app = ApplicationProvider.getApplicationContext<Application>()
    return AndroidSqliteDriver(Drink.Schema, app, "drinkdb")
}
