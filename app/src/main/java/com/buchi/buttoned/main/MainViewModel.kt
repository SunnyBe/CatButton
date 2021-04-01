package com.buchi.buttoned.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val authRepo: AuthRepository,
) : ViewModel() {

    fun sessionProcess(
        loggedUser: User?,
        scope: CoroutineScope,
        delay: Long,
        action: (User?) -> Unit
    ): Job {
        return scope.launch {
            delay(delay)
            if (loggedUser != null) {
                Log.d(
                    javaClass.simpleName,
                    "Logged ${loggedUser.username} out in ${delay / 1000}sec"
                )
                logout(user = loggedUser, action)
            }
        }
    }

    fun logout(user: User, action: (User?) -> Unit) {
        user.inSession = false
        authRepo.updateUser(user)
            .onCompletion {
                action(user)
            }
            .launchIn(viewModelScope)
    }

}