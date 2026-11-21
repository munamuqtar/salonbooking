package com.example.salonbooking

import com.example.salonbooking.domain.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Test

class UserRoleTest {

    @Test
    fun `fromString maps valid string to UserRole correctly`() {
        assertEquals(UserRole.CUSTOMER, UserRole.fromString("CUSTOMER"))
        assertEquals(UserRole.SALON_OWNER, UserRole.fromString("SALON_OWNER"))
        assertEquals(UserRole.PROFESSIONAL, UserRole.fromString("PROFESSIONAL"))
        assertEquals(UserRole.ADMIN, UserRole.fromString("ADMIN"))
    }

    @Test
    fun `fromString case insensitivity works`() {
        assertEquals(UserRole.CUSTOMER, UserRole.fromString("customer"))
        assertEquals(UserRole.SALON_OWNER, UserRole.fromString("salon_owner"))
        assertEquals(UserRole.ADMIN, UserRole.fromString("Admin"))
    }

    @Test
    fun `fromString invalid or null string defaults safely to CUSTOMER`() {
        assertEquals(UserRole.CUSTOMER, UserRole.fromString(null))
        assertEquals(UserRole.CUSTOMER, UserRole.fromString("SUPER_HACKER"))
    }
}
