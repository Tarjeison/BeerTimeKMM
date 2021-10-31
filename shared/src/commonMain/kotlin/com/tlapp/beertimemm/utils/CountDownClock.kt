package com.tlapp.beertimemm.utils

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
interface CountDownClock {
    suspend fun countDownEachSecond(duration: Duration, onTick: (millisUntilFinished: Long) -> Unit, onFinished: () -> Unit)
    fun stop()
}

@OptIn(ExperimentalTime::class)
expect class CountDownClockImpl: CountDownClock {
    override suspend fun countDownEachSecond(duration: Duration, onTick: (millisUntilFinished: Long) -> Unit, onFinished: () -> Unit)
    override fun stop()
}
