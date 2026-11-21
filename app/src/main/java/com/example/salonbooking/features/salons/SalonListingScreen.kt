package com.example.salonbooking.features.salons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.core.designsystem.components.*
import com.example.salonbooking.domain.model.Category
import com.example.salonbooking.domain.model.Salon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonListingScreen(
    category: Category?,
    salons: List<Salon>,
    isLoading: Boolean,
    onSalonClick: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = BrandBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = category?.name ?: "All Salons",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BrandText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrandBackground)
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            LoadingComponent()
        } else if (salons.isEmpty()) {
            EmptyStateComponent(
                title = "No Salons Available",
                description = "There are currently no registered salons in this category for your area."
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (category != null) {
                    item {
                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                            color = BrandSecondary,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = category.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = BrandText,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }

                items(salons) { salon ->
                    SalonCard(
                        salon = salon,
                        onViewSalonClick = { onSalonClick(salon.salonId) },
                        onBookAppointmentClick = { onSalonClick(salon.salonId) }
                    )
                }
            }
        }
    }
}
