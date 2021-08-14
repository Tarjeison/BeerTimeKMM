package com.pd.beertimer.feature.countdown

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tlapp.beertimemm.viewmodels.CountDownModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
class CountDownViewModel : ViewModel(), KoinComponent {
    private val countDownModel: CountDownModel by inject()

    val countDownTimerDisplayStringLiveData get() = countDownModel.countDownDisplayValueFlow.asLiveData()
    val drinkStatusLiveData get() = countDownModel.drinkStatusModelFlow.filterNotNull().asLiveData()
    val nUnitsDisplayValueLiveData get() = countDownModel.nOfUnitsDisplayValueFlow.filterNotNull().asLiveData()
    val countDownTimerDescriptionLiveData get() = countDownModel.countDownDescriptionDisplayValueFlow.asLiveData()

    fun startCountDown() {
        viewModelScope.launch {
            countDownModel.startCountDownTimer {
                startCountDown()
            }
        }
    }

    fun stopCountDown() {
        countDownModel.stopCountDown()
    }

    fun stopDrinking() {
        countDownModel.stopDrinking()
    }
}
