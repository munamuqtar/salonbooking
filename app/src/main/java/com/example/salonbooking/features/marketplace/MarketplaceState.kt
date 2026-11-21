package com.example.salonbooking.features.marketplace

import com.example.salonbooking.domain.model.*

data class MarketplaceState(
    val categories: List<Category> = emptyList(),
    val banners: List<Banner> = emptyList(),
    val popularSalons: List<Salon> = emptyList(),
    val topProfessionals: List<Professional> = emptyList(),
    val featuredHennaArtists: List<Professional> = emptyList(),
    val popularServices: List<BeautyService> = emptyList(),
    val selectedCity: String = "Dubai",
    val availableCities: List<String> = listOf("Dubai", "Abu Dhabi", "Sharjah", "Doha", "Riyadh"),
    val selectedCategory: Category? = null,
    val salonList: List<Salon> = emptyList(),
    val selectedSalon: Salon? = null,
    val selectedSalonServices: List<BeautyService> = emptyList(),
    val selectedSalonProfessionals: List<Professional> = emptyList(),
    val selectedProfessional: Professional? = null,
    val selectedService: BeautyService? = null,
    val searchQuery: String = "",
    val searchResults: SearchResults = SearchResults(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
