package com.example.salonbooking.domain.model

data class Banner(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val badge: String = "Special Offer",
    val imageUrl: String = "",
    val targetType: String = "CATEGORY", // "CATEGORY", "SALON", "SERVICE"
    val targetId: String = ""
)
