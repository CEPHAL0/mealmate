package com.example.mealmate.ui.grocery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mealmate.data.dao.GroceryItemDao

class GroceryListViewModelFactory(
    private val dao: GroceryItemDao,
    private val userId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroceryListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GroceryListViewModel(dao, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 