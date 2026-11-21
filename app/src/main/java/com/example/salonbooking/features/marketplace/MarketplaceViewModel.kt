package com.example.salonbooking.features.marketplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salonbooking.core.util.Resource
import com.example.salonbooking.domain.model.Category
import com.example.salonbooking.domain.repository.MarketplaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketplaceViewModel @Inject constructor(
    private val repository: MarketplaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketplaceState())
    val uiState: StateFlow<MarketplaceState> = _uiState.asStateFlow()

    init {
        loadHomeFeed()
    }

    fun loadHomeFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Categories
            repository.getCategories().collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(categories = res.data) }
                }
            }

            // Banners
            repository.getBanners().collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(banners = res.data) }
                }
            }

            // Salons for city
            repository.getSalons(city = _uiState.value.selectedCity).collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(popularSalons = res.data, salonList = res.data) }
                }
            }

            // Top Professionals
            repository.getTopProfessionals().collect { res ->
                if (res is Resource.Success) {
                    _uiState.update {
                        it.copy(
                            topProfessionals = res.data,
                            featuredHennaArtists = res.data.filter { p ->
                                p.specialty.contains("Henna", ignoreCase = true)
                            }
                        )
                    }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun selectCity(city: String) {
        _uiState.update { it.copy(selectedCity = city) }
        loadHomeFeed()
    }

    fun selectCategory(category: Category) {
        _uiState.update { it.copy(selectedCategory = category, isLoading = true) }
        viewModelScope.launch {
            repository.getSalons(categoryId = category.categoryId, city = _uiState.value.selectedCity).collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(salonList = res.data, isLoading = false) }
                }
            }
        }
    }

    fun loadSalonDetails(salonId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            repository.getSalonDetails(salonId).collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(selectedSalon = res.data) }
                }
            }

            repository.getServicesForSalon(salonId).collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(selectedSalonServices = res.data) }
                }
            }

            repository.getProfessionalsForSalon(salonId).collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(selectedSalonProfessionals = res.data, isLoading = false) }
                }
            }
        }
    }

    fun loadProfessionalDetails(professionalId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getProfessionalDetails(professionalId).collect { res ->
                if (res is Resource.Success) {
                    val prof = res.data
                    _uiState.update { it.copy(selectedProfessional = prof) }

                    // Also load salon services assigned to this professional if salonId present
                    if (!prof.salonId.isNullOrBlank()) {
                        repository.getServicesForSalon(prof.salonId).collect { srvRes ->
                            if (srvRes is Resource.Success) {
                                _uiState.update {
                                    it.copy(
                                        selectedSalonServices = srvRes.data,
                                        isLoading = false
                                    )
                                }
                            }
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    fun loadServiceDetails(serviceId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getServiceDetails(serviceId).collect { res ->
                if (res is Resource.Success) {
                    val srv = res.data
                    _uiState.update { it.copy(selectedService = srv) }

                    // Also load available professionals who can perform this service
                    if (srv.salonId.isNotBlank()) {
                        repository.getProfessionalsForSalon(srv.salonId).collect { pRes ->
                            if (pRes is Resource.Success) {
                                val assigned = pRes.data.filter { p ->
                                    p.serviceIds.contains(srv.serviceId) || srv.assignedProfessionalIds.contains(p.professionalId)
                                }
                                _uiState.update {
                                    it.copy(
                                        selectedSalonProfessionals = assigned.ifEmpty { pRes.data },
                                        isLoading = false
                                    )
                                }
                            }
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            repository.searchMarketplace(query, _uiState.value.selectedCity).collect { res ->
                if (res is Resource.Success) {
                    _uiState.update { it.copy(searchResults = res.data) }
                }
            }
        }
    }
}
