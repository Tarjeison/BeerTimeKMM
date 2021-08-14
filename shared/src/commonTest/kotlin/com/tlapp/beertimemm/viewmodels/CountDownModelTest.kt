package com.tlapp.beertimemm.viewmodels

import app.cash.turbine.test
import com.tlapp.beertimemm.BaseTest
import com.tlapp.beertimemm.drinking.DrinkCoordinator
import com.tlapp.beertimemm.drinking.DrinkNotificationScheduler
import com.tlapp.beertimemm.mock.ClockMock
import com.tlapp.beertimemm.mock.CountDownClockMock
import com.tlapp.beertimemm.mock.DrinkCoordinatorMock
import com.tlapp.beertimemm.mock.DrinkNotificationSchedulerMock
import com.tlapp.beertimemm.models.AlcoholUnit
import com.tlapp.beertimemm.models.DrinkingCalculator
import com.tlapp.beertimemm.models.Gender
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.resources.Strings.COUNTDOWN_DESCRIPTION
import com.tlapp.beertimemm.utils.CountDownClock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
internal class CountDownModelTest : BaseTest(), KoinTest {

    // 1. Jan 2017 20:00
    var currentInstant: Instant = Instant.fromEpochMilliseconds(1483297200)
    private val clockMock = ClockMock(currentInstant)
    private val drinkCoordinatorMock = DrinkCoordinatorMock()
    private val drinkNotificationSchedulerMock = DrinkNotificationSchedulerMock()
    private val countDownClockMock = CountDownClockMock()

    @BeforeTest
    fun before() {
        startKoin {
            modules(module {
                single<Clock> { clockMock }
                single<DrinkCoordinator> { drinkCoordinatorMock }
                single<DrinkNotificationScheduler> { drinkNotificationSchedulerMock }
                single<CountDownClock> { countDownClockMock }
                single { CountDownModel() }
            })
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
        drinkNotificationSchedulerMock.reset()
        drinkCoordinatorMock.reset()
    }

    @Test
    fun whenDrinkingHasNotStarted_flowsAreUpdatedWithCorrectValues() = runTest {
        val countDownModel = get<CountDownModel>()

        countDownModel.countDownDescriptionDisplayValueFlow.test {
            assertEquals(COUNTDOWN_DESCRIPTION, expectItem())
            expectNoEvents()
        }

        countDownModel.countDownDisplayValueFlow.test {
            assertEquals("00:00", expectItem())
            expectNoEvents()
        }

        countDownModel.nOfUnitsDisplayValueFlow.test {
            assertEquals(null, expectItem())
            expectNoEvents()
        }

        countDownModel.drinkStatusModelFlow.test {
            assertEquals(DrinkStatusModel.NotStarted, expectItem())
            expectNoEvents()
        }
    }

    @Test
    fun whenDrinkingHasStarted_flowsAreUpdatedWithCorrectValues() = runTest {
        val drinkingTimes = listOf(
            currentInstant.minus(Duration.Companion.minutes(60)),
            currentInstant.minus(Duration.Companion.minutes(30)),
            currentInstant.plus(Duration.Companion.minutes(30)),
            currentInstant.plus(Duration.Companion.minutes(60)),
            currentInstant.plus(Duration.Companion.minutes(90))
        )
        drinkCoordinatorMock.drinkingTimesReturnValue = drinkingTimes
        val wantedBloodLevel = 0.1F
        val drinkingCalculator = DrinkingCalculator(
            userProfile = UserProfile(Gender.MALE, 85),
            wantedBloodLevel = wantedBloodLevel,
            peakTime = currentInstant.plus(Duration.Companion.hours(2)),
            endTime = currentInstant.plus(Duration.Companion.hours(4)),
            preferredUnit = AlcoholUnit("Beer", 0.5F, 0.047F, "test", false)
        )
        drinkCoordinatorMock.lastSetDrinkingCalculator = drinkingCalculator
        val countDownModel = get<CountDownModel>()

        countDownModel.countDownDescriptionDisplayValueFlow.test {
            assertEquals(COUNTDOWN_DESCRIPTION, expectItem())
            expectNoEvents()
        }

        countDownModel.countDownDisplayValueFlow.test {
            assertEquals("00:00", expectItem())
            expectNoEvents()
        }

        countDownModel.nOfUnitsDisplayValueFlow.test {
            assertEquals("Enjoy your 2nd drink!", expectItem())
            expectNoEvents()
        }

        countDownModel.drinkStatusModelFlow.test {
            val drinkStatus = expectItem() as DrinkStatusModel.Drinking
            assertEquals(5, drinkStatus.graphList.size)
            val bacPredictions = drinkingCalculator.generateBACPrediction(drinkingTimes)
            drinkStatus.graphList.forEachIndexed { index, graphEntry ->
                assertEquals(index.toFloat(), graphEntry.x)
                assertEquals(bacPredictions!![index].first, graphEntry.y)
                assertEquals(drinkingTimes[index], graphEntry.drinkAt)

                if (drinkStatus.graphList.lastIndex == index) {
                    assertEquals("ic_finish_flag", graphEntry.iconName)
                } else {
                    assertEquals(drinkingCalculator.preferredUnit.iconName, graphEntry.iconName)
                }
            }
            assertEquals(wantedBloodLevel, drinkStatus.wantedBloodLevel)
            expectNoEvents()
        }
    }

    @Test
    fun stopDrinking_callsDrinkCoordinatorToStopDrinking() {
        val countDownModel = get<CountDownModel>()
        countDownModel.stopDrinking()
        assertEquals(true, drinkCoordinatorMock.stoppedDrinkingInvoked)
        assertEquals(true, countDownClockMock.stoppedInvoked)
    }

    @Test
    fun countDownClock_isSetWithCorrectCallbacks_andUpdatesCountDownFlow() = runTest {
        val drinkingTimes = listOf(
            currentInstant.plus(Duration.Companion.minutes(10))
        )
        drinkCoordinatorMock.drinkingTimesReturnValue = drinkingTimes
        val countDownModel = get<CountDownModel>()
        var onFinishedInvoked = false
        countDownModel.startCountDownTimer {
            onFinishedInvoked = true
        }
        countDownModel.countDownDisplayValueFlow.test {
            assertEquals("00:01", expectItem())
        }
        assertEquals(Duration.Companion.minutes(10), countDownClockMock.inputDuration)
        assertEquals(true, onFinishedInvoked)
    }
}
