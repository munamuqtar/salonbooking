package com.example.salonbooking.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.core.designsystem.components.*
import com.example.salonbooking.domain.model.Category
import com.example.salonbooking.features.marketplace.MarketplaceState

@Composable
fun HomeScreen(
    state: MarketplaceState,
    onSearchClick: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    onViewAllCategoriesClick: () -> Unit,
    onSalonClick: (String) -> Unit,
    onProfessionalClick: (String) -> Unit,
    onServiceClick: (String) -> Unit,
    onCitySelected: (String) -> Unit
) {
    var isCityMenuOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Header: Greeting, Location & Logo
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Find & Book Beauty",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BrandText
                        )
                        // City selector dropdown
                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { isCityMenuOpen = true }
                                    .padding(vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = state.selectedCity,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BrandPrimary
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Change City",
                                    tint = BrandPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = isCityMenuOpen,
                                onDismissRequest = { isCityMenuOpen = false }
                            ) {
                                state.availableCities.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city) },
                                        onClick = {
                                            onCitySelected(city)
                                            isCityMenuOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Logo badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BrandSecondary,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "👑", fontSize = 22.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar Trigger
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BrandSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSearchClick() }
                ) {
                    SearchBarComponent(
                        query = "",
                        onQueryChange = {},
                        placeholderText = "Search salons, henna artists, facials...",
                        modifier = Modifier.clickable { onSearchClick() }
                    )
                }
            }
        }

        // Promotional Banners Carousel
        item {
            Spacer(modifier = Modifier.height(8.dp))
            BannerCarousel(
                banners = state.banners,
                onBannerClick = { /* Handle banner click */ }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Categories Section
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Beauty Categories",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = BrandText
                    )
                    TextButton(onClick = onViewAllCategoriesClick) {
                        Text(
                            text = "See All",
                            style = MaterialTheme.typography.labelLarge,
                            color = BrandPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.categories) { category ->
                        CategoryCard(
                            category = category,
                            onClick = { onCategoryClick(category) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Top-Rated Beauty Professionals & Henna Artists
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Choose Your Specialist",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BrandText
                        )
                        Text(
                            text = "Zero queues • Ready when you arrive",
                            style = MaterialTheme.typography.bodySmall,
                            color = BrandAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(state.topProfessionals) { professional ->
                        ProfessionalCard(
                            professional = professional,
                            onSelectProfessionalClick = { onProfessionalClick(professional.professionalId) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }
        }

        // Popular Salons Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Popular Salons in ${state.selectedCity}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BrandText
                )
                Text(
                    text = "Verified luxury venues with dedicated private suites",
                    style = MaterialTheme.typography.bodySmall,
                    color = BrandTextSecondary
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        items(state.popularSalons) { salon ->
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                SalonCard(
                    salon = salon,
                    onViewSalonClick = { onSalonClick(salon.salonId) },
                    onBookAppointmentClick = { onSalonClick(salon.salonId) }
                )
            }
        }
    }
}
