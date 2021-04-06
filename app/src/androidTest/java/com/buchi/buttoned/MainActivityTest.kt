package com.buchi.buttoned

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith


@LargeTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    // Logged in User for the main activity
    private val loggedUser = User(
        "c825cae8-91f8-11eb-a8b3-0242ac130003",
        "Thomas Muller",
        "tMuller",
        "pass123"
    )
    // Initial intent with the logged in User
    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        .putExtra(Constants.KeyPairs.LOGGED_IN_USER, loggedUser)

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityScenarioRule = ActivityScenarioRule<MainActivity>(intent)

    @get:Rule(order = 2)
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)

    lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun init() {
        hiltRule.inject()
        scenario = launch(intent)
//        IdlingRegistry.getInstance().register(myIdlingResource)
    }

    @After
    fun close() {
        if (::scenario.isInitialized) scenario.close()
//        IdlingRegistry.getInstance().unregister(myIdlingResource)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.buchi.buttoned", appContext.packageName)
    }

    @Test
    fun welcomeTextIsDisplayed() {
        onView(withId(R.id.main_welcome_label)).check(matches(withText("Welcome Thomas Muller")))
    }

    @Test
    fun mainActivityLogsOutUserNavigatesToAuthActivity() {
        onView(withId(R.id.main_logout_action)).perform(click())
        intended(hasComponent(AuthActivity::class.java.name))
    }

    @Test
    fun mainActivityLogsOutAfter30secInForGroundNavigatesToAuthActivity() {
        runBlocking {
            // Added extra 1sec for clean up and rounding up the navigation to auth activity
            delay(Constants.TIMEOUT_FOREGROUND_MILLI + 1000)
            intended(hasComponent(AuthActivity::class.java.name))
        }
    }


}