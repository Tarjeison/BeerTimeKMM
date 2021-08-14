package com.tlapp.beertimemm.mock

import com.tlapp.beertimemm.drinking.DrinkCoordinator
import com.tlapp.beertimemm.models.DrinkingCalculator
import com.tlapp.beertimemm.utils.Result
import com.tlapp.beertimemm.utils.Success
import kotlinx.datetime.Instant
import kotlin.time.ExperimentalTime

@ExperimentalTime
class DrinkCoordinatorMock: DrinkCoordinator {
    var lastSetDrinkingCalculator: DrinkingCalculator? = null
    var stoppedDrinkingInvoked: Boolean = false
    var isDrinkingReturnValue = false
    var drinkingTimesReturnValue: List<Instant>? = null
    var startDrinkingReturnValue: Result<Boolean, String> = Success(true)

    fun reset() {
        lastSetDrinkingCalculator = null
        stoppedDrinkingInvoked = false
        isDrinkingReturnValue = false
        startDrinkingReturnValue = Success(true)
        drinkingTimesReturnValue = null
    }

    override fun startDrinking(drinkingCalculator: DrinkingCalculator): Result<Boolean, String> {
        lastSetDrinkingCalculator = drinkingCalculator
        return startDrinkingReturnValue
    }

    override fun stopDrinking() {
        stoppedDrinkingInvoked = true
    }

    override fun getDrinkingCalculator(): DrinkingCalculator? {
        return lastSetDrinkingCalculator
    }

    override fun getDrinkingTimes(): List<Instant>? {
        return drinkingTimesReturnValue
    }

    override fun getNextDrinkDrinkingTime(): Instant? {
        return lastSetDrinkingCalculator?.calculateDrinkingTimes()?.last()
    }

    override fun isDrinking(): Boolean {
        return isDrinkingReturnValue
    }
}
