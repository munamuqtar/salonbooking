package com.example.salonbooking.features.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.core.navigation.Screen
import com.example.salonbooking.domain.model.UserRole
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    authState: AuthState,
    onNavigateToNext: (String) -> Unit
) {
    val scale = remember { Animatable(0.7f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        delay(1200)

        val destination = when {
            authState.isLoggedIn && authState.user != null -> {
                when (authState.user.role) {
                    UserRole.SALON_OWNER -> Screen.SalonDashboard.route
                    UserRole.ADMIN -> Screen.AdminDashboard.route
                    else -> Screen.MainCustomer.route
                }
            }
            else -> Screen.Onboarding.route
        }
        onNavigateToNext(destination)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale.value)
        ) {
            Surface(
                modifier = Modifier.size(110.dp),
                shape = CircleShape,
                color = BrandSecondary,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "✨",
                        fontSize = 48.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "BeautyHub",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Multi-Salon Beauty & Henna Booking",
                style = MaterialTheme.typography.bodyLarge,
                color = BrandTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Zero queues • Choose your professional",
                style = MaterialTheme.typography.bodySmall,
                color = BrandAccent,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
