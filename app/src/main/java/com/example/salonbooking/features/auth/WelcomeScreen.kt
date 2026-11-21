package com.example.salonbooking.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.domain.model.UserRole

@Composable
fun WelcomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: (UserRole) -> Unit,
    onExploreAsGuest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Center Hero Logo & Heading
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = BrandSecondary,
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "👑", fontSize = 44.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome to BeautyHub",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = BrandText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "The premier marketplace for luxury beauty, henna artistry, and seamless appointment booking.",
                style = MaterialTheme.typography.bodyMedium,
                color = BrandTextSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        // Action Buttons
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Customer Sign Up
            Button(
                onClick = { onNavigateToRegister(UserRole.CUSTOMER) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text(
                    text = "Sign Up as Customer",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = BrandSurface
                )
            }

            // Salon Owner Sign Up / Partner
            OutlinedButton(
                onClick = { onNavigateToRegister(UserRole.SALON_OWNER) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BrandPrimary)
            ) {
                Text(
                    text = "Register as Salon Owner",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandPrimary
                )
            }

            // Existing Account Log In
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(1.dp, BrandBorder, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                color = BrandSurface,
                onClick = onNavigateToLogin
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "I already have an account • Log In",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = BrandText
                    )
                }
            }

            // Guest explore
            TextButton(
                onClick = onExploreAsGuest,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Browse Marketplace as Guest",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandAccent,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
