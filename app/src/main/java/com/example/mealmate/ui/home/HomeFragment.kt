package com.example.mealmate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mealmate.R
import com.example.mealmate.databinding.FragmentHomeBinding
import com.example.mealmate.ui.auth.AuthViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        observeAuthState()
        setupWelcomeMessage()

        return binding.root
    }

    private fun observeAuthState() {
        authViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    private fun setupWelcomeMessage() {
        authViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            binding.textHome.text = "Welcome ${user?.name}!"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 