package com.buchi.buttoned.authentication.presentation.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.extensions.isValidFullName
import com.buchi.buttoned.authentication.extensions.isValidPassword
import com.buchi.buttoned.authentication.extensions.isValidUserName
import com.buchi.buttoned.authentication.presentation.login.LoginStateEvent
import com.buchi.buttoned.authentication.presentation.login.LoginViewState
import com.buchi.buttoned.authentication.utils.ResultState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepository
): ViewModel() {
    private val TAG = javaClass.simpleName
    // Event Channel: This is updated to perform an event
    private val _stateEvents = MutableStateFlow<SignupStateEvent>(SignupStateEvent.Idle)
    val stateEvents: StateFlow<SignupStateEvent> = _stateEvents
    // DataState is the state of date returned with ViewState
    private val _dataState = MutableStateFlow<ResultState<SignupViewState>>(ResultState(null))
    val dataState: StateFlow<ResultState<SignupViewState>> get() = _dataState
    // ViewState holds what will be processed on the screen based on the value of the data state returned.
    private val _viewState: MutableStateFlow<SignupViewState> = MutableStateFlow(SignupViewState())
    val viewState: StateFlow<SignupViewState> = _viewState

    init {
        stateEvents.flatMapLatest { events ->
            processEvents(events).mapLatest { resp ->
                _dataState.value = resp
            }
        }.launchIn(viewModelScope)
    }

    private fun processEvents(event: SignupStateEvent): Flow<ResultState<SignupViewState>> {
        return when (event) {
            is SignupStateEvent.Idle -> {
                authRepo.idleState()
            }
            is SignupStateEvent.SignUp -> {
                authRepo.signUp(event.user)
            }
        }
    }

    fun setStateEvent(stateEvent: SignupStateEvent) {
        _stateEvents.value = stateEvent
    }

    fun processViewState(viewState: SignupViewState) {
        _viewState.value = viewState
    }

    fun entryCheck(fullName: String, userName: String, password: String): Boolean {
        return fullName.isValidFullName && userName.isValidUserName && password.isValidPassword
    }
}