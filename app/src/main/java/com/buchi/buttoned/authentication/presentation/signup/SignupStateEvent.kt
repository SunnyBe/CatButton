package com.buchi.buttoned.authentication.presentation.signup

import com.buchi.buttoned.authentication.model.User

/**
 * All possible events that are opened to be triggered from the view [SignUpFragment]
 * This is a component of MVI to make sure all possible request from the view are handled.
 */
sealed class SignupStateEvent {
    object Idle : SignupStateEvent()
    class SignUp(val user: User) : SignupStateEvent()
}
