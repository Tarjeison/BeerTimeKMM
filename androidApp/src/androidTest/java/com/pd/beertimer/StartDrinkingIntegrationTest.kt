package com.pd.beertimer

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pd.beertimer.feature.startdrinking.StartDrinkingViewModel
import com.pd.beertimer.util.hasTextValue
import com.pd.beertimer.util.isToast
import com.pd.beertimer.util.setProgress
import com.tlapp.beertimemm.drinking.DrinkCoordinator
import com.tlapp.beertimemm.models.Gender
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.storage.ProfileStorage
import io.mockk.mockk
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.time.ExperimentalTime

@ExperimentalSerializationApi
@ExperimentalTime
@RunWith(AndroidJUnit4::class)
class StartDrinkingIntegrationTest : KoinTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)


    private val testUserProfile = UserProfile(Gender.MALE, 75)

    @Before
    fun before() {
        getKoin().get<ProfileStorage>().saveUserProfile(testUserProfile)
    }

    @After
    fun after() {
        getKoin().get<DrinkCoordinator>().stopDrinking()
    }

    @Test
    fun toastAreDisplayed_whenAFieldIsNotSet() {
        onView(withId(R.id.bStartDrinking)).perform(scrollTo(), click())
        onView(withId(R.id.tvMessage)).inRoot(isToast()).check(matches(isDisplayed()))
    }

    @Test
    fun whenAllFieldsAreSet_navigateToCountDownFragment() {
        onView(withId(R.id.sbBloodLevel)).perform(setProgress(15))
        onView(withId(R.id.tvBloodLevelValue)).check(matches(hasTextValue()))

        onView(withId(R.id.sbHours)).perform(scrollTo(), setProgress(720))
        onView(withId(R.id.tvHoursValue)).check(matches(hasTextValue()))

        onView(withId(R.id.sbPeak)).perform(scrollTo(), setProgress(720))
        onView(withId(R.id.tvPeakValue)).check(matches(hasTextValue()))

        onView(withId(R.id.rvAlcoholUnit)).perform(scrollTo(), RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.bStartDrinking)).perform(scrollTo(), click())
        onView(withId(R.id.tcClock)).check(matches(isDisplayed()))
    }

}
