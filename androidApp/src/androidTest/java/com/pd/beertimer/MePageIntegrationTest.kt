package com.pd.beertimer

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4::class)
class MePageIntegrationTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun navigateToMePage() {
        activityRule.scenario.onActivity { activity ->
            activity.bottom_bar.selectTabAt(2)
        }
    }


    @Test
    fun navigateToProfile() {
        onView(withId(R.id.rvMe)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.clProfile)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateToMyDrinks() {
        onView(withId(R.id.rvMe)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        onView(withId(R.id.rvMyDrinks)).check(matches(isDisplayed()))
    }

}