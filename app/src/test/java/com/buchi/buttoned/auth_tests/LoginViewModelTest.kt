package com.buchi.buttoned.auth_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.login.LoginStateEvent
import com.buchi.buttoned.authentication.presentation.login.LoginViewModel
import com.buchi.buttoned.authentication.presentation.login.LoginViewState
import com.buchi.buttoned.authentication.presentation.signup.SignUpViewModel
import com.buchi.buttoned.authentication.presentation.signup.SignupStateEvent
import com.buchi.buttoned.authentication.presentation.signup.SignupViewState
import com.buchi.buttoned.authentication.utils.ResultState
import com.buchi.buttoned.utils.MainCoroutineScopeRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

//    @Mock
    lateinit var authRepo: AuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        authRepo = MockAuthRepository()
        viewModel = LoginViewModel(authRepo)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        coroutineScope.cleanupTestCoroutines()
    }

    @Test
    fun idleStateSetViewStateToNull() {
        coroutineScope.runBlockingTest {
            // Process
            viewModel.setStateEvent(LoginStateEvent.Idle)
            // Test
            Assert.assertNull(viewModel.dataState.value.data?.getContentIfNotHandled()?.user)
        }
    }

    @Test
    fun loginProcessSetViewStateToValidUser() {
        coroutineScope.runBlockingTest {

            // Process to test
            viewModel.setStateEvent(LoginStateEvent.Login("tMuller", "password123"))

            // Test confirmation
            Assert.assertEquals("tMuller", viewModel.dataState.first().data?.getContentIfNotHandled()?.user?.username)
        }
    }
}