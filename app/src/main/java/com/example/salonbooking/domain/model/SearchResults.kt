package com.example.salonbooking.domain.model

data class SearchResults(
    val salons: List<Salon> = emptyList(),
    val professionals: List<Professional> = emptyList(),
    val services: List<BeautyService> = emptyList()
) {
    val isEmpty: Boolean
        get() = salons.isEmpty() && professionals.isEmpty() && services.isEmpty()
}
