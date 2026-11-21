package com.example.salonbooking.data.model

import com.example.salonbooking.domain.model.Professional
import com.google.firebase.firestore.PropertyName

data class ProfessionalDto(
    @get:PropertyName("professionalId") @set:PropertyName("professionalId")
    var professionalId: String = "",

    @get:PropertyName("userId") @set:PropertyName("userId")
    var userId: String? = null,

    @get:PropertyName("salonId") @set:PropertyName("salonId")
    var salonId: String? = null,

    @get:PropertyName("salonName") @set:PropertyName("salonName")
    var salonName: String = "",

    @get:PropertyName("displayName") @set:PropertyName("displayName")
    var displayName: String = "",

    @get:PropertyName("bio") @set:PropertyName("bio")
    var bio: String = "",

    @get:PropertyName("photoUrl") @set:PropertyName("photoUrl")
    var photoUrl: String = "",

    @get:PropertyName("specialty") @set:PropertyName("specialty")
    var specialty: String = "",

    @get:PropertyName("portfolioUrls") @set:PropertyName("portfolioUrls")
    var portfolioUrls: List<String> = emptyList(),

    @get:PropertyName("serviceIds") @set:PropertyName("serviceIds")
    var serviceIds: List<String> = emptyList(),

    @get:PropertyName("workingHours") @set:PropertyName("workingHours")
    var workingHours: Map<String, String> = emptyMap(),

    @get:PropertyName("daysOff") @set:PropertyName("daysOff")
    var daysOff: List<String> = emptyList(),

    @get:PropertyName("ratingAverage") @set:PropertyName("ratingAverage")
    var ratingAverage: Double = 5.0,

    @get:PropertyName("ratingCount") @set:PropertyName("ratingCount")
    var ratingCount: Int = 0,

    @get:PropertyName("startingPrice") @set:PropertyName("startingPrice")
    var startingPrice: Double = 0.0,

    @get:PropertyName("approvalStatus") @set:PropertyName("approvalStatus")
    var approvalStatus: String = "APPROVED",

    @get:PropertyName("isActive") @set:PropertyName("isActive")
    var isActive: Boolean = true
) {
    fun toDomain(): Professional {
        return Professional(
            professionalId = professionalId,
            userId = userId,
            salonId = salonId,
            salonName = salonName,
            displayName = displayName,
            bio = bio,
            photoUrl = photoUrl,
            specialty = specialty,
            portfolioUrls = portfolioUrls,
            serviceIds = serviceIds,
            workingHours = workingHours,
            daysOff = daysOff,
            ratingAverage = ratingAverage,
            ratingCount = ratingCount,
            startingPrice = startingPrice,
            approvalStatus = approvalStatus,
            isActive = isActive
        )
    }

    companion object {
        fun fromDomain(professional: Professional): ProfessionalDto {
            return ProfessionalDto(
                professionalId = professional.professionalId,
                userId = professional.userId,
                salonId = professional.salonId,
                salonName = professional.salonName,
                displayName = professional.displayName,
                bio = professional.bio,
                photoUrl = professional.photoUrl,
                specialty = professional.specialty,
                portfolioUrls = professional.portfolioUrls,
                serviceIds = professional.serviceIds,
                workingHours = professional.workingHours,
                daysOff = professional.daysOff,
                ratingAverage = professional.ratingAverage,
                ratingCount = professional.ratingCount,
                startingPrice = professional.startingPrice,
                approvalStatus = professional.approvalStatus,
                isActive = professional.isActive
            )
        }
    }
}
