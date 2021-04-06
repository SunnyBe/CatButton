package com.buchi.buttoned.auth_tests

import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.login.LoginViewState
import com.buchi.buttoned.authentication.presentation.signup.SignupViewState
import com.buchi.buttoned.authentication.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MockAuthRepository : AuthRepository {
    override fun login(userName: String, password: String): Flow<ResultState<LoginViewState>> = flow {
        val user = User("test01", "Thomas Muller", "tMuller", "password123")
        emit(ResultState.data("User created", LoginViewState(user = user)))
    }

    override fun currentUser(): Flow<ResultState<LoginViewState>> = flow {
        val user = User("test01", "Thomas Muller", "tMuller", "password123")
        emit(ResultState.data("User created", LoginViewState(user = user)))
    }

    override fun signUp(user: User): Flow<ResultState<SignupViewState>> = flow {
        emit(ResultState.data("User created", SignupViewState(user = user)))
    }.flowOn(Dispatchers.IO)

    override fun idleState(): Flow<ResultState<SignupViewState>> = flow {
        emit(ResultState.data(null, SignupViewState()))
    }

    override fun updateUser(user: User): Flow<User?> = flow {
        val mUser = User("test01", "Thomas Muller", "tMuller", "password123")
        emit(mUser)
    }

    override fun deleteUser(user: User): Flow<Unit> = flow {
        emit(Unit)
    }

}
