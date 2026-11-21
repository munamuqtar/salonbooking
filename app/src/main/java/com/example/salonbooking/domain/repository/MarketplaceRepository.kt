package com.example.salonbooking.domain.repository

import com.example.salonbooking.core.util.Resource
import com.example.salonbooking.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MarketplaceRepository {
    fun getCategories(): Flow<Resource<List<Category>>>
    fun getBanners(): Flow<Resource<List<Banner>>>
    fun getSalons(categoryId: String? = null, city: String? = null): Flow<Resource<List<Salon>>>
    fun getSalonDetails(salonId: String): Flow<Resource<Salon>>
    fun getServicesForSalon(salonId: String): Flow<Resource<List<BeautyService>>>
    fun getProfessionalsForSalon(salonId: String): Flow<Resource<List<Professional>>>
    fun getTopProfessionals(specialty: String? = null): Flow<Resource<List<Professional>>>
    fun getProfessionalDetails(professionalId: String): Flow<Resource<Professional>>
    fun getServiceDetails(serviceId: String): Flow<Resource<BeautyService>>
    fun searchMarketplace(query: String, city: String? = null, categoryId: String? = null): Flow<Resource<SearchResults>>
}
