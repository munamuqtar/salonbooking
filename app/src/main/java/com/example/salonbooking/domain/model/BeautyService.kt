package com.example.salonbooking.domain.model

data class BeautyService(
    val serviceId: String = "",
    val salonId: String = "",
    val salonName: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val durationMinutes: Int = 45,
    val imageUrls: List<String> = emptyList(),
    val assignedProfessionalIds: List<String> = emptyList(),
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
