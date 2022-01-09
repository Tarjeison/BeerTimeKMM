package com.pd.beertimer.feature.drinks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tlapp.beertimemm.models.DrinkIconItem
import com.tlapp.beertimemm.utils.Result
import com.tlapp.beertimemm.viewmodels.AddDrinkInputField
import com.tlapp.beertimemm.viewmodels.AddDrinkModel
import kotlinx.coroutines.launch

class AddDrinkViewModel(
    private val addDrinkModel: AddDrinkModel
) : ViewModel() {

    val addDrinkResultLiveData: LiveData<Result<String, Pair<AddDrinkInputField, String>>> = addDrinkModel.addDrinkResultFlow.asLiveData()

    fun getDrinkIcons(): List<DrinkIconItem> {
        return addDrinkModel.getDrinkIcons()
    }

    fun addDrink(
        drinkName: String?,
        drinkPercentage: String?,
        drinkVolume: String?,
        drinkIconName: String?
    ) {
        viewModelScope.launch {
            addDrinkModel.addDrink(drinkName, drinkPercentage, drinkVolume, drinkIconName)
        }
    }
}
