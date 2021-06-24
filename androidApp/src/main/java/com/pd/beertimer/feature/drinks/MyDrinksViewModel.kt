package com.pd.beertimer.feature.drinks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tlapp.beertimemm.models.MyDrinkItem
import com.tlapp.beertimemm.sqldelight.DatabaseHelper
import com.tlapp.beertimemm.sqldelight.toDrinkItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MyDrinksViewModel(private val databaseHelper: DatabaseHelper): ViewModel() {

    private val _drinksLiveData = MutableLiveData<List<MyDrinkItem>>()
    val drinksLiveData: LiveData<List<MyDrinkItem>> = _drinksLiveData

    init {
        databaseHelper.selectAllItems().onEach { drinkList ->
            _drinksLiveData.postValue(drinkList.map { it.toDrinkItem() })
        }.launchIn(viewModelScope)
    }

    fun deleteDrink(drinkKey: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseHelper.deleteDrink(drinkKey)
        }
    }
}
