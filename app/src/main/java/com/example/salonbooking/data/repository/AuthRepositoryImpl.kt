package com.example.salonbooking.data.repository

import com.example.salonbooking.core.util.Constants
import com.example.salonbooking.core.util.Resource
import com.example.salonbooking.data.model.UserDto
import com.example.salonbooking.domain.model.User
import com.example.salonbooking.domain.model.UserRole
import com.example.salonbooking.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val currentUserState: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                trySend(null)
            } else {
                firestore.collection(Constants.USERS_COLLECTION)
                    .document(firebaseUser.uid)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        val dto = documentSnapshot.toObject(UserDto::class.java)
                        if (dto != null) {
                            trySend(dto.toDomain(isEmailVerified = firebaseUser.isEmailVerified))
                        } else {
                            // Fallback if firestore document hasn't completed yet
                            trySend(
                                User(
                                    uid = firebaseUser.uid,
                                    fullName = firebaseUser.displayName.orEmpty(),
                                    email = firebaseUser.email.orEmpty(),
                                    isEmailVerified = firebaseUser.isEmailVerified
                                )
                            )
                        }
                    }
                    .addOnFailureListener {
                        trySend(
                            User(
                                uid = firebaseUser.uid,
                                fullName = firebaseUser.displayName.orEmpty(),
                                email = firebaseUser.email.orEmpty(),
                                isEmailVerified = firebaseUser.isEmailVerified
                            )
                        )
                    }
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun signIn(email: String, pass: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val authResult = auth.signInWithEmailAndPassword(email.trim(), pass).await()
            val firebaseUser = authResult.user
                ?: throw IllegalStateException("Authentication failed: User is null")

            val userSnapshot = firestore.collection(Constants.USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()

            val userDto = userSnapshot.toObject(UserDto::class.java)
            if (userDto != null) {
                if (!userDto.isActive) {
                    auth.signOut()
                    emit(Resource.Error("Your account has been deactivated. Please contact support."))
                    return@flow
                }
                emit(Resource.Success(userDto.toDomain(isEmailVerified = firebaseUser.isEmailVerified)))
            } else {
                // If profile document not found, create base profile
                val newUser = User(
                    uid = firebaseUser.uid,
                    fullName = firebaseUser.displayName.orEmpty(),
                    email = firebaseUser.email.orEmpty(),
                    isEmailVerified = firebaseUser.isEmailVerified
                )
                firestore.collection(Constants.USERS_COLLECTION)
                    .document(firebaseUser.uid)
                    .set(UserDto.fromDomain(newUser))
                    .await()
                emit(Resource.Success(newUser))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Sign in failed. Please check your credentials.", e))
        }
    }

    override fun signUp(
        name: String,
        email: String,
        pass: String,
        phone: String,
        role: UserRole
    ): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            // Guard: Public registration cannot create ADMIN role
            val safeRole = if (role == UserRole.ADMIN) UserRole.CUSTOMER else role

            val authResult = auth.createUserWithEmailAndPassword(email.trim(), pass).await()
            val firebaseUser = authResult.user
                ?: throw IllegalStateException("Sign up failed: User is null")

            // Update display name in Firebase Auth
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name.trim())
                .build()
            firebaseUser.updateProfile(profileUpdates).await()

            // Automatically send email verification
            try {
                firebaseUser.sendEmailVerification().await()
            } catch (_: Exception) {
                // Non-fatal if verification email send encounters network delay
            }

            val newUser = User(
                uid = firebaseUser.uid,
                fullName = name.trim(),
                email = email.trim(),
                phone = phone.trim(),
                photoUrl = "",
                role = safeRole,
                isActive = true,
                isEmailVerified = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            // Save user document in Firestore
            firestore.collection(Constants.USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(UserDto.fromDomain(newUser))
                .await()

            emit(Resource.Success(newUser))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Registration failed. Please try again.", e))
        }
    }

    override fun sendPasswordReset(email: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.sendPasswordResetEmail(email.trim()).await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to send reset email.", e))
        }
    }

    override fun sendEmailVerification(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val user = auth.currentUser ?: throw IllegalStateException("No user logged in")
            user.sendEmailVerification().await()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to send verification email.", e))
        }
    }

    override fun reloadUser(): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val firebaseUser = auth.currentUser ?: throw IllegalStateException("No user logged in")
            firebaseUser.reload().await()

            val doc = firestore.collection(Constants.USERS_COLLECTION)
                .document(firebaseUser.uid)
                .get()
                .await()
            val dto = doc.toObject(UserDto::class.java)
            if (dto != null) {
                emit(Resource.Success(dto.toDomain(isEmailVerified = firebaseUser.isEmailVerified)))
            } else {
                emit(Resource.Success(User(uid = firebaseUser.uid, email = firebaseUser.email.orEmpty(), isEmailVerified = firebaseUser.isEmailVerified)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to reload user.", e))
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}
