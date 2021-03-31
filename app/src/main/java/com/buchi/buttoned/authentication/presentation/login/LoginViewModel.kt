package com.buchi.buttoned.authentication.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.utils.ResultState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    // Event Channel: This is updated to perform an event
    private val _stateEvents = MutableStateFlow<LoginStateEvent>(LoginStateEvent.Idle)
    private val stateEvents: StateFlow<LoginStateEvent> = _stateEvents

    // DataState is the state of date returned with ViewState
    private val _dataState = MutableStateFlow<ResultState<LoginViewState>>(ResultState.data(null))
    val dataState: StateFlow<ResultState<LoginViewState>> get() = _dataState

    // ViewState holds what will be processed on the screen based on the value of the data state returned.
    private val _viewState: MutableStateFlow<LoginViewState> = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState

    init {
        stateEvents.flatMapLatest { events ->
            processEvents(events).mapLatest { resp ->
                _dataState.value = resp
            }
        }.launchIn(viewModelScope)

    }

    private fun processEvents(event: LoginStateEvent): Flow<ResultState<LoginViewState>> {
        return when (event) {
            is LoginStateEvent.Idle -> {
                flow { emit(ResultState.data(null, LoginViewState())) }
            }
            is LoginStateEvent.Login -> {
                authRepo.login(event.userName, event.password)
            }
        }
    }

    fun setStateEvent(stateEvent: LoginStateEvent) {
        _stateEvents.value = stateEvent
    }

    fun processViewState(viewState: LoginViewState) {
        _viewState.value = viewState
    }

}