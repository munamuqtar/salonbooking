package com.example.salonbooking.features.professionals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun ProfessionalDetailsScreen(
    professional: Professional?,
    services: List<BeautyService>,
    isLoading: Boolean,
    onServiceSelectedForBooking: (BeautyService, Professional) -> Unit,
    onNavigateBack: () -> Unit
) {
    if (isLoading || professional == null) {
        LoadingComponent(message = "Loading specialist profile...")
        return
    }

    Scaffold(
        containerColor = BrandBackground,
        topBar = {
            TopAppBar(
                title = { Text(professional.displayName, style = MaterialTheme.typography.titleLarge) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Profile Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = BrandSurface,
                    shadowElevation = 3.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileImage(
                            imageUrl = professional.photoUrl,
                            size = 96.dp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = professional.displayName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrandText
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = BrandPrimary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = professional.specialty,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = null,
                                tint = BrandAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = professional.salonName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = BrandTextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RatingComponent(
                                rating = professional.ratingAverage,
                                reviewCount = professional.ratingCount
                            )

                            PriceComponent(price = professional.startingPrice)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = professional.bio,
                            style = MaterialTheme.typography.bodyMedium,
                            color = BrandText,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Portfolio Showcase
            if (professional.portfolioUrls.isNotEmpty()) {
                item {
                    Text(
                        text = "Portfolio & Recent Artistry",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(professional.portfolioUrls) { imgUrl ->
                            AsyncImage(
                                model = imgUrl,
                                contentDescription = "Portfolio image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                        }
                    }
                }
            }

            // Working Hours & Availability
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BrandSecondary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = BrandPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Working Hours & Schedule",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = BrandText
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        professional.workingHours.forEach { (day, hours) ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = day, style = MaterialTheme.typography.bodySmall, color = BrandText)
                                Text(text = hours, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = BrandPrimary)
                            }
                        }
                    }
                }
            }

            // Services Offered by This Professional
            item {
                Text(
                    text = "Services by ${professional.displayName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BrandText
                )
                Text(
                    text = "Choose a service to book directly with this specialist:",
                    style = MaterialTheme.typography.bodySmall,
                    color = BrandTextSecondary
                )
            }

            items(services) { service ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BrandSurface,
                    shadowElevation = 2.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = service.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = BrandTextSecondary,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                PriceComponent(price = service.price, prefix = "")
                                Text(
                                    text = "• ${service.durationMinutes} min",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BrandTextSecondary
                                )
                            }
                        }

                        Button(
                            onClick = { onServiceSelectedForBooking(service, professional) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                        ) {
                            Text("Book Slot")
                        }
                    }
                }
            }
        }
    }
}
