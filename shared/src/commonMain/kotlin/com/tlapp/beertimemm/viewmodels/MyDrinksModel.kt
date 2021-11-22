package com.tlapp.beertimemm.viewmodels

import com.tlapp.beertimemm.models.MyDrinkItem
import com.tlapp.beertimemm.sqldelight.DatabaseHelper
import com.tlapp.beertimemm.sqldelight.toDrinkItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyDrinksModel : KoinComponent {

    private val databaseHelper: DatabaseHelper by inject()

    fun getDrinks(): Flow<List<MyDrinkItem>> {
        return databaseHelper.selectAllItems().map { drinkList ->
            drinkList.map { it.toDrinkItem() }
        }
    }

    suspend fun deleteDrink(drinkKey: Long) {
        databaseHelper.deleteDrink(drinkKey)
    }
}