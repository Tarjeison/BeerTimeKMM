package com.pd.beertimer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tlapp.beertimemm.drinking.DrinkCoordinator
import com.tlapp.beertimemm.models.AlcoholUnit
import com.tlapp.beertimemm.models.DrinkingCalculator
import com.tlapp.beertimemm.models.Gender
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.storage.ProfileStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.java.KoinJavaComponent.getKoin
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalSerializationApi
@ExperimentalTime
@RunWith(AndroidJUnit4::class)
class CountDownIntegrationTest {
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    private val testUserProfile = UserProfile(Gender.MALE, 75)
    private val testCalculator = DrinkingCalculator(
        testUserProfile,
        1F,
        Clock.System.now().plus(Duration.Companion.hours(1)),
        Clock.System.now().plus(Duration.Companion.hours(1)),
        AlcoholUnit("Test", 0.5F, 0.047F, "Test", true)
    )


    @Before
    fun before() {
        getKoin().get<DrinkCoordinator>().startDrinking(testCalculator)
        getKoin().get<ProfileStorage>().saveUserProfile(testUserProfile)
        activityRule.scenario.onActivity { activity ->
            activity.bottom_bar.selectTabAt(1)
        }
    }

    @Test
    fun countDownFragment_hasCorrectVisibleFields_whenDrinking() {
        onView(withId(R.id.tcClock)).check(matches(isDisplayed()))
        onView(withId(R.id.clNumOfUnits)).check(matches(isDisplayed()))
        onView(withId(R.id.chartBac)).check(matches(isDisplayed()))
        onView(withId(R.id.bStopDrinking)).perform(scrollTo()).check(matches(isDisplayed()))
    }

    @Test
    fun countDownFragment_stopDrinking_clearsView() {
        onView(withId(R.id.bStopDrinking)).perform(scrollTo(), click())
        onView(withText("YES")).perform(click())
        onView(withText("00:00")).check(matches(isDisplayed()))
    }

    @After
    fun after() {
        getKoin().get<DrinkCoordinator>().stopDrinking()
    }
}