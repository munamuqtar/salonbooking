package com.example.salonbooking.domain.model

data class Professional(
    val professionalId: String = "",
    val userId: String? = null,
    val salonId: String? = null,
    val salonName: String = "",
    val displayName: String = "",
    val bio: String = "",
    val photoUrl: String = "",
    val specialty: String = "",
    val portfolioUrls: List<String> = emptyList(),
    val serviceIds: List<String> = emptyList(),
    val workingHours: Map<String, String> = emptyMap(), // e.g. "Monday" -> "09:00 - 18:00"
    val daysOff: List<String> = emptyList(), // e.g. ["Sunday"]
    val ratingAverage: Double = 5.0,
    val ratingCount: Int = 0,
    val startingPrice: Double = 0.0,
    val approvalStatus: String = "APPROVED",
    val isActive: Boolean = true
)
