package com.buchi.buttoned.auth_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.login.LoginViewState
import com.buchi.buttoned.authentication.presentation.signup.SignUpViewModel
import com.buchi.buttoned.authentication.presentation.signup.SignupStateEvent
import com.buchi.buttoned.authentication.presentation.signup.SignupViewState
import com.buchi.buttoned.authentication.utils.ResultState
import com.buchi.buttoned.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SignUpViewModelTest {
    private lateinit var viewModel: SignUpViewModel

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
        viewModel = SignUpViewModel(authRepo)
    }

    @After
    fun cleanUp() {
    }

    @Test
    fun idleStateSetViewStateToNull() {
        coroutineScope.runBlockingTest {
            // Process
            viewModel.setStateEvent(SignupStateEvent.Idle)
            // Test
            Assert.assertNull(viewModel.dataState.value.data?.getContentIfNotHandled()?.user)
        }
    }

    @Test
    fun signUpProcessSetViewStateToValidUser() {
        coroutineScope.runBlockingTest {
            val newUser = User(fullName = "Thomas Muller", username = "tMuller", password = "password123")
            val testFlow = flow {
                val user = User("test01", "Thomas Muller", "tMuller", "password123")
                emit(ResultState.data("User created", SignupViewState(user = user)))
            }

            // Process to test
            viewModel.setStateEvent(SignupStateEvent.SignUp(newUser))
//            Mockito.`when`(authRepo.signUp(newUser)).thenReturn(testFlow)

            // Test confirmation
            Assert.assertEquals("tMuller", viewModel.dataState.first().data?.getContentIfNotHandled()?.user?.username)
        }
    }
}