package com.tlapp.beertimemm.nativeviewmodels

import co.touchlab.stately.ensureNeverFrozen
import com.tlapp.beertimemm.viewmodels.StartDrinkingModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
class NativeStartDrinkingViewModel(
    private val onWantedBloodLevelTextChanged: (String) -> Unit,
    private val onDrinkUntilDisplayTextChanged: (String) -> Unit
) {

    private val startDrinkingModel = StartDrinkingModel()
    private val scope = MainScope()

    init {
        ensureNeverFrozen()
    }

    fun observeUpdates() {
        scope.launch {
            startDrinkingModel.wantedBloodLevelUiModelFlow.filterNotNull().onEach {
                onWantedBloodLevelTextChanged.invoke(it)
            }.launchIn(this)

            startDrinkingModel.finishDrinkingSeekBarUiModelFlow.filterNotNull().onEach {
                onDrinkUntilDisplayTextChanged.invoke(it)
            }.launchIn(this)
        }
    }

    fun updatedSelectedBloodLevel(seekbarValue: Int) {
        startDrinkingModel.setWantedBloodLevel(seekbarValue)
    }

    fun updateFinishDrinking(seekbarValue: Int) {
        startDrinkingModel.setFinishDrinkingInHoursMinutes(seekbarValue)
    }

    fun onDestroy() {
        scope.cancel()
    }
}
