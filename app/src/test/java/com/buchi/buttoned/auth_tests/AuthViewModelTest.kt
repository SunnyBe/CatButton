package com.buchi.buttoned.auth_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.presentation.AuthViewModel
import com.buchi.buttoned.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
open class AuthViewModelTest {
    private lateinit var viewModel: AuthViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Mock
    lateinit var mainRepo: AuthRepository

    @Before
    open fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = AuthViewModel(mainRepo)
    }

    @After
    fun cleanUp() {

    }
}