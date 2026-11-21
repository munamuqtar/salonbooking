package com.example.salonbooking.data.model

import com.example.salonbooking.domain.model.Salon
import com.google.firebase.firestore.PropertyName

data class SalonDto(
    @get:PropertyName("salonId") @set:PropertyName("salonId")
    var salonId: String = "",

    @get:PropertyName("ownerId") @set:PropertyName("ownerId")
    var ownerId: String = "",

    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("logoUrl") @set:PropertyName("logoUrl")
    var logoUrl: String = "",

    @get:PropertyName("coverImageUrl") @set:PropertyName("coverImageUrl")
    var coverImageUrl: String = "",

    @get:PropertyName("phone") @set:PropertyName("phone")
    var phone: String = "",

    @get:PropertyName("address") @set:PropertyName("address")
    var address: String = "",

    @get:PropertyName("city") @set:PropertyName("city")
    var city: String = "Dubai",

    @get:PropertyName("openingHours") @set:PropertyName("openingHours")
    var openingHours: String = "09:00 AM - 09:00 PM",

    @get:PropertyName("approvalStatus") @set:PropertyName("approvalStatus")
    var approvalStatus: String = "APPROVED",

    @get:PropertyName("isActive") @set:PropertyName("isActive")
    var isActive: Boolean = true,

    @get:PropertyName("ratingAverage") @set:PropertyName("ratingAverage")
    var ratingAverage: Double = 5.0,

    @get:PropertyName("ratingCount") @set:PropertyName("ratingCount")
    var ratingCount: Int = 0,

    @get:PropertyName("startingPrice") @set:PropertyName("startingPrice")
    var startingPrice: Double = 0.0,

    @get:PropertyName("servicesSummary") @set:PropertyName("servicesSummary")
    var servicesSummary: List<String> = emptyList(),

    @get:PropertyName("galleryUrls") @set:PropertyName("galleryUrls")
    var galleryUrls: List<String> = emptyList(),

    @get:PropertyName("categoryIds") @set:PropertyName("categoryIds")
    var categoryIds: List<String> = emptyList()
) {
    fun toDomain(): Salon {
        return Salon(
            salonId = salonId,
            ownerId = ownerId,
            name = name,
            description = description,
            logoUrl = logoUrl,
            coverImageUrl = coverImageUrl,
            phone = phone,
            address = address,
            city = city,
            openingHours = openingHours,
            approvalStatus = approvalStatus,
            isActive = isActive,
            ratingAverage = ratingAverage,
            ratingCount = ratingCount,
            startingPrice = startingPrice,
            servicesSummary = servicesSummary,
            galleryUrls = galleryUrls,
            categoryIds = categoryIds
        )
    }

    companion object {
        fun fromDomain(salon: Salon): SalonDto {
            return SalonDto(
                salonId = salon.salonId,
                ownerId = salon.ownerId,
                name = salon.name,
                description = salon.description,
                logoUrl = salon.logoUrl,
                coverImageUrl = salon.coverImageUrl,
                phone = salon.phone,
                address = salon.address,
                city = salon.city,
                openingHours = salon.openingHours,
                approvalStatus = salon.approvalStatus,
                isActive = salon.isActive,
                ratingAverage = salon.ratingAverage,
                ratingCount = salon.ratingCount,
                startingPrice = salon.startingPrice,
                servicesSummary = salon.servicesSummary,
                galleryUrls = salon.galleryUrls,
                categoryIds = salon.categoryIds
            )
        }
    }
}
