package com.tlapp.beertimemm.sqldelight

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.tlapp.beertimemm.models.AlcoholUnit
import com.tlapp.beertimemm.models.MyDrinkItem
import drinkdb.Drink
import drinkdb.DrinkEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DatabaseHelper(
    sqlDriver: SqlDriver,
    private val backgroundDispatcher: CoroutineDispatcher
) {
    private val dbRef: Drink = Drink(sqlDriver)

    fun selectAllItems(): Flow<List<DrinkEntity>> =
        dbRef.drinkQueries
            .selectAll()
            .asFlow()
            .mapToList()
            .flowOn(backgroundDispatcher)

    suspend fun insertDrink(name: String, percentage: Float, volume: Float, iconName: String) {
        dbRef.drinkQueries.insertItem(
            key = null,
            name = name,
            percentage = percentage,
            volume = volume,
            icon_name = iconName
        )
    }

    suspend fun deleteDrink(drinkId: Long) {
        dbRef.drinkQueries.delete(drinkId)
    }
}

fun DrinkEntity.toAlcoholUnit(): AlcoholUnit {
    return AlcoholUnit(
        name = name,
        volume = volume,
        percentage = percentage,
        iconName = icon_name
    )
}

fun DrinkEntity.toDrinkItem(): MyDrinkItem {
    return MyDrinkItem(
        name = name,
        volume = volume,
        percentage = percentage,
        iconName = icon_name,
        key = key
    )
}