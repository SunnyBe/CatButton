package com.buchi.buttoned.authentication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "fullName")
    val fullName: String? = "",
    @ColumnInfo(name = "userName")
    val username: String? = "",
    @ColumnInfo(name = "password")
    val password: String? = ""
)