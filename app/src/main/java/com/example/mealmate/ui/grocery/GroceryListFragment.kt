package com.example.mealmate.ui.grocery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealmate.R
import com.example.mealmate.databinding.FragmentGroceryListBinding
import com.example.mealmate.data.entity.GroceryItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.example.mealmate.data.AppDatabase

class GroceryListFragment : Fragment() {
    private var _binding: FragmentGroceryListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GroceryListViewModel
    private lateinit var adapter: GroceryListAdapter

      

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentGroceryListBinding.inflate(inflater, container, false)
            
            // Get database and create factory
            val db = AppDatabase.getInstance(requireContext())
            val factory = GroceryListViewModelFactory(db.groceryItemDao(), getUserId())
            
            viewModel = ViewModelProvider(this, factory)[GroceryListViewModel::class.java]
            setupRecyclerView()
            setupObservers()
            setupClickListeners()
            return binding.root
        }

       private fun setupRecyclerView() {
         adapter = GroceryListAdapter(
        onItemClick = { item ->
            findNavController().navigate(
                GroceryListFragmentDirections.actionGroceryListToGroceryActions(item.id)
            )
        },
        onItemChecked = { item -> 
            viewModel.updateItem(item.copy(isPurchased = !item.isPurchased))
        },
        onItemEdit = { item -> showEditDialog(item) },
        onItemDelete = { item -> showDeleteDialog(item) }
    )
    binding.recyclerViewGrocery.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = this@GroceryListFragment.adapter
    }
    }

    private fun setupObservers() {
        viewModel.groceryItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
        }
    }

    private fun setupClickListeners() {
        binding.fabAddItem.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
          val dialogView = layoutInflater.inflate(R.layout.dialog_grocery_item, null)
    
    MaterialAlertDialogBuilder(requireContext())
        .setTitle("Add Item")
        .setView(dialogView)
        .setPositiveButton("Add") { _, _ ->
            val nameEditText = dialogView.findViewById<TextInputEditText>(R.id.editTextName)
            val quantityEditText = dialogView.findViewById<TextInputEditText>(R.id.editTextQuantity)
            
            val name = nameEditText.text.toString()
            val quantity = quantityEditText.text.toString().toIntOrNull() ?: 1
            
            if (name.isNotBlank()) {
                viewModel.addItem(name, quantity)
            }
        }
        .setNegativeButton("Cancel", null)
        .show()
    }

    private fun showEditDialog(item: GroceryItem) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_grocery_item, null)
        
        // Pre-fill existing values
        dialogView.findViewById<TextInputEditText>(R.id.editTextName).setText(item.name)
        dialogView.findViewById<TextInputEditText>(R.id.editTextQuantity).setText(item.quantity.toString())
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Item")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = dialogView.findViewById<TextInputEditText>(R.id.editTextName).text.toString()
                val quantity = dialogView.findViewById<TextInputEditText>(R.id.editTextQuantity)
                    .text.toString().toIntOrNull() ?: item.quantity
                
                if (name.isNotBlank()) {
                    viewModel.updateItem(item.copy(name = name, quantity = quantity))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteDialog(item: GroceryItem) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete ${item.name}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteItem(item)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun getUserId():Long{
        return 1L
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 