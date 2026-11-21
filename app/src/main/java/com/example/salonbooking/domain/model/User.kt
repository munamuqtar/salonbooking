package com.example.salonbooking.domain.model

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val photoUrl: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val isActive: Boolean = true,
    val isEmailVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
