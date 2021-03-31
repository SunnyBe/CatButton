package com.buchi.buttoned.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.buchi.buttoned.authentication.data.cache.ButtonedDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ButtonedDatabase::class.java, "button_db")
            .build()
}