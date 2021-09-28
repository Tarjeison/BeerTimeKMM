package com.tlapp.beertimemm.drinking

import kotlinx.datetime.Instant

interface DrinkNotificationScheduler {
    fun scheduleNotification(drinkingTimes: List<Instant>)
    fun cancelAlarm()
}
