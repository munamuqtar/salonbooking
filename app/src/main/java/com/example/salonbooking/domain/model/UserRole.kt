package com.example.salonbooking.domain.model

enum class UserRole {
    CUSTOMER,
    SALON_OWNER,
    PROFESSIONAL,
    ADMIN;

    companion object {
        fun fromString(value: String?): UserRole {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: CUSTOMER
        }
    }
}
