package com.buchi.buttoned.authentication.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?> get() = _user

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error: MutableStateFlow<String?> = MutableStateFlow(null)
    val error: StateFlow<String?> get() = _error

    init {
        authRepo.currentUser().mapLatest { ds ->
            _user.value = ds.data?.getContentIfNotHandled()?.user
        }.launchIn(viewModelScope)

    }

    fun dataStateChanged(dataState: ResultState<*>) {
        Log.d(javaClass.simpleName, "DataState Updated: $dataState")
        _error.value = dataState.error?.getContentIfNotHandled()?.message
        _loading.value = dataState.loading
    }

}