package com.pd.beertimer

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pd.beertimer.util.isToast
import com.tlapp.beertimemm.models.Gender
import com.tlapp.beertimemm.storage.PreferredVolume
import com.tlapp.beertimemm.storage.ProfileStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import kotlin.time.ExperimentalTime

@ExperimentalSerializationApi
@ExperimentalTime
@RunWith(AndroidJUnit4::class)
class ProfileIntegrationTest: KoinTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun navigateToProfile() {
        activityRule.scenario.onActivity { activity ->
            activity.bottom_bar.selectTabAt(2)
        }
        onView(withId(R.id.rvMe))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    @Test
    fun updateProfile() {
        onView(withId(R.id.ibFemale)).perform(click())
        onView(withId(R.id.etWeight)).perform(replaceText("69"))
        onView(withId(R.id.bOunce)).perform(click())
        onView(withId(R.id.bSave)).perform(click())
        onView(withId(R.id.tvMessage)).inRoot(isToast()).check(matches(isDisplayed()))

        val profileStorage = getKoin().get<ProfileStorage>()
        assert(profileStorage.getPreferredVolume() == PreferredVolume.OUNCES)

        val userProfile = profileStorage.getUserProfile()
        assert(userProfile!!.gender == Gender.FEMALE)
        assert(userProfile.weight == 69)
    }

}