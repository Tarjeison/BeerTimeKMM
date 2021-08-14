package com.pd.beertimer.util

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.pd.beertimer.NotificationBroadcast
import com.tlapp.beertimemm.drinking.DrinkNotificationScheduler
import com.tlapp.beertimemm.models.DrinkingCalculator
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

private const val REQUEST_CODE: Int = 1337727272

@ExperimentalTime
class NotificationScheduler(private val application: Context) : DrinkNotificationScheduler {

    override fun scheduleNotification(notificationTimeInMs: Long) {
        val am = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(application, NotificationBroadcast::class.java).let { intent ->
            PendingIntent.getBroadcast(
                application, REQUEST_CODE,
                intent,
                0
            )
        }
        val alarmClockInfo = AlarmManager.AlarmClockInfo(notificationTimeInMs, alarmIntent)
        am.setAlarmClock(alarmClockInfo, alarmIntent)
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
