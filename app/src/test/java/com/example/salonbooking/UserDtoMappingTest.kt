package com.example.salonbooking

import com.example.salonbooking.data.model.UserDto
import com.example.salonbooking.domain.model.User
import com.example.salonbooking.domain.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserDtoMappingTest {

    @Test
    fun `toDomain converts UserDto correctly`() {
        val dto = UserDto(
            uid = "user_123",
            fullName = "Amina Beauty",
            email = "amina@example.com",
            phone = "+1234567890",
            photoUrl = "https://ik.imagekit.io/demo/avatar.jpg",
            role = "SALON_OWNER",
            isActive = true,
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val domain = dto.toDomain(isEmailVerified = true)

        assertEquals("user_123", domain.uid)
        assertEquals("Amina Beauty", domain.fullName)
        assertEquals("amina@example.com", domain.email)
        assertEquals(UserRole.SALON_OWNER, domain.role)
        assertTrue(domain.isActive)
        assertTrue(domain.isEmailVerified)
    }

    @Test
    fun `fromDomain converts User correctly`() {
        val user = User(
            uid = "cust_999",
            fullName = "Sarah Customer",
            email = "sarah@example.com",
            phone = "+9876543210",
            photoUrl = "",
            role = UserRole.CUSTOMER,
            isActive = true,
            isEmailVerified = false,
            createdAt = 5000L,
            updatedAt = 6000L
        )

        val dto = UserDto.fromDomain(user)

        assertEquals("cust_999", dto.uid)
        assertEquals("Sarah Customer", dto.fullName)
        assertEquals("sarah@example.com", dto.email)
        assertEquals("CUSTOMER", dto.role)
        assertTrue(dto.isActive)
    }
}
