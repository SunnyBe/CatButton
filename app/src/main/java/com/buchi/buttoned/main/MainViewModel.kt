package com.buchi.buttoned.main

import androidx.lifecycle.ViewModel
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val authRepo: AuthRepository,
//    val mainRepo: MainRepository
): ViewModel() {
    fun logoutUser(user: User): Flow<Unit> {
        return authRepo.deleteUser(user)
    }

}