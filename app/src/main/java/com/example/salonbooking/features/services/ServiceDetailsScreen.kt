package com.example.salonbooking.features.services

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.core.designsystem.components.*
import com.example.salonbooking.domain.model.BeautyService
import com.example.salonbooking.domain.model.Professional

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsScreen(
    service: BeautyService?,
    availableProfessionals: List<Professional>,
    isLoading: Boolean,
    onProfessionalSelectedForBooking: (BeautyService, Professional) -> Unit,
    onNavigateBack: () -> Unit
) {
    if (isLoading || service == null) {
        LoadingComponent(message = "Loading service details...")
        return
    }

    var selectedProfessional by remember(availableProfessionals) {
        mutableStateOf(availableProfessionals.firstOrNull())
    }

    Scaffold(
        containerColor = BrandBackground,
        topBar = {
            TopAppBar(
                title = { Text(service.name, style = MaterialTheme.typography.titleLarge) },
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
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BrandSurface,
                shadowElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Price",
                            style = MaterialTheme.typography.bodySmall,
                            color = BrandTextSecondary
                        )
                        PriceComponent(price = service.price, prefix = "")
                    }

                    Button(
                        onClick = {
                            selectedProfessional?.let {
                                onProfessionalSelectedForBooking(service, it)
                            }
                        },
                        enabled = selectedProfessional != null,
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Text(
                            text = if (selectedProfessional != null) "Select Slot with ${selectedProfessional?.displayName?.substringBefore(" ")}" else "Select Professional",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = BrandSurface
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Service Images
            if (service.imageUrls.isNotEmpty()) {
                item {
                    AsyncImage(
                        model = service.imageUrls.first(),
                        contentDescription = service.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )
                }
            }

            // Title & Badges
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BrandPrimary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = service.categoryName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandText
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Business, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = service.salonName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandTextSecondary
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PriceComponent(price = service.price, prefix = "")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = BrandTextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${service.durationMinutes} minutes duration",
                                style = MaterialTheme.typography.bodySmall,
                                color = BrandTextSecondary
                            )
                        }
                    }
                }
            }

            // Description
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BrandSecondary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Service Details",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = BrandText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = service.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = BrandText,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Select Specific Professional (Mandatory Selection Step)
            item {
                Column {
                    Text(
                        text = "Choose Your Professional For This Service",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandText
                    )
                    Text(
                        text = "The selected specialist will be waiting and ready for you at your booked time.",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrandAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            items(availableProfessionals) { prof ->
                val isSelected = selectedProfessional?.professionalId == prof.professionalId
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) BrandSecondary else BrandSurface,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) BrandPrimary else BrandBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedProfessional = prof }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileImage(
                            imageUrl = prof.photoUrl,
                            size = 54.dp
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = prof.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandText
                            )
                            Text(
                                text = prof.specialty,
                                style = MaterialTheme.typography.bodySmall,
                                color = BrandPrimary
                            )
                            RatingComponent(
                                rating = prof.ratingAverage,
                                reviewCount = prof.ratingCount
                            )
                        }
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedProfessional = prof },
                            colors = RadioButtonDefaults.colors(selectedColor = BrandPrimary)
                        )
                    }
                }
            }
        }
    }
}
