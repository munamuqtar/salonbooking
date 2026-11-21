package com.example.salonbooking.data.repository

import com.example.salonbooking.core.util.Constants
import com.example.salonbooking.core.util.Resource
import com.example.salonbooking.data.model.UserDto
import com.example.salonbooking.domain.model.User
import com.example.salonbooking.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override fun getUserProfile(userId: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val doc = firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            val dto = doc.toObject(UserDto::class.java)
            if (dto != null) {
                emit(Resource.Success(dto.toDomain()))
            } else {
                emit(Resource.Error("User profile not found."))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to load user profile.", e))
        }
    }

    override fun updateUserProfile(user: User): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val updates = mapOf(
                "fullName" to user.fullName,
                "phone" to user.phone,
                "photoUrl" to user.photoUrl,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection(Constants.USERS_COLLECTION)
                .document(user.uid)
                .update(updates)
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to update profile.", e))
        }
    }

    override fun updateProfilePhoto(userId: String, photoUrl: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val updates = mapOf(
                "photoUrl" to photoUrl,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection(Constants.USERS_COLLECTION)
                .document(userId)
                .update(updates)
                .await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to update profile photo.", e))
        }
    }
}
