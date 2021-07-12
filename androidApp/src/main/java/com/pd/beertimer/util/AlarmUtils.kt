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
class AlarmUtils(private val application: Context): DrinkNotificationScheduler {

    fun setFirstAlarmAndStoreTimesToSharedPref(
        drinkingTimes: List<Instant>,
        calculator: DrinkingCalculator
    ) {
        saveDrinkingValuesToSharedPref(drinkingTimes, calculator)

        // Drop first time as it is when the user started drinking
        drinkingTimes.getOrNull(1)?.let {
            val millisTriggerTime = it.toEpochMilliseconds()
            scheduleNotification(millisTriggerTime)
        }
    }

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
        clearDrinkingValuesSharedPref()
    }

    fun getExistingDrinkTimesFromSharedPref(): List<Instant>? {
        val sharedPref =
            application.getSharedPreferences(SHARED_PREF_BEER_TIME, Context.MODE_PRIVATE)
        sharedPref.getString(SHARED_PREF_DRINKING_TIMES, null)?.let {
            val drinkingTimes = Json.decodeFromString<List<Instant>>(it)
            if (drinkingTimes.last() > Clock.System.now()) {
                return drinkingTimes
            }
        }
        return null
    }

    fun getNextDrinkingTimeFromSharedPref(): Pair<Instant, Boolean>? {
        val sharedPref =
            application.getSharedPreferences(SHARED_PREF_BEER_TIME, Context.MODE_PRIVATE)
        sharedPref.getString(SHARED_PREF_DRINKING_TIMES, null)?.let { drinkingTimesFromSharedPref ->
            val drinkingTimes = Json.decodeFromString<List<Instant>>(drinkingTimesFromSharedPref)
            val currentTime = Clock.System.now().plus(Duration.Companion.seconds(10)) // Give some leeway
            val nextDrinkingTime =
                drinkingTimes.firstOrNull { it > currentTime } ?: return null
            val isLast =
                drinkingTimes.indexOf(nextDrinkingTime) == drinkingTimes.lastIndex
            return Pair(nextDrinkingTime, isLast)
        }
        return null
    }

    fun getDrinkingCalculatorSharedPref(): DrinkingCalculator? {
        val sharedPref =
            application.getSharedPreferences(SHARED_PREF_BEER_TIME, Context.MODE_PRIVATE)
        sharedPref.getString(SHARED_PREF_DRINKING_CALCULATOR, null)?.let {
            return Json.decodeFromString<DrinkingCalculator>(it)
        }
        return null
    }

    private fun clearDrinkingValuesSharedPref() {
        val sharedPref =
            application.getSharedPreferences(SHARED_PREF_BEER_TIME, Context.MODE_PRIVATE)
        sharedPref.edit()
            .remove(SHARED_PREF_DRINKING_TIMES)
            .remove(SHARED_PREF_DRINKING_CALCULATOR)
            .apply()
    }

    private fun saveDrinkingValuesToSharedPref(
        drinkingTimes: List<Instant>,
        calculator: DrinkingCalculator
    ) {
        val sharedPref =
            application.getSharedPreferences(SHARED_PREF_BEER_TIME, Context.MODE_PRIVATE)
        sharedPref.edit()
            .putString(SHARED_PREF_DRINKING_TIMES, Json.encodeToString(drinkingTimes))
            .putString(SHARED_PREF_DRINKING_CALCULATOR, Json.encodeToString(calculator))
            .apply()
    }
}
