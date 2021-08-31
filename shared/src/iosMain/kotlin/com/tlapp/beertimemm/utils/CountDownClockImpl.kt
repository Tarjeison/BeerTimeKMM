package com.tlapp.beertimemm.utils

import platform.Foundation.NSTimer
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

private const val oneSecond: Double = 1.0

@ExperimentalTime
actual class CountDownClockImpl : CountDownClock {
    private var countDownTimer: NSTimer? = null
    private var remainingDurationInMs = -1L

    actual override suspend fun countDownEachSecond(
        duration: Duration,
        onTick: (millisUntilFinished: Long) -> Unit,
        onFinished: () -> Unit
    ) {
        countDownTimer?.invalidate()
        remainingDurationInMs = duration.inWholeMilliseconds
        countDownTimer = NSTimer.scheduledTimerWithTimeInterval(oneSecond, false) {
            remainingDurationInMs -= 1000L
            if (remainingDurationInMs < 0L) {
                onFinished.invoke()
            } else {
                onTick.invoke(remainingDurationInMs)
            }
        }
    }

    actual override fun stop() {
        countDownTimer?.invalidate()
        countDownTimer = null
    }
}
