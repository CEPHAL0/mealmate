package com.example.mealmate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grocery_items")
data class GroceryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var quantity: Int,
    var isPurchased: Boolean = false,
    val userId: Long
) 