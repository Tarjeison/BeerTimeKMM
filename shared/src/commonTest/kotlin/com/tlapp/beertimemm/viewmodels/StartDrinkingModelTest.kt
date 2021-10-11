package com.tlapp.beertimemm.viewmodels

import app.cash.turbine.test
import com.russhwolf.settings.MockSettings
import com.tlapp.beertimemm.BaseTest
import com.tlapp.beertimemm.createTestSqlDriver
import com.tlapp.beertimemm.drinking.DrinkCoordinator
import com.tlapp.beertimemm.drinking.DrinkNotificationScheduler
import com.tlapp.beertimemm.mock.ClockMock
import com.tlapp.beertimemm.mock.DisplayDateHelperMock
import com.tlapp.beertimemm.mock.DrinkCoordinatorMock
import com.tlapp.beertimemm.models.AlcoholUnit
import com.tlapp.beertimemm.models.Gender
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.resources.Strings.ERROR_NO_BLOOD_LEVEL_SET
import com.tlapp.beertimemm.resources.Strings.ERROR_NO_DRINK_SELECTED
import com.tlapp.beertimemm.resources.Strings.ERROR_NO_PROFILE_FOUND
import com.tlapp.beertimemm.sqldelight.DatabaseHelper
import com.tlapp.beertimemm.storage.ProfileStorage
import com.tlapp.beertimemm.utils.DisplayDateHelper
import com.tlapp.beertimemm.utils.Failure
import com.tlapp.beertimemm.utils.PROFILE_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@ExperimentalSerializationApi
@ExperimentalTime
internal class StartDrinkingModelTest : BaseTest(), KoinTest {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(
        createTestSqlDriver(),
        Dispatchers.Default
    )

    private val mockSettings = MockSettings()
    private val displayDateHelperMock = DisplayDateHelperMock()
    private val drinkCoordinatorMock = DrinkCoordinatorMock()

    private var notificationScheduled = false
    private val drinkCoordinator = object : DrinkNotificationScheduler {
        override fun scheduleNotification(drinkingTimes: List<Instant>) {
            notificationScheduled = true
        }

        override fun cancelAlarm() {}
    }

    // 1. Jan 2017 20:00
    var currentInstant: Instant = Instant.fromEpochMilliseconds(1483297200)
    private val clockMock = ClockMock(currentInstant)

