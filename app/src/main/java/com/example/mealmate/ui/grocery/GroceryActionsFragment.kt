package com.example.mealmate.ui.grocery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mealmate.R
import com.example.mealmate.data.entity.GroceryItem
import com.example.mealmate.databinding.FragmentGroceryActionsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class GroceryActionsFragment : Fragment() {
    private var _binding: FragmentGroceryActionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GroceryListViewModel
    private val args: GroceryActionsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroceryActionsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[GroceryListViewModel::class.java]
        
        setupClickListeners()
        observeItem()
        
        return binding.root
    }

    private fun observeItem() {
        viewModel.getItemById(args.itemId).observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.textViewItemName.text = it.name
                binding.buttonMarkPurchased.text = 
                    if (it.isPurchased) "Mark as Not Purchased" else "Mark as Purchased"
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonMarkPurchased.setOnClickListener {
            viewModel.toggleItemPurchased(args.itemId)
        }

        binding.buttonEdit.setOnClickListener {
            viewModel.getItemById(args.itemId).value?.let { item ->
                showEditDialog(item)
            }
        }

        binding.buttonDelete.setOnClickListener {
            viewModel.getItemById(args.itemId).value?.let { item ->
                showDeleteDialog(item)
            }
        }
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
                findNavController().navigateUp()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}