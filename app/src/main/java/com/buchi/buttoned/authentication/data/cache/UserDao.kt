package com.buchi.buttoned.authentication.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.buchi.buttoned.authentication.model.User

@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    suspend fun userByUserName(username: String): User?

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    suspend fun userById(userId: String): User?

    @Query("SELECT * FROM user WHERE userName=:userName AND password=:password LIMIT 1")
    suspend fun userByUserNameAndPassword(userName: String, password: String): User?

    @Query("SELECT * FROM user")
    fun users(): List<User>

    @Delete
    fun delete(user: User)
}