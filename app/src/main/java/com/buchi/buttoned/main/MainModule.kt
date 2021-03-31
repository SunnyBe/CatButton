package com.buchi.buttoned.main

import androidx.lifecycle.ViewModel
import com.buchi.buttoned.authentication.presentation.AuthViewModel
import com.buchi.buttoned.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(ActivityComponent::class)
object MainModule {
}

@Module
@InstallIn(ActivityComponent::class)
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(mainViewModel: MainViewModel): ViewModel
}