package com.example.mealmate.data.dao

import androidx.room.*
import com.example.mealmate.data.entity.GroceryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryItemDao {
    @Query("SELECT * FROM grocery_items WHERE userId = :userId")
    fun getAllItems(userId: Long): Flow<List<GroceryItem>>

    @Query("SELECT * FROM grocery_items WHERE id = :itemId")
    suspend fun getItemById(itemId: Long): GroceryItem?

    @Insert
    suspend fun insert(item: GroceryItem)

    @Update
    suspend fun update(item: GroceryItem)

    @Delete
    suspend fun delete(item: GroceryItem)
} 