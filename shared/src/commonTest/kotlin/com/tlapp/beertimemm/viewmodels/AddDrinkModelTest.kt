package com.tlapp.beertimemm.viewmodels

import app.cash.turbine.test
import com.russhwolf.settings.MockSettings
import com.tlapp.beertimemm.BaseTest
import com.tlapp.beertimemm.createTestSqlDriver
import com.tlapp.beertimemm.resources.Strings
import com.tlapp.beertimemm.sqldelight.DatabaseHelper
import com.tlapp.beertimemm.storage.PreferredVolume
import com.tlapp.beertimemm.storage.ProfileStorage
import com.tlapp.beertimemm.utils.Failure
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AddDrinkModelTest : BaseTest(), KoinTest {

    private val databaseHelper: DatabaseHelper = DatabaseHelper(
        createTestSqlDriver(),
        Dispatchers.Default
    )

    @BeforeTest
    fun before() {
        startKoin {
            modules(
                module
                {
                    single { ProfileStorage(MockSettings()) }
                    single { databaseHelper }
                }
            )
        }
    }

    @AfterTest
    fun after() {
        stopKoin()
    }

    @Test
    fun drinkWithNoName_causesCorrectFailure() = runTest {
        val addDrinkModel = AddDrinkModel()
        addDrinkModel.addDrink(null, "2", "0.5", "ic_beer")
        addDrinkModel.addDrinkResultFlow.test {
            val value = expectItem()
            assertTrue(value is Failure)
            assertEquals(value.reason.second, Strings.NO_NAMELESS_DRINK)
            assertEquals(value.reason.first, AddDrinkInputField.DRINK_NAME)
        }
    }

    @Test
    fun drinkWithNoPercentage_causesCorrectFailure() = runTest {
        val addDrinkModel = AddDrinkModel()
        addDrinkModel.addDrink("test", null, "0.5", "ic_beer")
        addDrinkModel.addDrinkResultFlow.test {
            val value = expectItem()
            assertTrue(value is Failure)
            assertEquals(value.reason.second, Strings.NO_ALCOHOL_INPUT_ERROR)
            assertEquals(value.reason.first, AddDrinkInputField.DRINK_PERCENTAGE)
        }
    }

    @Test
    fun drinkWithNoVolume_causesCorrectFailure() = runTest {
        val addDrinkModel = AddDrinkModel()
        addDrinkModel.addDrink("test", "2", null, "ic_beer")
        addDrinkModel.addDrinkResultFlow.test {
            val value = expectItem()
            assertTrue(value is Failure)
            assertEquals(value.reason.second, Strings.NO_VOLUME_DRINK)
            assertEquals(value.reason.first, AddDrinkInputField.DRINK_VOLUME)
        }
    }

    @Test
    fun drinkWithTooHighPercentage_causesCorrectFailure() = runTest {
        val addDrinkModel = AddDrinkModel()
        addDrinkModel.addDrink("test", "101", "0.5", "ic_beer")
        addDrinkModel.addDrinkResultFlow.test {
            val value = expectItem()
            assertTrue(value is Failure)
            assertEquals(value.reason.second, Strings.TOO_STRONG_DRINK)
            assertEquals(value.reason.first, AddDrinkInputField.DRINK_PERCENTAGE)
        }
    }

    @Test
    fun drinkWithNegativePercentage_causesCorrectFailure() = runTest {
        val addDrinkModel = AddDrinkModel()
        addDrinkModel.addDrink("test", "-10", "0.5", "ic_beer")
        addDrinkModel.addDrinkResultFlow.test {
            val value = expectItem()
            assertTrue(value is Failure)
            assertEquals(value.reason.second, Strings.TOO_WEAK_DRINK)
            assertEquals(value.reason.first, AddDrinkInputField.DRINK_PERCENTAGE)
        }
    }

    @Test
    fun drinkWithNegativeVolume_causesCorrectFailure_whenPreferredUnitIsLiters() = runTest {
        get<ProfileStorage>().savePreferredVolume(PreferredVolume.LITER)
        val addDrinkModel = AddDrinkModel()
        addDrinkModel.addDrink("test", "2", "-0.5", "ic_beer")
        addDrinkModel.addDrinkResultFlow.test {
            val value = expectItem()
            assertTrue(value is Failure)
            assertEquals(value.reason.second, Strings.BABY_DRINK_LITER)
            assertEquals(value.reason.first, AddDrinkInputField.DRINK_VOLUME)
        }
    }

    @Test
    fun drinkWithNegativeVolume_causesCorrectFailure_whenPreferredUnitIsOz() = runTest {
        get<ProfileStorage>().savePreferredVolume(PreferredVolume.OUNCES)
        val addDrinkModel = AddDrinkModel()
        addDrinkModel.addDrink("test", "2", "-0.5", "ic_beer")
        addDrinkModel.addDrinkResultFlow.test {
            val value = expectItem()
            assertTrue(value is Failure)
            assertEquals(value.reason.second, Strings.BABY_DRINK_OZ)
            assertEquals(value.reason.first, AddDrinkInputField.DRINK_VOLUME)
        }
    }

    @Test
    fun drinkWithTooMuchVolume_causesCorrectFailure_whenPreferredUnitIsOz() = runTest {
        get<ProfileStorage>().savePreferredVolume(PreferredVolume.OUNCES)
        val addDrinkModel = AddDrinkModel()
        addDrinkModel.addDrink("test", "2", "99", "ic_beer")
        addDrinkModel.addDrinkResultFlow.test {
            val value = expectItem()
            assertTrue(value is Failure)
            assertEquals(value.reason.second, Strings.TOO_BIG_DRINK_OZ)
            assertEquals(value.reason.first, AddDrinkInputField.DRINK_VOLUME)
        }
    }

    @Test
    fun drinkWithTooMuchVolume_causesCorrectFailure_whenPreferredUnitIsLiter() = runTest {
        get<ProfileStorage>().savePreferredVolume(PreferredVolume.LITER)
        val addDrinkModel = AddDrinkModel()
        addDrinkModel.addDrink("test", "2", "99", "ic_beer")
        addDrinkModel.addDrinkResultFlow.test {
            val value = expectItem()
            assertTrue(value is Failure)
            assertEquals(value.reason.second, Strings.TOO_BIG_DRINK_LITER)
            assertEquals(value.reason.first, AddDrinkInputField.DRINK_VOLUME)
        }
    }

    @Test
    fun validDrink_isSavedCorrectly() = runTest {
        get<ProfileStorage>().savePreferredVolume(PreferredVolume.LITER)
        val addDrinkModel = AddDrinkModel()
        val drinkName = "Correct Drink"
        val drinkPercentage = "2"
        val drinkVolume = "0.5"
        val drinkIconName = "ic_test"
        addDrinkModel.addDrink(drinkName, drinkPercentage, drinkVolume, drinkIconName)
        databaseHelper.selectAllItems().test {
            val drinks = expectItem()
            val insertedDrink = drinks.first { it.name == drinkName }
            assertEquals(0.02F, insertedDrink.percentage)
            assertEquals(drinkVolume.toFloat(), insertedDrink.volume)
            assertEquals(drinkIconName, insertedDrink.icon_name)

            // Clean up
            databaseHelper.deleteDrink(insertedDrink.key)
        }
    }

    @Test
    fun validDrink_isSavedCorrectly_whenUsingIconTag() = runTest {
        get<ProfileStorage>().savePreferredVolume(PreferredVolume.LITER)
        val addDrinkModel = AddDrinkModel()
        val drinkName = "Correct Drink"
        val drinkPercentage = "2"
        val drinkVolume = "0.5"
        val drinkIconTag = 2
        addDrinkModel.addDrink(drinkName, drinkPercentage, drinkVolume, drinkIconTag)
        databaseHelper.selectAllItems().test {
            val drinks = expectItem()
            val insertedDrink = drinks.first { it.name == drinkName }
            assertEquals(0.02F, insertedDrink.percentage)
            assertEquals(drinkVolume.toFloat(), insertedDrink.volume)
            assertEquals(addDrinkModel.getDrinkIcons().find { it.tag == drinkIconTag }?.iconString, insertedDrink.icon_name)

            // Clean up
            databaseHelper.deleteDrink(insertedDrink.key)
        }
    }
}