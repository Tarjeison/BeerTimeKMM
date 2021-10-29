package com.pd.beertimer

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pd.beertimer.util.clickChildViewWithId
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(AndroidJUnit4::class)
class MyDrinksIntegrationTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        activityRule.scenario.onActivity { activity ->
            activity.bottom_bar.selectTabAt(2)
        }
        onView(withId(R.id.rvMe))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, ViewActions.click()))
    }

    @Test
    fun addAndDeleteNewDrink() {
        onView(withId(R.id.addDrinkFab)).perform(click())
        onView(withId(R.id.etDrinkName)).perform(replaceText("TestDrink"))
        onView(withId(R.id.etPercentage)).perform(replaceText("6.9"))
        onView(withId(R.id.etVolume)).perform(replaceText("0.5"))
        onView(withId(R.id.rvDrinkIcons)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.bAddDrink)).perform(click())

        onView(withText("TestDrink")).check(matches(isDisplayed()))
        onView(withId(R.id.rvMyDrinks)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("TestDrink")),
                clickChildViewWithId(R.id.ivDelete)
            )
        )
        sleep(100)
        onView(allOf(withId(R.id.tvDrinkName))).check(matches(not(withText("TestDrink"))))
    }
}