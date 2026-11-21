package com.example.salonbooking.domain.model

data class Category(
    val categoryId: String = "",
    val name: String = "",
    val description: String = "",
    val iconEmoji: String = "✨",
    val imageUrl: String = "",
    val isActive: Boolean = true,
    val sortOrder: Int = 0
)
