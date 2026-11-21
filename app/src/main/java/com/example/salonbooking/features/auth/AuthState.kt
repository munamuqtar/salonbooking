package com.example.salonbooking.features.auth

import com.example.salonbooking.domain.model.User
import com.example.salonbooking.domain.model.UserRole

data class AuthState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isUploadingPhoto: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isEmailSent: Boolean = false,
    val isPasswordResetSent: Boolean = false,
    val isRegistered: Boolean = false,
    val isLoggedIn: Boolean = false,
    val selectedRole: UserRole = UserRole.CUSTOMER
)
