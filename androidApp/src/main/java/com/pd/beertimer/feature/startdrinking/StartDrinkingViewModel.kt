package com.pd.beertimer.feature.startdrinking

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pd.beertimer.util.SingleLiveEvent
import com.tlapp.beertimemm.models.AlcoholUnit
import com.tlapp.beertimemm.models.AlertDialogUiModel
import com.tlapp.beertimemm.viewmodels.StartDrinkingModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StartDrinkingViewModel : ViewModel(), KoinComponent {

    private val startDrinkingModel: StartDrinkingModel by inject()

    init {
        startDrinkingModel.errorToastFlow.filterNotNull().onEach {
            toastLiveData.postValue(it.value)
        }.launchIn(viewModelScope)

        startDrinkingModel.alertFlow.filterNotNull().onEach {
            alertDialogLiveData.postValue(it.value)
        }.launchIn(viewModelScope)

        startDrinkingModel.navigateToCountDownFlow.filterNotNull().onEach {
            navigationLiveData.call()
        }.launchIn(viewModelScope)
    }

    val drinksLiveData: LiveData<List<AlcoholUnit>>
        get() = startDrinkingModel.getDrinks().asLiveData(viewModelScope.coroutineContext)

    val peakHourLiveData: LiveData<String>
        get() = startDrinkingModel.peakTimeSeekBarUiModelFlow.filterNotNull().asLiveData(viewModelScope.coroutineContext)

    val finishBarLiveData: LiveData<String>
        get() =
            startDrinkingModel.finishDrinkingSeekBarUiModelFlow.filterNotNull().asLiveData(viewModelScope.coroutineContext)

    val wantedBloodLevelLiveData: LiveData<String>
        get() =
            startDrinkingModel.wantedBloodLevelUiModelFlow.filterNotNull().asLiveData(viewModelScope.coroutineContext)

    val toastLiveData = SingleLiveEvent<String>()

    val alertDialogLiveData = SingleLiveEvent<AlertDialogUiModel>()

    val navigationLiveData = SingleLiveEvent<Any>()

    fun setWantedBloodLevel(wantedBloodLevelProgress: Int) {
        startDrinkingModel.setWantedBloodLevel(wantedBloodLevelProgress)
    }

    fun setFinishDrinkingInHoursMinutes(seekBarValue: Int) {
        startDrinkingModel.setFinishDrinkingInHoursMinutes(seekBarValue)
    }

    fun setPeakTimeInHoursMinutes(seekBarValue: Int) {
        startDrinkingModel.setPeakTimeInHoursMinutes(seekBarValue)
    }

    fun setSelectedUnit(selectedUnit: AlcoholUnit) {
        startDrinkingModel.setSelectedUnit(selectedUnit)
    }

    fun startDrinking() {
        startDrinkingModel.startDrinking()
    }
}
