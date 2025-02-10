package com.example.mealmate.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import com.example.mealmate.data.entity.User

class FirebaseAuthRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): User? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                User(
                    id = it.uid.hashCode().toLong(),
                    email = it.email ?: "",
                    name = it.displayName ?: ""
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(email: String, password: String, name: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            )?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getCurrentUser(): User? {
        return auth.currentUser?.let {
            User(
                id = it.uid.hashCode().toLong(),
                email = it.email ?: "",
                name = it.displayName ?: ""
            )
        }
    }

    fun logout() {
        auth.signOut()
    }

    companion object {
        @Volatile
        private var instance: FirebaseAuthRepository? = null

        fun getInstance(): FirebaseAuthRepository {
            return instance ?: synchronized(this) {
                instance ?: FirebaseAuthRepository().also { instance = it }
            }
        }
    }
} 