package com.example.finance_project

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object FirebaseAuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success("Sign-up Successful!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success("Login Successful")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout (): Result<String> {
        return try {
            auth.signOut()
            Result.success("Logout successful")
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun getCurrentUserEmail(): String? = auth.currentUser?.email
}