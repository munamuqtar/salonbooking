package com.example.salonbooking.features.auth

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.core.designsystem.components.ImageUploadComponent
import com.example.salonbooking.domain.model.UserRole
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    authState: AuthState,
    onSaveProfile: (String, String, String) -> Unit,
    onUploadPhoto: (File) -> Unit,
    onCompleteSetup: (UserRole) -> Unit
) {
    val context = LocalContext.current
    var fullName by remember(authState.user) { mutableStateOf(authState.user?.fullName.orEmpty()) }
    var phone by remember(authState.user) { mutableStateOf(authState.user?.phone.orEmpty()) }
    val photoUrl = authState.user?.photoUrl.orEmpty()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val tempFile = File(context.cacheDir, "temp_avatar_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(tempFile)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                onUploadPhoto(tempFile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        containerColor = BrandBackground,
        topBar = {
            TopAppBar(
                title = { Text("Complete Your Profile", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandBackground)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Personalize Your Account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = BrandText,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Add your details and an avatar so salons and specialists recognize you.",
                style = MaterialTheme.typography.bodyMedium,
                color = BrandTextSecondary,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ImageKit Avatar Upload Component
            ImageUploadComponent(
                currentImageUrl = photoUrl,
                isUploading = authState.isUploadingPhoto,
                onPickImageClick = { imagePickerLauncher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap to upload profile photo (via ImageKit CDN)",
                style = MaterialTheme.typography.labelSmall,
                color = BrandAccent
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Feedback messages
            if (authState.successMessage != null) {
                Surface(
                    color = BrandSuccess.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    Text(
                        text = authState.successMessage,
                        color = BrandSuccess,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            if (authState.errorMessage != null) {
                Surface(
                    color = BrandError.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                ) {
                    Text(
                        text = authState.errorMessage,
                        color = BrandError,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            // Name Field
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = BrandPrimary)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = BrandBorder,
                    focusedLabelColor = BrandPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Field
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = BrandPrimary)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = BrandBorder,
                    focusedLabelColor = BrandPrimary
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    onSaveProfile(fullName, phone, photoUrl)
                    val role = authState.user?.role ?: UserRole.CUSTOMER
                    onCompleteSetup(role)
                },
                enabled = !authState.isLoading && fullName.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                if (authState.isLoading) {
                    CircularProgressIndicator(
                        color = BrandSurface,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Continue to Marketplace",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = BrandSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    val role = authState.user?.role ?: UserRole.CUSTOMER
                    onCompleteSetup(role)
                }
            ) {
                Text(
                    text = "Skip for Now",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandTextSecondary
                )
            }
        }
    }
}
