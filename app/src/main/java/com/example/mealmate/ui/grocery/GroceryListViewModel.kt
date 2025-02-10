package com.example.mealmate.ui.grocery

import androidx.lifecycle.*
import com.example.mealmate.data.dao.GroceryItemDao
import com.example.mealmate.data.entity.GroceryItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GroceryListViewModel(
    private val dao: GroceryItemDao,
    private val userId: Long
) : ViewModel() {

    private val _groceryItems = MutableLiveData<List<GroceryItem>>()
    val groceryItems: LiveData<List<GroceryItem>> = _groceryItems

    private val _selectedItem = MutableLiveData<GroceryItem?>()
    val selectedItem: LiveData<GroceryItem?> = _selectedItem

    init {
        loadItems()
        // Add initial items if the list is empty
        viewModelScope.launch {
            if (dao.getAllItems(userId).first().isEmpty()) {
                addItem("Milk", 1)
                addItem("Bread", 2)
                addItem("Eggs", 12)
                addItem("Butter", 1)
            }
        }
    }

    private fun loadItems() {
        viewModelScope.launch {
            dao.getAllItems(userId).collect { items ->
                _groceryItems.value = items
            }
        }
    }

    fun addItem(name: String, quantity: Int) {
        viewModelScope.launch {
            val item = GroceryItem(
                name = name,
                quantity = quantity,
                userId = userId
            )
            dao.insert(item)
        }
    }

    fun updateItem(item: GroceryItem) {
        viewModelScope.launch {
            dao.update(item)
        }
    }

    fun deleteItem(item: GroceryItem) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }

    fun toggleItemPurchased(itemId: Long) {
        viewModelScope.launch {
            val items = dao.getAllItems(userId).first()
            val item = items.find { it.id == itemId }
            item?.let {
                val updatedItem = it.copy(isPurchased = !it.isPurchased)
                dao.update(updatedItem)
            }
        }
    }

    fun getItemById(itemId: Long): LiveData<GroceryItem?> {
        return liveData {
            val items = dao.getAllItems(userId).first()
            emit(items.find { it.id == itemId })
        }
    }
} 