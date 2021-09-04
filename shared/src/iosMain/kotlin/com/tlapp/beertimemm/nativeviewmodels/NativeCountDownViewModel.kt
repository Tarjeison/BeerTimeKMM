package com.tlapp.beertimemm.nativeviewmodels

import co.touchlab.stately.ensureNeverFrozen
import com.tlapp.beertimemm.viewmodels.CountDownModel
import com.tlapp.beertimemm.viewmodels.DrinkStatusModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@ExperimentalTime
class NativeCountDownViewModel(
    private val onCountDownChanged: (String) -> Unit,
    private val onCurrentlyDrinking: (DrinkStatusModel.Drinking) -> Unit
) {

    private var scope: CoroutineScope? = null
    private val countDownModel = CountDownModel()

    init {
        ensureNeverFrozen()
    }

    fun observeData() {
        ensureScopeNotNull()
        scope?.launch {
            countDownModel.countDownDisplayValueFlow.onEach {
                onCountDownChanged.invoke(it)
            }.launchIn(this)

            countDownModel.drinkStatusModelFlow.onEach { drinkStatusModel ->
                drinkStatusModel?.let {
                    when (it) {
                        DrinkStatusModel.NotStarted -> {
                            // Nothing yet
                        }
                        is DrinkStatusModel.Drinking -> {
                            onCurrentlyDrinking.invoke(it)
                        }
                        DrinkStatusModel.Finished -> {
                            // Nothing yet
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    fun startCountDown() {
        ensureScopeNotNull()
        scope?.launch {
            countDownModel.startCountDownTimer {
                startCountDown()
            }
        }
    }

    fun onDestroy() {
        scope?.cancel()
        scope = null
    }

    private fun ensureScopeNotNull() {
        if (scope == null) scope = MainScope()
    }
}