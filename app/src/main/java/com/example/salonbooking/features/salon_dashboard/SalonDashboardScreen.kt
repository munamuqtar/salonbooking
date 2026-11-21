package com.example.salonbooking.features.salon_dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonDashboardScreen(
    user: User?,
    onSignOutClick: () -> Unit
) {
    Scaffold(
        containerColor = BrandBackground,
        topBar = {
            TopAppBar(
                title = { Text("Salon Owner Portal", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = onSignOutClick) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Sign Out", tint = BrandPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandBackground)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = BrandSecondary,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "💇‍♀️", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Salon Management Portal",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Owner: ${user?.fullName}\nEmail: ${user?.email}\nStatus: Active Owner",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrandText,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Button(
                onClick = onSignOutClick,
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sign Out")
            }
        }
    }
}
