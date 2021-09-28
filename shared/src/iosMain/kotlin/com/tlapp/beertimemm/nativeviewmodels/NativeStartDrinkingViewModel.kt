package com.tlapp.beertimemm.nativeviewmodels

import co.touchlab.stately.ensureNeverFrozen
import com.tlapp.beertimemm.models.AlcoholUnit
import com.tlapp.beertimemm.models.AlertDialogUiModel
import com.tlapp.beertimemm.viewmodels.StartDrinkingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.time.ExperimentalTime

@ExperimentalSerializationApi
@ExperimentalTime
class NativeStartDrinkingViewModel(
    private val onWantedBloodLevelTextChanged: (String) -> Unit,
    private val onDrinkUntilDisplayTextChanged: (String) -> Unit,
    private val onPeakHourDisplayTextChanged: (String) -> Unit,
    private val onDrinkListChanged: (List<AlcoholUnit>) -> Unit,
    private val onNewToastMessage: (String) -> Unit,
    private val onAlertDialogMessage: (AlertDialogUiModel) -> Unit
) {

    private val startDrinkingModel = StartDrinkingModel()
    private var scope: CoroutineScope? = null

    init {
        ensureNeverFrozen()
    }

    fun observeUpdates() {
        scope = MainScope().also { coroutineScope ->
            coroutineScope.launch {
                startDrinkingModel.wantedBloodLevelUiModelFlow.filterNotNull().onEach {
                    onWantedBloodLevelTextChanged.invoke(it)
                }.launchIn(this)

                startDrinkingModel.finishDrinkingSeekBarUiModelFlow.filterNotNull().onEach {
                    onDrinkUntilDisplayTextChanged.invoke(it)
                }.launchIn(this)

                startDrinkingModel.peakTimeSeekBarUiModelFlow.filterNotNull().onEach {
                    onPeakHourDisplayTextChanged.invoke(it)
                }.launchIn(this)

                startDrinkingModel.getDrinks().filterNotNull().onEach {
                    onDrinkListChanged.invoke(it)
                }.launchIn(this)

                startDrinkingModel.errorToastFlow.filterNotNull().onEach {
                    onNewToastMessage(it.value)
                }.launchIn(this)

                startDrinkingModel.alertFlow.filterNotNull().onEach {
                    onAlertDialogMessage(it.value)
                }.launchIn(this)
            }
        }
    }

    fun updateSelectedUnit(alcoholUnit: AlcoholUnit) {
        startDrinkingModel.setSelectedUnit(alcoholUnit)
    }

    fun updatedSelectedBloodLevel(seekbarValue: Int) {
        startDrinkingModel.setWantedBloodLevel(seekbarValue)
    }

    fun updateFinishDrinking(seekbarValue: Int) {
        startDrinkingModel.setFinishDrinkingInHoursMinutes(seekbarValue)
    }

    fun updatePeakHour(seekbarValue: Int) {
        startDrinkingModel.setPeakTimeInHoursMinutes(seekbarValue)
    }

    fun startDrinking() {
        startDrinkingModel.startDrinking()
    }

    fun onDestroy() {
        scope?.cancel()
        scope = null
    }
}
