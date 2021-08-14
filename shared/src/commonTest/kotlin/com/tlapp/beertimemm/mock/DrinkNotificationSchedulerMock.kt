package com.tlapp.beertimemm.mock

import com.tlapp.beertimemm.drinking.DrinkNotificationScheduler

class DrinkNotificationSchedulerMock(): DrinkNotificationScheduler {

    var notificationScheduledWithMs = -1L
    var alarmCancelled = false

    fun reset() {
        notificationScheduledWithMs = -1L
        alarmCancelled = false
    }

    override fun scheduleNotification(notificationTimeInMs: Long) {
        notificationScheduledWithMs = notificationTimeInMs
    }

    override fun cancelAlarm() {
        alarmCancelled = true
    }
}