package com.pd.beertimer.feature.startdrinking

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tlapp.beertimemm.models.AlcoholUnit
import com.tlapp.beertimemm.models.AlertDialogUiModel
import com.tlapp.beertimemm.viewmodels.StartDrinkingModel
import kotlinx.coroutines.flow.filterNotNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
class StartDrinkingViewModel : ViewModel(), KoinComponent {

    private val startDrinkingModel: StartDrinkingModel by inject()

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

    val toastLiveData: LiveData<String>
        get() = startDrinkingModel.errorToastFlow.filterNotNull().asLiveData()

    val alertDialogLiveData: LiveData<AlertDialogUiModel>
        get() = startDrinkingModel.alertFlow.filterNotNull().asLiveData()

    val navigationLiveData: LiveData<Boolean>
        get() = startDrinkingModel.navigateToCountDownFlow.filterNotNull().asLiveData()

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
