package com.example.salonbooking.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salonbooking.core.designsystem.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    authState: AuthState,
    onResendVerificationEmail: () -> Unit,
    onCheckVerificationStatus: () -> Unit,
    onProceedToProfileSetup: () -> Unit
) {
    LaunchedEffect(authState.user?.isEmailVerified) {
        if (authState.user?.isEmailVerified == true) {
            onProceedToProfileSetup()
        }
    }

    Scaffold(
        containerColor = BrandBackground,
        topBar = {
            TopAppBar(
                title = { Text("Email Verification", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandBackground)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Surface(
                    modifier = Modifier.size(110.dp),
                    shape = CircleShape,
                    color = BrandSecondary,
                    shadowElevation = 6.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.MarkEmailRead,
                            contentDescription = "Verify Email",
                            tint = BrandPrimary,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Verify Your Email",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = BrandText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "We have sent a confirmation link to:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandTextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = authState.user?.email ?: "your registered email",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Please check your inbox (and spam folder) and click the verification link to secure your account.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                if (authState.successMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        color = BrandSuccess.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = authState.successMessage,
                            color = BrandSuccess,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Button(
                    onClick = onCheckVerificationStatus,
                    enabled = !authState.isLoading,
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
                            text = "I've Verified My Email",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = BrandSurface
                        )
                    }
                }

                OutlinedButton(
                    onClick = onResendVerificationEmail,
                    enabled = !authState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, BrandPrimary)
                ) {
                    Text(
                        text = "Resend Verification Email",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandPrimary
                    )
                }

                TextButton(
                    onClick = onProceedToProfileSetup,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Continue to Profile Setup",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrandAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
