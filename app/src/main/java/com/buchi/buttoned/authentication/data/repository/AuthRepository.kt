package com.buchi.buttoned.authentication.data.repository

import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.login.LoginViewState
import com.buchi.buttoned.authentication.presentation.signup.SignupViewState
import com.buchi.buttoned.authentication.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(userName: String, password: String): Flow<ResultState<LoginViewState>>
    fun currentUser(): Flow<ResultState<LoginViewState>>
    fun signUp(user: User): Flow<ResultState<SignupViewState>>
    fun idleState(): Flow<ResultState<SignupViewState>>
    fun deleteUser(user: User): Flow<Unit>
    fun updateUser(user: User): Flow<User?>
}