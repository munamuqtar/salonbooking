package com.example.salonbooking.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salonbooking.core.util.Resource
import com.example.salonbooking.data.remote.image.ImageKitUploadManager
import com.example.salonbooking.domain.model.User
import com.example.salonbooking.domain.model.UserRole
import com.example.salonbooking.domain.repository.AuthRepository
import com.example.salonbooking.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val imageKitUploadManager: ImageKitUploadManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.currentUserState.collect { user ->
                _uiState.update {
                    it.copy(
                        user = user,
                        isLoggedIn = user != null
                    )
                }
            }
        }
    }

    fun setSelectedRole(role: UserRole) {
        _uiState.update { it.copy(selectedRole = role) }
    }

    fun signIn(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter both email and password") }
            return
        }

        viewModelScope.launch {
            authRepository.signIn(email, pass).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.data,
                            isLoggedIn = true,
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    fun signUp(name: String, email: String, pass: String, phone: String, role: UserRole) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all required fields") }
            return
        }
        if (pass.length < 6) {
            _uiState.update { it.copy(errorMessage = "Password must be at least 6 characters") }
            return
        }

        viewModelScope.launch {
            authRepository.signUp(name, email, pass, phone, role).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.data,
                            isRegistered = true,
                            isLoggedIn = true,
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your email address") }
            return
        }

        viewModelScope.launch {
            authRepository.sendPasswordReset(email).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            isPasswordResetSent = true,
                            successMessage = "Password reset email sent. Please check your inbox.",
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    fun sendEmailVerification() {
        viewModelScope.launch {
            authRepository.sendEmailVerification().collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEmailSent = true,
                            successMessage = "Verification email sent. Please check your inbox."
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    fun reloadUser() {
        viewModelScope.launch {
            authRepository.reloadUser().collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.data,
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    fun updateProfile(fullName: String, phone: String, photoUrl: String) {
        val currentUser = _uiState.value.user ?: return
        val updatedUser = currentUser.copy(
            fullName = fullName,
            phone = phone,
            photoUrl = photoUrl
        )

        viewModelScope.launch {
            userRepository.updateUserProfile(updatedUser).collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Success -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = updatedUser,
                            successMessage = "Profile updated successfully!"
                        )
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    else -> Unit
                }
            }
        }
    }

    fun uploadProfilePhoto(file: File) {
        val currentUser = _uiState.value.user ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isUploadingPhoto = true, errorMessage = null) }
            val uploadResult = imageKitUploadManager.uploadImage(
                file = file,
                folder = "/users/avatars",
                customFileName = "avatar_${currentUser.uid}.jpg"
            )

            when (uploadResult) {
                is Resource.Success -> {
                    val uploadedUrl = uploadResult.data.url
                    userRepository.updateProfilePhoto(currentUser.uid, uploadedUrl).collect { res ->
                        when (res) {
                            is Resource.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isUploadingPhoto = false,
                                        user = it.user?.copy(photoUrl = uploadedUrl),
                                        successMessage = "Photo uploaded to ImageKit successfully!"
                                    )
                                }
                            }
                            is Resource.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isUploadingPhoto = false,
                                        errorMessage = res.message
                                    )
                                }
                            }
                            else -> Unit
                        }
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isUploadingPhoto = false,
                            errorMessage = uploadResult.message
                        )
                    }
                }
                else -> {
                    _uiState.update { it.copy(isUploadingPhoto = false) }
                }
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _uiState.value = AuthState()
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }
}
