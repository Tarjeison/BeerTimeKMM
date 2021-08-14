package com.tlapp.beertimemm.utils

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
actual class CountDownClockImpl : CountDownClock {
    actual override suspend fun countDownEachSecond(duration: Duration, onTick: (millisUntilFinished: Long) -> Unit, onFinished: () -> Unit) {
    }

    actual override fun stop() {
    }
}
