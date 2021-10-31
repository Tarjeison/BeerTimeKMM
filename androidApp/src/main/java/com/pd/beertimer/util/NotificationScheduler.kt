package com.pd.beertimer.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.pd.beertimer.NotificationBroadcast
import com.tlapp.beertimemm.drinking.DrinkNotificationScheduler
import kotlinx.datetime.Instant

private const val REQUEST_CODE: Int = 1337727272

class NotificationScheduler(
    private val application: Context,
) : DrinkNotificationScheduler {

    override fun scheduleNotification(drinkingTimes: List<Instant>) {
        application.scheduleNotificationAtMs(drinkingTimes[0].toEpochMilliseconds())
    }

    override fun cancelAlarm() {
        val am = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent =
            Intent(application, NotificationBroadcast::class.java).let { intent ->
                PendingIntent.getBroadcast(
                    application,
                    REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            }
        am.cancel(alarmIntent)
    }
}


fun Context.scheduleNotificationAtMs(notificationTimeInMs: Long) {
    val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(this, NotificationBroadcast::class.java).let { intent ->
        PendingIntent.getBroadcast(
            this, REQUEST_CODE,
            intent,
            0
        )
    }
    val alarmClockInfo = AlarmManager.AlarmClockInfo(notificationTimeInMs, alarmIntent)
    am.setAlarmClock(alarmClockInfo, alarmIntent)
}
