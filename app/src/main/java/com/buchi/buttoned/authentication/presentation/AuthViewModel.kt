package com.buchi.buttoned.authentication.presentation

import androidx.lifecycle.ViewModel
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.login.LoginViewState
import com.buchi.buttoned.authentication.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository
): ViewModel() {

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?> get() = _user

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error: MutableStateFlow<String?> = MutableStateFlow(null)
    val error: StateFlow<String?> get() = _error

    init {
        // Todo Fetch user and update [_user] stateFlow
        authRepo.currentUser()
    }

    fun dataStateChanged(dataState: ResultState<*>) {
        _error.value = dataState.error?.getContentIfNotHandled()?.message
        _loading.value = dataState.loading
    }

}