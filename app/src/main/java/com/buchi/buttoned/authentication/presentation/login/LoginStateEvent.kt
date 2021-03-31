package com.buchi.buttoned.authentication.presentation.login

sealed class LoginStateEvent {
    object Idle : LoginStateEvent()
    class Login(val userName: String, val password: String) : LoginStateEvent()
}
