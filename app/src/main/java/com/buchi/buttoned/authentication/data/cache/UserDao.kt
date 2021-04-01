package com.buchi.buttoned.authentication.data.cache

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
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
    suspend fun user(): User?

    @Update(onConflict = REPLACE)
    fun update(user: User): Int?

    @Delete
    fun delete(user: User)
}