    @BeforeTest
    fun before() {
        startKoin {
            modules(module {
                single { databaseHelper }
                single { ProfileStorage(mockSettings) }
                single<DrinkNotificationScheduler> { drinkCoordinator }
                single { StartDrinkingModel() }
                single<Clock> { clockMock }
                single<DisplayDateHelper> { displayDateHelperMock }
                single<DrinkCoordinator> { drinkCoordinatorMock }
            })
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
        mockSettings.clear()
    }

    @Test
    fun whenNoDrinkIsSelected_toastFlowIsUpdatedWithCorrectError() = runTest {
        val startDrinkingModel = getKoin().get<StartDrinkingModel>()
        startDrinkingModel.startDrinking()
        startDrinkingModel.errorToastFlow.filterNotNull().test {
            assertEquals(ERROR_NO_DRINK_SELECTED, expectItem().value)
            expectNoEvents()
        }
    }

    @Test
    fun whenWantedBloodLevelIs0f_toastFlowIsUpdatedWithCorrectError() = runTest {
        val startDrinkingModel = getKoin().get<StartDrinkingModel>()
        startDrinkingModel.setSelectedUnit(TEST_ALCOHOL_UNIT)
        startDrinkingModel.startDrinking()
        startDrinkingModel.errorToastFlow.filterNotNull().test {
            assertEquals(ERROR_NO_BLOOD_LEVEL_SET, expectItem().value)
            expectNoEvents()
        }
    }

    @Test
    fun whenNoProfileIsFound_toastFlowIsUpdatedWithCorrectError() = runTest {
        val startDrinkingModel = getKoin().get<StartDrinkingModel>()
        startDrinkingModel.setSelectedUnit(TEST_ALCOHOL_UNIT)
        startDrinkingModel.setWantedBloodLevel(20)
        startDrinkingModel.startDrinking()
        startDrinkingModel.errorToastFlow.filterNotNull().test {
            assertEquals(ERROR_NO_PROFILE_FOUND, expectItem().value)
            expectNoEvents()
        }
    }

    @Test
    fun whenStartDrinkingSucceedsAndDrinkCoordinatorReturnsSuccess_navigationFlowIsUpdated() = runTest {
        val startDrinkingModel = getKoin().get<StartDrinkingModel>()
        mockSettings.putString(PROFILE_KEY, Json.encodeToString(UserProfile(Gender.MALE, 70)))
        startDrinkingModel.setSelectedUnit(TEST_ALCOHOL_UNIT)
        startDrinkingModel.setWantedBloodLevel(20)
        startDrinkingModel.startDrinking()
        startDrinkingModel.navigateToCountDownFlow.filterNotNull().test {
            assertEquals(true, expectItem().value)
            expectNoEvents()
        }
    }

    @Test
    fun whenStartDrinkingSucceedsAndDrinkCoordinatorRreturnsFailure_navigationFlowIsNotUpdatedAndErrorFlowIsUpdated() = runTest {
        val startDrinkingModel = getKoin().get<StartDrinkingModel>()
        mockSettings.putString(PROFILE_KEY, Json.encodeToString(UserProfile(Gender.MALE, 70)))
        drinkCoordinatorMock.startDrinkingReturnValue = Failure("Test")
        startDrinkingModel.setSelectedUnit(TEST_ALCOHOL_UNIT)
        startDrinkingModel.setWantedBloodLevel(20)
        startDrinkingModel.startDrinking()
        startDrinkingModel.navigateToCountDownFlow.filterNotNull().test {
            expectNoEvents()
        }
        startDrinkingModel.errorToastFlow.filterNotNull().test {
            assertEquals("Test", expectItem().value)
            expectNoEvents()
        }
    }

    @Test
    fun whenDrinkCoordinatorReturnsIsDrinking_alertDialogFlowIsTriggered() = runTest {
        val startDrinkingModel = get<StartDrinkingModel>()
        drinkCoordinatorMock.isDrinkingReturnValue = true
        startDrinkingModel.startDrinking()
        startDrinkingModel.alertFlow.filterNotNull().test {
            expectEvent()
            expectNoEvents()
        }
    }

    @Test
    fun whenDrinkCoordinatorReturnsIsDrinkingFalse_alertDialogFlowIsNotTriggered() = runTest {
        val startDrinkingModel = get<StartDrinkingModel>()
        drinkCoordinatorMock.isDrinkingReturnValue = false
        startDrinkingModel.startDrinking()
        startDrinkingModel.alertFlow.filterNotNull().test {
            expectNoEvents()
        }
    }

    @Test
    fun whenPeakTimeIsSet_peakTimeFlowIsUpdated_andDisplayHelperCalledWithCorrectDateTime() = runTest {
        val startDrinkingModel = get<StartDrinkingModel>()
        startDrinkingModel.setPeakTimeInHoursMinutes(90)
        startDrinkingModel.peakTimeSeekBarUiModelFlow.test {
            expectEvent()
            expectNoEvents()
        }
        assertEquals(
            Instant.parse("1970-01-18T05:40:37.200Z"),
            displayDateHelperMock.lastInvokedWith!!.toInstant(TimeZone.currentSystemDefault())
        )
    }

    @Test
    fun whenEndTimeIsSet_peakTimeFlowIsUpdated_andDisplayHelperCalledWithCorrectDateTime() = runTest {
        val startDrinkingModel = get<StartDrinkingModel>()
        startDrinkingModel.setFinishDrinkingInHoursMinutes(130)
        startDrinkingModel.finishDrinkingSeekBarUiModelFlow.test {
            expectEvent()
            expectNoEvents()
        }

        assertEquals(
            Instant.parse("1970-01-18T06:20:37.200Z"),
            displayDateHelperMock.lastInvokedWith!!.toInstant(TimeZone.currentSystemDefault())
        )
    }

    @Test
    fun getDrinks_returnsDrinksFromDatabase() = runTest {
        val startDrinkingModel = get<StartDrinkingModel>()
        startDrinkingModel.getDrinks().test {
            assertEquals(
                listOf(
                    AlcoholUnit(name = "Small beer", volume = 0.33f, percentage = 0.047f, iconName = "ic_small_beer", isSelected = false),
                    AlcoholUnit(name = "Beer", volume = 0.5f, percentage = 0.047f, iconName = "ic_beer", isSelected = false),
                    AlcoholUnit(name = "Wine", volume = 0.15f, percentage = 0.125f, iconName = "ic_wine", isSelected = false)
                ), expectItem()
            )
        }
    }


    @Test
    fun getDrinks_setsCorrectDrinkToSelected_whenSelectedDrinkIsUpdated() = runTest {
        val startDrinkingModel = get<StartDrinkingModel>()
        startDrinkingModel.setSelectedUnit(
            AlcoholUnit(
                name = "Small beer",
                volume = 0.33f,
                percentage = 0.047f,
                iconName = "ic_small_beer",
                isSelected = false
            )
        )
        startDrinkingModel.getDrinks().test {
            assertEquals(
                listOf(
                    AlcoholUnit(name = "Small beer", volume = 0.33f, percentage = 0.047f, iconName = "ic_small_beer", isSelected = true),
                    AlcoholUnit(name = "Beer", volume = 0.5f, percentage = 0.047f, iconName = "ic_beer", isSelected = false),
                    AlcoholUnit(name = "Wine", volume = 0.15f, percentage = 0.125f, iconName = "ic_wine", isSelected = false)
                ), expectItem()
            )
        }
    }

    companion object {
        private val TEST_ALCOHOL_UNIT = AlcoholUnit(
            "Test",
            0.125F,
            0.05F,
            "Test",
            false
        )
    }
}
