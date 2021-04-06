package com.buchi.buttoned

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.buchi.buttoned.authentication.data.cache.ButtonedDatabase
import com.buchi.buttoned.authentication.data.cache.UserDao
import com.buchi.buttoned.authentication.model.User
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MainDbTest {
    private lateinit var userDao: UserDao
    private lateinit var db: ButtonedDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ButtonedDatabase::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        runBlocking {
            val user =
                User(fullName = "Thomas Muller", username = "tMuller", password = "password123")
            userDao.insert(user)
            val byName = userDao.user()
            assertThat(byName, equalTo(user))
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadUserByUserNameAndPassword_returnsValidUser() {
        runBlocking {
            val user = User(fullName = "Thomas Muller", username = "tMuller", password = "password123")
            userDao.insert(user)

            val byName = userDao.userByUserNameAndPassword("tMuller", "password123")
            assertThat(byName, equalTo(user))
        }
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadUserByUserNameAndPassword_returnsEmptyResponse() {
        runBlocking {
            val user = User(fullName = "Thomas Muller", username = "tMuller", password = "password123")
            userDao.insert(user)

            // This is with a wrong password
            val nullName = userDao.userByUserNameAndPassword("tMuller", "password1234")
            assert(nullName == null)
        }
    }


    @Test
    @Throws(Exception::class)
    fun writeAndDeleteDb_confirmsNoUserInDb() {
        runBlocking {
            // Write a new user
            val user = User(fullName = "Thomas Muller", username = "tMuller", password = "password123")
            userDao.insert(user)

            // Delete the user
            userDao.delete(user)

            // Confirm no user in DB
            val confirmUser = userDao.user()
            Assert.assertNull(confirmUser)
        }
    }
}