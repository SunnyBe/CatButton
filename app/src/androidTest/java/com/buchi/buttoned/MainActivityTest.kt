package com.buchi.buttoned

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.AuthActivity
import com.buchi.buttoned.main.MainActivity
import com.buchi.buttoned.utils.Constants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule(order = 2)
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.buchi.buttoned", appContext.packageName)
    }

    @Test
    fun welcomeTextIsDisplayed() {
        val loggedUser = User("c825cae8-91f8-11eb-a8b3-0242ac130003", "Thomas Muller", "tMuller", "password123")
        val expectedIntent: Matcher<Intent> = hasExtra(Constants.KeyPairs.LOGGED_IN_USER, loggedUser)
        val intentData = Intent().apply {
            putExtra(Constants.KeyPairs.LOGGED_IN_USER, loggedUser)
        }
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, intentData)
        Intents.intending(expectedIntent).respondWith(result)

        onView(withId(R.id.main_welcome_label)).check(matches(withText("Welcome Thomas Muller")))
    }


}