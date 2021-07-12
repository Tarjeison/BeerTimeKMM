package com.tlapp.beertimemm.storage

import com.tlapp.beertimemm.models.DrinkingCalculator
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

@ExperimentalTime
actual class DrinkStorage {
    actual fun getExistingDrinkingTimes(): List<Instant>? {
        TODO("Not yet implemented")
    }

    actual fun getNextDrinkingTime(): NextDrinkingTime? {
        TODO("Not yet implemented")
    }

    actual fun getCurrentDrinkingCalculator(): DrinkingCalculator? {
        TODO("Not yet implemented")
    }

    actual fun clear() {
    }

    actual fun saveDrinkingTimesAndCalculator(
        drinkTimes: List<Instant>,
        drinkingCalculator: DrinkingCalculator
    ) {
    }
}
