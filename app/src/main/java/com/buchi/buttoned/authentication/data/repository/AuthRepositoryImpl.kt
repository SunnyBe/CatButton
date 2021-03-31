package com.buchi.buttoned.authentication.data.repository

import com.buchi.buttoned.authentication.data.cache.ButtonedDatabase
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.login.LoginViewState
import com.buchi.buttoned.authentication.presentation.signup.SignupViewState
import com.buchi.buttoned.authentication.utils.ResultState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val cache: ButtonedDatabase
) : AuthRepository {

    override fun idleState(): Flow<ResultState<SignupViewState>> {
        return flow { emit(ResultState.data(null, SignupViewState())) }
    }

    fun currentUser(userId: String?, username: String?): Flow<LoginViewState> {
        return flow {
            val user =
                if (userId != null) {
                    cache.userDao().userById(userId)
                } else if (username != null) {
                    cache.userDao().userByUserName(
                        username
                    )
                } else null

            emit(LoginViewState(user = user))
        }
    }

    override fun currentUser(): Flow<ResultState<LoginViewState>> {
        return flow {
            val user = cache.userDao().users().last()
            emit(ResultState.data(message = "Fetched user", LoginViewState(user = user)))
        }.onStart {
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
//            emit(ResultState.data("User created", SignupViewState(user = User("858595995", "Thomas Muller", "tMuller", "password123"))))
        }.flowOn(IO)
    }

    override fun login(userName: String, password: String): Flow<ResultState<LoginViewState>> {
        return flow {
            val user = cache.userDao().userByUserNameAndPassword(userName, password)
            emit(ResultState.data("Fetched", LoginViewState(user = user)))
        }.onStart {
            emit(ResultState.loading(true))
        }
            .catch { cause ->
                emit(ResultState.error("System error occurred", cause))
            }
    }
}