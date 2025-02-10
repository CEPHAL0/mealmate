package com.example.mealmate.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealmate.data.entity.User
import com.example.mealmate.data.repository.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = UserRepository.getInstance()
    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult> = _authResult

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty()) {
                _authResult.value = AuthResult.Error("Email and password cannot be empty")
                return@launch
            }

            val user = repository.login(email, password)
            if (user != null) {
                _currentUser.value = user
                _authResult.value = AuthResult.Success
            } else {
                _authResult.value = AuthResult.Error("Invalid credentials")
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                _authResult.value = AuthResult.Error("All fields are required")
                return@launch
            }

            val success = repository.addUser(email, password, name)
            if (success) {
                _authResult.value = AuthResult.Success
            } else {
                _authResult.value = AuthResult.Error("Email already exists")
            }
        }
    }

    fun logout() {
        _currentUser.value = null
    }
} 