package com.tlapp.beertimemm.mock

import com.tlapp.beertimemm.utils.CountDownClock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class CountDownClockMock : CountDownClock {
    var stoppedInvoked = false
    var inputDuration: Duration? = null

    fun reset() {
        inputDuration = null
        stoppedInvoked = false
    }
    override suspend fun countDownEachSecond(duration: Duration, onTick: (millisUntilFinished: Long) -> Unit, onFinished: () -> Unit) {
        inputDuration = duration
        onTick.invoke(1000)
        onFinished.invoke()
    }

    override fun stop() {
        stoppedInvoked = true
    }
}
