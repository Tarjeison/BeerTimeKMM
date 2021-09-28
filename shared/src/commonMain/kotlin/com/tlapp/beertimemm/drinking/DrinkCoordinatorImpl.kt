package com.tlapp.beertimemm.drinking

import com.tlapp.beertimemm.models.DrinkingCalculator
import com.tlapp.beertimemm.resources.Strings
import com.tlapp.beertimemm.storage.DrinkStorage
import com.tlapp.beertimemm.utils.Failure
import com.tlapp.beertimemm.utils.Result
import com.tlapp.beertimemm.utils.Success
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime

@ExperimentalTime
interface DrinkCoordinator {
    fun startDrinking(drinkingCalculator: DrinkingCalculator): Result<Boolean, String>
    fun stopDrinking()
    fun getDrinkingCalculator(): DrinkingCalculator?
    fun getDrinkingTimes(): List<Instant>?
    fun getNextDrinkDrinkingTime(): Instant?
    fun isDrinking(): Boolean
}

@ExperimentalTime
class DrinkCoordinatorImpl(): DrinkCoordinator, KoinComponent {
    private val drinkStorage: DrinkStorage by inject()
    private val drinkNotificationScheduler: DrinkNotificationScheduler by inject()
    private val clock: Clock by inject()

    override fun startDrinking(drinkingCalculator: DrinkingCalculator): Result<Boolean, String> {
        val drinkingTimes = drinkingCalculator.calculateDrinkingTimes()
        when (drinkingTimes.size) {
            in 0..1 -> {
                return Failure(Strings.ERROR_NOTHING_TO_DRINK)
            }
            2 -> {
                return Failure(Strings.ERROR_ONLY_ONE_DRINK)
            }
            in 30..Int.MAX_VALUE -> {
                return Failure(Strings.ERROR_TOO_MANY_DRINKS)
            }
        }

        drinkStorage.saveDrinkingTimesAndCalculator(drinkingTimes, drinkingCalculator)
        // The first element is the first beer, which should not give a notification
        drinkNotificationScheduler.scheduleNotification(drinkingTimes.slice(1..drinkingTimes.lastIndex))
        return Success(true)
    }

    override fun stopDrinking() {
        drinkStorage.clear()
        drinkNotificationScheduler.cancelAlarm()
    }

    override fun getDrinkingTimes() = drinkStorage.getExistingDrinkingTimes()

    override fun getNextDrinkDrinkingTime(): Instant? {
        return drinkStorage.getExistingDrinkingTimes()?.find { it > clock.now() }
    }

    override fun isDrinking(): Boolean {
        return drinkStorage.getExistingDrinkingTimes()?.any { it > clock.now() } ?: false
    }

    override fun getDrinkingCalculator() = drinkStorage.getCurrentDrinkingCalculator()
}
