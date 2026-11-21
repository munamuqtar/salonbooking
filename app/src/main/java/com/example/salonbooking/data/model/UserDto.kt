package com.example.salonbooking.data.model

import com.example.salonbooking.domain.model.User
import com.example.salonbooking.domain.model.UserRole
import com.google.firebase.firestore.PropertyName

data class UserDto(
    @get:PropertyName("uid") @set:PropertyName("uid")
    var uid: String = "",

    @get:PropertyName("fullName") @set:PropertyName("fullName")
    var fullName: String = "",

    @get:PropertyName("email") @set:PropertyName("email")
    var email: String = "",

    @get:PropertyName("phone") @set:PropertyName("phone")
    var phone: String = "",

    @get:PropertyName("photoUrl") @set:PropertyName("photoUrl")
    var photoUrl: String = "",

    @get:PropertyName("role") @set:PropertyName("role")
    var role: String = UserRole.CUSTOMER.name,

    @get:PropertyName("isActive") @set:PropertyName("isActive")
    var isActive: Boolean = true,

    @get:PropertyName("createdAt") @set:PropertyName("createdAt")
    var createdAt: Long = System.currentTimeMillis(),

    @get:PropertyName("updatedAt") @set:PropertyName("updatedAt")
    var updatedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(isEmailVerified: Boolean = false): User {
        return User(
            uid = uid,
            fullName = fullName,
            email = email,
            phone = phone,
            photoUrl = photoUrl,
            role = UserRole.fromString(role),
            isActive = isActive,
            isEmailVerified = isEmailVerified,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromDomain(user: User): UserDto {
            return UserDto(
                uid = user.uid,
                fullName = user.fullName,
                email = user.email,
                phone = user.phone,
                photoUrl = user.photoUrl,
                role = user.role.name,
                isActive = user.isActive,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}
