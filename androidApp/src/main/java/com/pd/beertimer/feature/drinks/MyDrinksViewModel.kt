package com.pd.beertimer.feature.drinks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tlapp.beertimemm.models.MyDrinkItem
import com.tlapp.beertimemm.viewmodels.MyDrinksModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyDrinksViewModel(
    private val myDrinksModel: MyDrinksModel,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    val drinksLiveData: LiveData<List<MyDrinkItem>> = myDrinksModel.getDrinks().asLiveData()

    fun deleteDrink(drinkKey: Long) {
        viewModelScope.launch(dispatcher) {
            myDrinksModel.deleteDrink(drinkKey)
        }
    }
}
