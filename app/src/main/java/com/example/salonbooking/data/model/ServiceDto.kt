package com.example.salonbooking.data.model

import com.example.salonbooking.domain.model.BeautyService
import com.google.firebase.firestore.PropertyName

data class ServiceDto(
    @get:PropertyName("serviceId") @set:PropertyName("serviceId")
    var serviceId: String = "",

    @get:PropertyName("salonId") @set:PropertyName("salonId")
    var salonId: String = "",

    @get:PropertyName("salonName") @set:PropertyName("salonName")
    var salonName: String = "",

    @get:PropertyName("categoryId") @set:PropertyName("categoryId")
    var categoryId: String = "",

    @get:PropertyName("categoryName") @set:PropertyName("categoryName")
    var categoryName: String = "",

    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("price") @set:PropertyName("price")
    var price: Double = 0.0,

    @get:PropertyName("durationMinutes") @set:PropertyName("durationMinutes")
    var durationMinutes: Int = 45,

    @get:PropertyName("imageUrls") @set:PropertyName("imageUrls")
    var imageUrls: List<String> = emptyList(),

    @get:PropertyName("assignedProfessionalIds") @set:PropertyName("assignedProfessionalIds")
    var assignedProfessionalIds: List<String> = emptyList(),

    @get:PropertyName("isActive") @set:PropertyName("isActive")
    var isActive: Boolean = true,

    @get:PropertyName("createdAt") @set:PropertyName("createdAt")
    var createdAt: Long = System.currentTimeMillis(),

    @get:PropertyName("updatedAt") @set:PropertyName("updatedAt")
    var updatedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): BeautyService {
        return BeautyService(
            serviceId = serviceId,
            salonId = salonId,
            salonName = salonName,
            categoryId = categoryId,
            categoryName = categoryName,
            name = name,
            description = description,
            price = price,
            durationMinutes = durationMinutes,
            imageUrls = imageUrls,
            assignedProfessionalIds = assignedProfessionalIds,
            isActive = isActive,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromDomain(service: BeautyService): ServiceDto {
            return ServiceDto(
                serviceId = service.serviceId,
                salonId = service.salonId,
                salonName = service.salonName,
                categoryId = service.categoryId,
                categoryName = service.categoryName,
                name = service.name,
                description = service.description,
                price = service.price,
                durationMinutes = service.durationMinutes,
                imageUrls = service.imageUrls,
                assignedProfessionalIds = service.assignedProfessionalIds,
                isActive = service.isActive,
                createdAt = service.createdAt,
                updatedAt = service.updatedAt
            )
        }
    }
}
