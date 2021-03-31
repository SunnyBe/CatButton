package com.buchi.buttoned.authentication.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
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
): Parcelable