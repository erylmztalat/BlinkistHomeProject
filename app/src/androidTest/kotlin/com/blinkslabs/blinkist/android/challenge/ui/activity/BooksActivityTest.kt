package com.blinkslabs.blinkist.android.challenge.ui.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.blinkslabs.blinkist.android.challenge.R

@RunWith(AndroidJUnit4::class)
class BooksActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(BooksActivity::class.java)

    @Test
    fun checkViewsAreDisplayed() {
        onView(withId(R.id.spinner)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.swipeRefreshLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun checkSpinnerSelection() {
        onView(withId(R.id.spinner)).perform(click())
        onView(withText("Alphabetically")).perform(click())
        onView(withId(R.id.spinner)).check(matches(withSpinnerText("Alphabetically")))
    }
}