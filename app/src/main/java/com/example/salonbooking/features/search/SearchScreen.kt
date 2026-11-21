package com.example.salonbooking.features.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.core.designsystem.components.*
import com.example.salonbooking.domain.model.SearchResults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: SearchResults,
    onQueryChange: (String) -> Unit,
    onSalonClick: (String) -> Unit,
    onProfessionalClick: (String) -> Unit,
    onServiceClick: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filterTabs = listOf("All", "Salons", "Artists", "Services")

    Scaffold(
        containerColor = BrandBackground,
        topBar = {
            TopAppBar(
                title = {
                    SearchBarComponent(
                        query = searchQuery,
                        onQueryChange = onQueryChange,
                        placeholderText = "Search salons, artists, services...",
                        modifier = Modifier.fillMaxWidth()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterTabs.forEach { tab ->
                    val isSelected = selectedFilter == tab
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = tab },
                        label = { Text(tab) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandPrimary,
                            selectedLabelColor = BrandSurface,
                            containerColor = BrandSurfaceVariant,
                            labelColor = BrandTextSecondary
                        )
                    )
                }
            }

            if (searchQuery.isNotEmpty() && searchResults.isEmpty) {
                EmptyStateComponent(
                    title = "No Results Found",
                    description = "We couldn't find any match for \"$searchQuery\". Try checking the spelling or searching another category."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Salons Section
                    if (selectedFilter == "All" || selectedFilter == "Salons") {
                        if (searchResults.salons.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Salons (${searchResults.salons.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandText
                                )
                            }
                            items(searchResults.salons) { salon ->
                                SalonCard(
                                    salon = salon,
                                    onViewSalonClick = { onSalonClick(salon.salonId) },
                                    onBookAppointmentClick = { onSalonClick(salon.salonId) }
                                )
                            }
                        }
                    }

                    // Professionals Section
                    if (selectedFilter == "All" || selectedFilter == "Artists") {
                        if (searchResults.professionals.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Beauty Professionals (${searchResults.professionals.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandText,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            items(searchResults.professionals) { prof ->
                                ProfessionalCard(
                                    professional = prof,
                                    onSelectProfessionalClick = { onProfessionalClick(prof.professionalId) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // Services Section
                    if (selectedFilter == "All" || selectedFilter == "Services") {
                        if (searchResults.services.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Services (${searchResults.services.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandText,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            items(searchResults.services) { service ->
                                ServiceCard(
                                    service = service,
                                    onServiceClick = { onServiceClick(service.serviceId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
