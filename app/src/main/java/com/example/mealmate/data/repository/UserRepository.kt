package com.example.mealmate.data.repository

import com.example.mealmate.data.entity.User

class UserRepository private constructor() {
    private val users = mutableListOf<User>()

    fun addUser(email: String, password: String, name: String): Boolean {
        if (users.any { it.email == email }) {
            return false // User already exists
        }
        users.add(User(email = email, password = password, name = name))
        return true
    }

    fun login(email: String, password: String): User? {
        return users.find { it.email == email && it.password == password }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository().also { instance = it }
            }
        }
    }
} 