package com.buchi.buttoned

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import com.buchi.buttoned.authentication.presentation.AuthActivity
import com.buchi.buttoned.authentication.presentation.login.LoginFragment
import com.buchi.buttoned.authentication.presentation.signup.SignUpFragment
import com.buchi.buttoned.main.MainActivity
import com.buchi.buttoned.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AuthActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityScenarioRule = ActivityScenarioRule(AuthActivity::class.java)

    @get:Rule(order = 2)
    val intentsTestRule = IntentsTestRule(AuthActivity::class.java)

    @Before
    fun init() {
        hiltRule.inject()
    }

    // Create test nav controller with navigation graph and return the nav controller instance
    private fun testAuthNavHostController(): TestNavHostController {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        runOnUiThread {
            navController.setGraph(R.navigation.auth_nav)
        }
        return navController
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.buchi.buttoned", appContext.packageName)
    }

    @Test
    fun testSignUpFragment_navigates_to_loginFragment() {
        // make auth nav host controller
        val navController: TestNavHostController = testAuthNavHostController()
        launchFragmentInHiltContainer<SignUpFragment> {
            view?.let { v->
                Navigation.setViewNavController(v, navController)
            }
        }

        // Enter test full name
        onView(withId(R.id.fullname_entry)).perform(typeText("Thomas Muller"))
        onView(withId(R.id.username_entry)).perform(typeText("tMuller"))
        onView(withId(R.id.password_entry)).perform(typeText("password123"))
        // Perform click on submit button
        onView(withId(R.id.register_action)).perform(click())

        // Assert navigation was done to signIn page
        assert(navController.currentDestination?.id == R.id.loginFragment)
    }

    @Test
    fun testLoginFragment_navigates_to_mainActivity() {
        // make auth nav host controller
        val navController: TestNavHostController = testAuthNavHostController()
        launchFragmentInHiltContainer<LoginFragment> {
            view?.let { v->
                Navigation.setViewNavController(v, navController)
            }
        }

        // Enter test full name
        onView(withId(R.id.login_username_entry)).perform(typeText("tMuller"), closeSoftKeyboard())
        onView(withId(R.id.login_password_entry)).perform(typeText("password123"), closeSoftKeyboard())
        // Perform click on submit button
        onView(withId(R.id.login_action)).perform(click())

        // Assert activity switch to MainActivity
        intended(hasComponent(MainActivity::class.java.name.toString()))
    }

    @Test
    fun testSignUpFragment_confirmUserCreated() {
        // make auth nav host controller
        val navController: TestNavHostController = testAuthNavHostController()
        launchFragmentInHiltContainer<SignUpFragment> {
            view?.let { v->
                Navigation.setViewNavController(v, navController)
            }
        }

        // Enter test full name
        onView(withId(R.id.fullname_entry)).perform(typeText("Thomas Muller"), closeSoftKeyboard())
        onView(withId(R.id.username_entry)).perform(typeText("tMuller"), closeSoftKeyboard())
        onView(withId(R.id.password_entry)).perform(typeText("password123"), closeSoftKeyboard())
        // Perform click on submit button
        onView(withId(R.id.register_action)).perform(click())

        // Assert navigation was done to signIn page
        assert(navController.currentDestination?.id == R.id.loginFragment)
    }

    @Test
    fun testSignUpFragment_confirmNavigationToLoginScreen() {
        // make auth nav host controller
        val navController: TestNavHostController = testAuthNavHostController()
        launchFragmentInHiltContainer<SignUpFragment> {
            view?.let { v->
                Navigation.setViewNavController(v, navController)
            }
        }
        // Perform click on submit button
        onView(withId(R.id.proceed_to_login_action)).perform(click())

        // Assert navigation was done to signIn page
        assert(navController.currentDestination?.id == R.id.loginFragment)
    }

}