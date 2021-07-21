package com.tlapp.beertimemm.storage

import android.content.SharedPreferences
import com.tlapp.beertimemm.models.DrinkingCalculator
import com.tlapp.beertimemm.utils.DRINKING_CALCULATOR_KEY
import com.tlapp.beertimemm.utils.DRINKING_TIMES_KEY
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
internal actual class DrinkStorage(private val sharedPreferences: SharedPreferences) {
    actual fun getExistingDrinkingTimes(): List<Instant>? {
        sharedPreferences.getString(DRINKING_TIMES_KEY, null)?.let {
            val drinkingTimes = Json.decodeFromString<List<Instant>>(it)
            if (drinkingTimes.last() > Clock.System.now()) {
                return drinkingTimes
            }
        }
        return null
    }

    actual fun getNextDrinkingTime(): NextDrinkingTime? {
        sharedPreferences.getString(DRINKING_TIMES_KEY, null)?.let { drinkingTimesFromSharedPref ->
            val drinkingTimes = Json.decodeFromString<List<Instant>>(drinkingTimesFromSharedPref)
            val currentTime = Clock.System.now().plus(Duration.Companion.seconds(10)) // Give some leeway
            val nextDrinkingTime =
                drinkingTimes.firstOrNull { it > currentTime } ?: return null
            val isLast =
                drinkingTimes.indexOf(nextDrinkingTime) == drinkingTimes.lastIndex
            return NextDrinkingTime(nextDrinkingTime, isLast)
        }
        return null
    }

    actual fun getCurrentDrinkingCalculator(): DrinkingCalculator? {
        sharedPreferences.getString(DRINKING_CALCULATOR_KEY, null)?.let {
            return Json.decodeFromString<DrinkingCalculator>(it)
        }
        return null
    }

    actual fun clear() {
        sharedPreferences.edit()
            .remove(DRINKING_CALCULATOR_KEY)
            .remove(DRINKING_TIMES_KEY)
            .apply()
    }

    actual fun saveDrinkingTimesAndCalculator(
        drinkTimes: List<Instant>,
        drinkingCalculator: DrinkingCalculator
    ) {
        sharedPreferences.edit()
            .putString(DRINKING_TIMES_KEY, Json.encodeToString(drinkTimes))
            .putString(DRINKING_CALCULATOR_KEY, Json.encodeToString(drinkingCalculator))
            .apply()
    }
}
