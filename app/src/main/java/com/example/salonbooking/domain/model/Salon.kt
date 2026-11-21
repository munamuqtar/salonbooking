package com.example.salonbooking.domain.model

data class Salon(
    val salonId: String = "",
    val ownerId: String = "",
    val name: String = "",
    val description: String = "",
    val logoUrl: String = "",
    val coverImageUrl: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "Dubai",
    val openingHours: String = "09:00 AM - 09:00 PM",
    val approvalStatus: String = "APPROVED",
    val isActive: Boolean = true,
    val ratingAverage: Double = 5.0,
    val ratingCount: Int = 0,
    val startingPrice: Double = 0.0,
    val servicesSummary: List<String> = emptyList(),
    val galleryUrls: List<String> = emptyList(),
    val categoryIds: List<String> = emptyList()
)
