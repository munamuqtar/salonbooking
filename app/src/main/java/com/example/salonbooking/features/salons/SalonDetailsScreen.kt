package com.example.salonbooking.features.salons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.salonbooking.domain.model.Salon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonDetailsScreen(
    salon: Salon?,
    services: List<BeautyService>,
    team: List<Professional>,
    isLoading: Boolean,
    onProfessionalClick: (String) -> Unit,
    onServiceClick: (String) -> Unit,
    onBookClick: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    if (isLoading || salon == null) {
        LoadingComponent(message = "Loading salon profile...")
        return
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Services", "Team", "Gallery", "About")

    Scaffold(
        containerColor = BrandBackground,
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
                            text = "Starting from",
                            style = MaterialTheme.typography.bodySmall,
                            color = BrandTextSecondary
                        )
                        PriceComponent(price = salon.startingPrice, prefix = "")
                    }
                    Button(
                        onClick = { onBookClick(salon.salonId) },
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Text(
                            text = "Select Service & Specialist",
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
        ) {
            // Header Image & Top Navigation Overlay
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    AsyncImage(
                        model = salon.coverImageUrl,
                        contentDescription = salon.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Top Back & Share buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BrandSurface.copy(alpha = 0.9f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = BrandText
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = BrandSurface.copy(alpha = 0.9f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(onClick = { /* Share */ }) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = BrandText
                                )
                            }
                        }
                    }

                    // Logo Overlap
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier
                            .size(72.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = 20.dp, y = 36.dp)
                            .border(3.dp, BrandSurface, CircleShape),
                        shadowElevation = 6.dp
                    ) {
                        AsyncImage(
                            model = salon.logoUrl,
                            contentDescription = salon.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(44.dp))
            }

            // Salon Name & Ratings
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = salon.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RatingComponent(
                            rating = salon.ratingAverage,
                            reviewCount = salon.ratingCount
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BrandSuccess.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "Verified Partner",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BrandSuccess,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Address, Phone, Hours
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "${salon.address}, ${salon.city}", style = MaterialTheme.typography.bodySmall, color = BrandTextSecondary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = salon.phone, style = MaterialTheme.typography.bodySmall, color = BrandTextSecondary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = BrandAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = salon.openingHours, style = MaterialTheme.typography.bodySmall, color = BrandTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Tab Selector
            item {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = BrandBackground,
                    contentColor = BrandPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = BrandPrimary
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tab Content
            when (selectedTabIndex) {
                // Tab 0: Services
                0 -> {
                    items(services) { service ->
                        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                            ServiceCard(
                                service = service,
                                onServiceClick = { onServiceClick(service.serviceId) }
                            )
                        }
                    }
                }

                // Tab 1: Team / Beauty Specialists
                1 -> {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            Text(
                                text = "Choose Your Specialist",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandText
                            )
                            Text(
                                text = "Select an artist below to inspect their individual portfolio and book directly with them.",
                                style = MaterialTheme.typography.bodySmall,
                                color = BrandTextSecondary
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                    items(team) { professional ->
                        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                            ProfessionalCard(
                                professional = professional,
                                onSelectProfessionalClick = { onProfessionalClick(professional.professionalId) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Tab 2: Gallery
                2 -> {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            Text(
                                text = "Salon & Portfolio Gallery",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandText
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            val images = salon.galleryUrls.ifEmpty { listOf(salon.coverImageUrl) }
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                images.forEach { imgUrl ->
                                    AsyncImage(
                                        model = imgUrl,
                                        contentDescription = "Salon photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                    )
                                }
                            }
                        }
                    }
                }

                // Tab 3: About & Policies
                3 -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "About The Salon",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandText
                            )
                            Text(
                                text = salon.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = BrandText,
                                lineHeight = 22.sp
                            )

                            Divider(color = BrandBorder)

                            Text(
                                text = "Appointment & Cancellation Policy",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = BrandText
                            )
                            Text(
                                text = "• Zero-queue guarantee: Please arrive 5 minutes prior to your slot.\n" +
                                        "• Free cancellation up to 2 hours before the appointment.\n" +
                                        "• Payment is completed securely in-salon upon service completion.",
                                style = MaterialTheme.typography.bodySmall,
                                color = BrandTextSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
