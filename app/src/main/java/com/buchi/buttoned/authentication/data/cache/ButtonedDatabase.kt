package com.buchi.buttoned.authentication.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.buchi.buttoned.authentication.model.User

@Database(entities = [User::class], version = 1, exportSchema = true)
abstract class ButtonedDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}