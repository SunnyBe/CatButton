package com.buchi.buttoned.authentication.presentation.login

/**
 * All possible events that are opened to be triggered from the view [LoginFragment]
 * This is a component of MVI to make sure all possible request from the view are handled.
 */
sealed class LoginStateEvent {
    object Idle : LoginStateEvent()
    class Login(val userName: String, val password: String) : LoginStateEvent()
}
