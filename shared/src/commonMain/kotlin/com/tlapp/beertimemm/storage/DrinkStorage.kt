package com.tlapp.beertimemm.storage

import com.tlapp.beertimemm.models.DrinkingCalculator
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

@ExperimentalTime
internal expect class DrinkStorage {
    fun getExistingDrinkingTimes(): List<Instant>?
    fun getNextDrinkingTime(): NextDrinkingTime?
    fun getCurrentDrinkingCalculator(): DrinkingCalculator?
    fun clear()
    fun saveDrinkingTimesAndCalculator(drinkTimes: List<Instant>, drinkingCalculator: DrinkingCalculator)
}

data class NextDrinkingTime(val time: Instant, val isLastDrink: Boolean)
