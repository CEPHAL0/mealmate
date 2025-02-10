package com.example.mealmate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val password: String? = null,  // Make password optional with default value null
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
) 