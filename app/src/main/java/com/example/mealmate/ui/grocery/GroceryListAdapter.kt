package com.example.mealmate.ui.grocery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mealmate.data.entity.GroceryItem
import com.example.mealmate.databinding.ItemGroceryBinding

class GroceryListAdapter(
    private val onItemClick: (GroceryItem) -> Unit,
    private val onItemChecked: (GroceryItem) -> Unit,
    private val onItemEdit: (GroceryItem) -> Unit,
    private val onItemDelete: (GroceryItem) -> Unit
) : ListAdapter<GroceryItem, GroceryListAdapter.GroceryViewHolder>(GroceryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val binding = ItemGroceryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GroceryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GroceryViewHolder(
        private val binding: ItemGroceryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GroceryItem) {
    binding.apply {
        root.setOnClickListener {
            onItemClick(item)
        }
        checkBoxItem.isChecked = item.isPurchased
        textViewName.text = item.name
        textViewQuantity.text = "Qty: ${item.quantity}"

        checkBoxItem.setOnClickListener {
            onItemChecked(item)
        }
        buttonEdit.setOnClickListener {
            onItemEdit(item)
        }
        buttonDelete.setOnClickListener {
            onItemDelete(item)
        }
    }
}
    }
}

private class GroceryDiffCallback : DiffUtil.ItemCallback<GroceryItem>() {
    override fun areItemsTheSame(oldItem: GroceryItem, newItem: GroceryItem) = 
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GroceryItem, newItem: GroceryItem) = 
        oldItem == newItem
} 