package com.example.salonbooking.domain.repository

import com.example.salonbooking.core.util.Resource
import com.example.salonbooking.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(userId: String): Flow<Resource<User>>
    fun updateUserProfile(user: User): Flow<Resource<Unit>>
    fun updateProfilePhoto(userId: String, photoUrl: String): Flow<Resource<Unit>>
}
