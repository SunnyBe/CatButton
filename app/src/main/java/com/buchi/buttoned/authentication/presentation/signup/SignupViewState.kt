package com.buchi.buttoned.authentication.presentation.signup

import com.buchi.buttoned.authentication.model.User

/**
 * View States that are possible to the sign up screen [SignUpFragment]. Data can be fetched from
 * repository through the ViewModel to the view. The availability of any param is processed in the
 * View [SignUpFragment]. An Idle state will have all param set to null. his is a component of the
 * MVI architecture.
 * @param user to be signed up and added to the DB
 */
data class SignupViewState(
    val user: User? = null
)