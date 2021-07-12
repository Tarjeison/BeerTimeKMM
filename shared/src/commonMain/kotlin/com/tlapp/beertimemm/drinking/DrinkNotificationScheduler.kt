package com.tlapp.beertimemm.drinking

interface DrinkNotificationScheduler {
    fun scheduleNotification(notificationTimeInMs: Long)
    fun cancelAlarm()
}
