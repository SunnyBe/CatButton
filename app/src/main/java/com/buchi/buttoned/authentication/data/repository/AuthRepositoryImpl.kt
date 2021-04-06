package com.buchi.buttoned.authentication.data.repository

import com.buchi.buttoned.authentication.data.cache.ButtonedDatabase
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.login.LoginViewState
import com.buchi.buttoned.authentication.presentation.signup.SignupViewState
import com.buchi.buttoned.authentication.utils.ResultState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val cache: ButtonedDatabase
) : AuthRepository {

    override fun idleState(): Flow<ResultState<SignupViewState>> {
        return flow { emit(ResultState.data(null, SignupViewState())) }
    }

    override fun currentUser(): Flow<ResultState<LoginViewState>> {
        return flow {
            val user = cache.userDao().user()
            emit(ResultState.data(message = "Fetched user", LoginViewState(user = user)))
        }.onStart {
            emit(ResultState.loading(true))
        }
            .onStart {
                emit(ResultState.loading(true))
            }
            .catch { cause ->
                emit(ResultState.error("System error occurred", cause))
            }
    }

    override fun signUp(user: User): Flow<ResultState<SignupViewState>> {
        return flow {
            cache.userDao().insert(user)
            emit(ResultState.data("User created", SignupViewState(user = user)))
        }
            .onStart {
                emit(ResultState.loading(true))
            }
            .catch { cause ->
                emit(ResultState.error("System error occurred", cause))
            }
            .flowOn(IO)
    }

    override fun login(userName: String, password: String): Flow<ResultState<LoginViewState>> {
        return flow<ResultState<LoginViewState>> {
            val user = cache.userDao().userByUserNameAndPassword(userName, password)
            if (user!= null) {
                user.inSession = true
                cache.userDao().update(user)
                emit(ResultState.data("Fetched", LoginViewState(user = user)))
            } else {
                emit(ResultState.error("No user was found", Throwable("Invalid login credentials")))
            }
        }.onStart {
            emit(ResultState.loading(true))
        }
            .catch { cause ->
                emit(ResultState.error("System error occurred", cause))
            }.flowOn(IO)
    }

    override fun deleteUser(user: User): Flow<Unit> {
        return flow {
            emit(cache.userDao().delete(user))
        }
    }

    override fun updateUser(user: User): Flow<User?> {
        return flow {
            cache.userDao().update(user)
            val updatedUser = cache.userDao().user()
            emit(updatedUser)
        }
            .flowOn(IO)
    }
}