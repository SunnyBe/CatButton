package com.buchi.buttoned.authentication.data.repository

import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.login.LoginViewState
import com.buchi.buttoned.authentication.presentation.signup.SignupViewState
import com.buchi.buttoned.authentication.utils.ResultState
import kotlinx.coroutines.flow.Flow

/**
 * Repository for authentication processes
 */
interface AuthRepository {
    /**
     * Perform login with specified userName and password.
     * @param userName to login. Must be found in the DB record for successful response.
     * @param password to login with. Must be found in the DB record for successful response.
     */
    fun login(userName: String, password: String): Flow<ResultState<LoginViewState>>

    /**
     * Get the last registered user. This app assumes only one user can be registered and logged in.
     */
    fun currentUser(): Flow<ResultState<LoginViewState>>

    /**
     * Signup process to add a new user to the DB
     * @param user to be added to the DB. This is expected to be a user that was not in the DB.
     */
    fun signUp(user: User): Flow<ResultState<SignupViewState>>

    /**
     * Due to MVI architecture this is a idle state function that returns a ResultState with empty
     * data and a SignupViewState instance where all param is null
     */
    fun idleState(): Flow<ResultState<SignupViewState>>

    /**
     * Delete a user that is already in the DB
     * @param user to be deleted
     */
    fun deleteUser(user: User): Flow<Unit>

    /**
     * Update user record in the DB
     * @param user to be updated
     */
    fun updateUser(user: User): Flow<User?>
}