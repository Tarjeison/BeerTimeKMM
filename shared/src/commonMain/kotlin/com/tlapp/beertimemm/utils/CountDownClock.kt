package com.tlapp.beertimemm.utils

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
interface CountDownClock {
    suspend fun countDownEachSecond(duration: Duration, onTick: (millisUntilFinished: Long) -> Unit, onFinished: () -> Unit)
    fun stop()
}

@ExperimentalTime
expect class CountDownClockImpl: CountDownClock {
    override suspend fun countDownEachSecond(duration: Duration, onTick: (millisUntilFinished: Long) -> Unit, onFinished: () -> Unit)
    override fun stop()
}
