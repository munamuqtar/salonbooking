package com.example.salonbooking.features.professionals

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
import com.example.salonbooking.domain.model.Professional

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalListingScreen(
    professionals: List<Professional>,
    isLoading: Boolean,
    onProfessionalClick: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = BrandBackground,
        topBar = {
            TopAppBar(
                title = { Text("Beauty Specialists & Artists", style = MaterialTheme.typography.titleLarge) },
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
        } else if (professionals.isEmpty()) {
            EmptyStateComponent(
                title = "No Specialists Found",
                description = "No beauty professionals are available matching your criteria."
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
                item {
                    Text(
                        text = "Choose Your Specific Artist",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrandText
                    )
                    Text(
                        text = "Browse certified beauty experts, view verified portfolios, and book directly without queues.",
                        style = MaterialTheme.typography.bodySmall,
                        color = BrandTextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                items(professionals) { prof ->
                    ProfessionalCard(
                        professional = prof,
                        onSelectProfessionalClick = { onProfessionalClick(prof.professionalId) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
