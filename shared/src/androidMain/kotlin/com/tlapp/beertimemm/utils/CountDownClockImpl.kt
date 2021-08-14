package com.tlapp.beertimemm.utils

import android.os.CountDownTimer
import org.koin.core.component.KoinComponent
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
actual class CountDownClockImpl : CountDownClock, KoinComponent {
    private lateinit var countDownTimer: CountDownTimer

    actual override suspend fun countDownEachSecond(duration: Duration, onTick: (millisUntilFinished: Long) -> Unit, onFinished: () -> Unit) {
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        countDownTimer = object : CountDownTimer(duration.inWholeMilliseconds, 1000) {
            override fun onFinish() {
                onFinished.invoke()
            }

            override fun onTick(millisUntilFinsihed: Long) {
                onTick.invoke(millisUntilFinsihed)
            }
        }.start()
    }

    actual override fun stop() {
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}
