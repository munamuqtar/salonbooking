package com.example.salonbooking

import com.example.salonbooking.data.model.CategoryDto
import com.example.salonbooking.data.model.ProfessionalDto
import com.example.salonbooking.data.model.SalonDto
import com.example.salonbooking.data.model.ServiceDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarketplaceModelTest {

    @Test
    fun `CategoryDto to Domain and back works properly`() {
        val dto = CategoryDto(
            categoryId = "cat_henna",
            name = "Henna & Bridal Henna",
            description = "Intricate bridal henna designs",
            iconEmoji = "✨",
            imageUrl = "https://example.com/henna.jpg",
            isActive = true,
            sortOrder = 1
        )
        val domain = dto.toDomain()
        assertEquals("cat_henna", domain.categoryId)
        assertEquals("Henna & Bridal Henna", domain.name)
        assertTrue(domain.isActive)

        val backToDto = CategoryDto.fromDomain(domain)
        assertEquals(dto.name, backToDto.name)
        assertEquals(dto.sortOrder, backToDto.sortOrder)
    }

    @Test
    fun `ProfessionalDto to Domain preserves portfolio and services`() {
        val dto = ProfessionalDto(
            professionalId = "prof_1",
            salonId = "salon_1",
            salonName = "Amina Studio",
            displayName = "Amina Al-Mansoor",
            specialty = "Bridal Henna",
            portfolioUrls = listOf("https://example.com/p1.jpg", "https://example.com/p2.jpg"),
            serviceIds = listOf("srv_1", "srv_2"),
            ratingAverage = 4.95,
            ratingCount = 200,
            startingPrice = 50.0
        )
        val domain = dto.toDomain()
        assertEquals("prof_1", domain.professionalId)
        assertEquals("Amina Al-Mansoor", domain.displayName)
        assertEquals(2, domain.portfolioUrls.size)
        assertEquals(2, domain.serviceIds.size)
        assertEquals(4.95, domain.ratingAverage, 0.001)
    }

    @Test
    fun `SalonDto to Domain and ServiceDto mapping work properly`() {
        val salonDto = SalonDto(
            salonId = "salon_99",
            name = "Luxury Lounge",
            city = "Dubai",
            ratingAverage = 4.8,
            startingPrice = 40.0
        )
        val salon = salonDto.toDomain()
        assertEquals("salon_99", salon.salonId)
        assertEquals("Dubai", salon.city)

        val serviceDto = ServiceDto(
            serviceId = "srv_1",
            salonId = "salon_99",
            name = "HydraFacial Deluxe",
            price = 120.0,
            durationMinutes = 60
        )
        val service = serviceDto.toDomain()
        assertEquals(120.0, service.price, 0.001)
        assertEquals(60, service.durationMinutes)
    }
}
