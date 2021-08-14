package com.tlapp.beertimemm.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.tlapp.beertimemm.models.DrinkingCalculator
import com.tlapp.beertimemm.utils.DRINKING_CALCULATOR_KEY
import com.tlapp.beertimemm.utils.DRINKING_TIMES_KEY
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
internal class DrinkStorage(private val settings: Settings): KoinComponent {

    fun getExistingDrinkingTimes(): List<Instant>? {
        return settings.get<String>(DRINKING_TIMES_KEY)?.let {
            Json.decodeFromString<List<Instant>>(it)
        }
    }

    fun getCurrentDrinkingCalculator(): DrinkingCalculator? {
        return settings.get<String>(DRINKING_CALCULATOR_KEY)?.let {
            Json.decodeFromString<DrinkingCalculator>(it)
        }
    }

    fun clear() {
        settings.remove(DRINKING_TIMES_KEY)
        settings.remove(DRINKING_CALCULATOR_KEY)
    }

    fun saveDrinkingTimesAndCalculator(drinkTimes: List<Instant>, drinkingCalculator: DrinkingCalculator) {
        settings.putString(DRINKING_TIMES_KEY, Json.encodeToString(drinkTimes))
        settings.putString(DRINKING_CALCULATOR_KEY, Json.encodeToString(drinkingCalculator))
    }
}

data class NextDrinkingTime(val time: Instant, val isLastDrink: Boolean)
