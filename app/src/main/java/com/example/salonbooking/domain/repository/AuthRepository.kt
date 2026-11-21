package com.example.salonbooking.domain.repository

import com.example.salonbooking.core.util.Resource
import com.example.salonbooking.domain.model.User
import com.example.salonbooking.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserState: Flow<User?>
    fun isUserLoggedIn(): Boolean
    fun getCurrentUserId(): String?
    fun signIn(email: String, pass: String): Flow<Resource<User>>
    fun signUp(name: String, email: String, pass: String, phone: String, role: UserRole): Flow<Resource<User>>
    fun sendPasswordReset(email: String): Flow<Resource<Unit>>
    fun sendEmailVerification(): Flow<Resource<Unit>>
    fun reloadUser(): Flow<Resource<User>>
    fun signOut()
}
