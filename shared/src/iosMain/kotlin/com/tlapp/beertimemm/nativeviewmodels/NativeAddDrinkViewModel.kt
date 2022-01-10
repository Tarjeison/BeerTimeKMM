package com.tlapp.beertimemm.nativeviewmodels

import co.touchlab.stately.ensureNeverFrozen
import com.tlapp.beertimemm.utils.Failure
import com.tlapp.beertimemm.utils.Success
import com.tlapp.beertimemm.viewmodels.AddDrinkInputField
import com.tlapp.beertimemm.viewmodels.AddDrinkModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NativeAddDrinkViewModel(
    private val onNameError: (String) -> Unit,
    private val onPercentageError: (String) -> Unit,
    private val onVolumeError: (String) -> Unit,
    private val onSuccess: () -> Unit
): KoinComponent {
    private val addDrinkModel: AddDrinkModel by inject()
    private var scope: CoroutineScope? = null

    init {
        ensureNeverFrozen()
    }

    fun addDrink(
        drinkName: String?,
        drinkPercentage: String?,
        drinkVolume: String?,
        drinkIconTag: Int?
    ) {
        addDrinkModel.addDrink(drinkName, drinkPercentage, drinkVolume, drinkIconTag)
    }

    fun getDrinkIcons() = addDrinkModel.getDrinkIcons()

    fun observeDrinkResultsFlow() {
        scope = MainScope().apply {
            addDrinkModel.addDrinkResultFlow.onEach {
                when (it) {
                    is Success -> onSuccess.invoke()
                    is Failure -> {
                        when (it.reason.first) {
                            AddDrinkInputField.DRINK_NAME -> onNameError.invoke(it.reason.second)
                            AddDrinkInputField.DRINK_VOLUME -> onVolumeError.invoke(it.reason.second)
                            AddDrinkInputField.DRINK_PERCENTAGE -> onPercentageError.invoke(it.reason.second)
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    fun onDestroy() {
        scope?.cancel()
        scope = null
    }
}
