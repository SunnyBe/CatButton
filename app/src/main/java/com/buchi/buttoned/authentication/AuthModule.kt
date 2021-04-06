package com.buchi.buttoned.authentication

import androidx.lifecycle.ViewModel
import com.buchi.buttoned.authentication.data.cache.ButtonedDatabase
import com.buchi.buttoned.authentication.data.repository.AuthRepository
import com.buchi.buttoned.authentication.data.repository.AuthRepositoryImpl
import com.buchi.buttoned.authentication.presentation.AuthViewModel
import com.buchi.buttoned.authentication.presentation.login.LoginViewModel
import com.buchi.buttoned.authentication.presentation.signup.SignUpViewModel
import com.buchi.buttoned.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityComponent::class)
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun authViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(authViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun signUpViewModel(authViewModel: SignUpViewModel): ViewModel
}

@ExperimentalCoroutinesApi
@Module(includes = [AuthViewModelModule::class])
@InstallIn(ActivityComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(cache: ButtonedDatabase): AuthRepository = AuthRepositoryImpl(cache)
}