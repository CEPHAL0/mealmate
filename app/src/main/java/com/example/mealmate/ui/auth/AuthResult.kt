package com.example.mealmate.ui.auth

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
} 