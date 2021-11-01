package com.tlapp.beertimemm.nativeviewmodels

import com.tlapp.beertimemm.models.MyDrinkItem
import com.tlapp.beertimemm.viewmodels.MyDrinksModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NativeMyDrinksViewModel(
    private val onDrinksUpdated: (List<MyDrinkItem>) -> Unit
): KoinComponent {

    private val myDrinksModel: MyDrinksModel by inject()
    private var scope: CoroutineScope? = null

    fun observeDrinks() {
        scope = MainScope().also {
            myDrinksModel.getDrinks().onEach { myDrinkList ->
                onDrinksUpdated.invoke(myDrinkList)
            }.launchIn(it)
        }
    }

    fun deleteDrink(drinkKey: Long) {
        if (scope == null) scope = MainScope()
        scope?.launch {
            myDrinksModel.deleteDrink(drinkKey)
        }
    }

    fun onDestroy() {
        scope?.cancel()
        scope = null
    }
}
