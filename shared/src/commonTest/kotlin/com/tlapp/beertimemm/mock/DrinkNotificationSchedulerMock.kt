package com.tlapp.beertimemm.mock

import com.tlapp.beertimemm.drinking.DrinkNotificationScheduler
import kotlinx.datetime.Instant

class DrinkNotificationSchedulerMock(): DrinkNotificationScheduler {

    var notificationScheduledWithMs = -1L
    var alarmCancelled = false

    fun reset() {
        notificationScheduledWithMs = -1L
        alarmCancelled = false
    }

    override fun scheduleNotification(drinkingTimes: List<Instant>) {
        notificationScheduledWithMs = drinkingTimes[1].toEpochMilliseconds()
    }

    override fun cancelAlarm() {
        alarmCancelled = true
    }
}