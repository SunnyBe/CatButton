package com.buchi.buttoned.authentication.presentation.signup

import com.buchi.buttoned.authentication.model.User

sealed class SignupStateEvent {
    object Idle : SignupStateEvent()
    class SignUp(val user: User): SignupStateEvent()
}
