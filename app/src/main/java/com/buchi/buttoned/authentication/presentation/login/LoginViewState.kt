package com.buchi.buttoned.authentication.presentation.login

import com.buchi.buttoned.authentication.model.User

/**
 * View States that are possible to the sign up screen [LoginFragment]. Data can be fetched from
 * repository through the ViewModel to the view. The availability of any param is processed in the
 * View [LoginFragment]. An Idle state will have all param set to null. This is a component of the
 * MVI architecture.
 * @param user to be signed up and added to the DB.
 */
data class LoginViewState(
    val user: User? = null
)