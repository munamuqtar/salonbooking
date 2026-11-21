package com.example.salonbooking.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.core.designsystem.components.EmptyStateComponent
import com.example.salonbooking.core.designsystem.components.ProfileImage
import com.example.salonbooking.domain.model.Category
import com.example.salonbooking.domain.model.User
import com.example.salonbooking.features.marketplace.MarketplaceState
import com.example.salonbooking.features.search.SearchScreen

sealed class BottomNavTab(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavTab("nav_home", "Home", Icons.Default.Home)
    object Explore : BottomNavTab("nav_explore", "Explore", Icons.Default.Search)
    object Bookings : BottomNavTab("nav_bookings", "Bookings", Icons.Default.CalendarToday)
    object Favorites : BottomNavTab("nav_favorites", "Favorites", Icons.Default.Favorite)
    object Profile : BottomNavTab("nav_profile", "Profile", Icons.Default.Person)
}

@Composable
fun MarketplaceBottomNavContainer(
    user: User?,
    marketplaceState: MarketplaceState,
    onSearchClick: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    onViewAllCategoriesClick: () -> Unit,
    onSalonClick: (String) -> Unit,
    onProfessionalClick: (String) -> Unit,
    onServiceClick: (String) -> Unit,
    onCitySelected: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onSignOutClick: () -> Unit
) {
    var currentTab by remember { mutableStateOf<BottomNavTab>(BottomNavTab.Home) }

    val navItems = listOf(
        BottomNavTab.Home,
        BottomNavTab.Explore,
        BottomNavTab.Bookings,
        BottomNavTab.Favorites,
        BottomNavTab.Profile
    )

    Scaffold(
        containerColor = BrandBackground,
        bottomBar = {
            NavigationBar(
                containerColor = BrandSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.height(72.dp)
            ) {
                navItems.forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandPrimary,
                            selectedTextColor = BrandPrimary,
                            indicatorColor = BrandSecondary,
                            unselectedIconColor = BrandTextSecondary,
                            unselectedTextColor = BrandTextSecondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                BottomNavTab.Home -> {
                    HomeScreen(
                        state = marketplaceState,
                        onSearchClick = onSearchClick,
                        onCategoryClick = onCategoryClick,
                        onViewAllCategoriesClick = onViewAllCategoriesClick,
                        onSalonClick = onSalonClick,
                        onProfessionalClick = onProfessionalClick,
                        onServiceClick = onServiceClick,
                        onCitySelected = onCitySelected
                    )
                }
                BottomNavTab.Explore -> {
                    SearchScreen(
                        searchQuery = marketplaceState.searchQuery,
                        searchResults = marketplaceState.searchResults,
                        onQueryChange = onSearchQueryChanged,
                        onSalonClick = onSalonClick,
                        onProfessionalClick = onProfessionalClick,
                        onServiceClick = onServiceClick,
                        onNavigateBack = { currentTab = BottomNavTab.Home }
                    )
                }
                BottomNavTab.Bookings -> {
                    EmptyStateComponent(
                        title = "Your In-Salon Appointments",
                        description = "Ready for Phase 3: Live double-booking protection, calendar timeslot selection, and queue-free arrival tickets.",
                        actionButtonText = "Browse Salons",
                        onActionClick = { currentTab = BottomNavTab.Home }
                    )
                }
                BottomNavTab.Favorites -> {
                    EmptyStateComponent(
                        title = "Favorite Salons & Artists",
                        description = "Save your preferred henna specialists and luxury salons for instant one-tap booking.",
                        actionButtonText = "Discover Artists",
                        onActionClick = { currentTab = BottomNavTab.Home }
                    )
                }
                BottomNavTab.Profile -> {
                    CustomerProfileTab(
                        user = user,
                        onSignOutClick = onSignOutClick
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerProfileTab(
    user: User?,
    onSignOutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            ProfileImage(imageUrl = user?.photoUrl, size = 90.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = user?.fullName?.ifBlank { "Beauty Enthusiast" } ?: "Guest User",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = BrandText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user?.email.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = BrandTextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = BrandSecondary
            ) {
                Text(
                    text = "Role: ${user?.role?.name ?: "CUSTOMER"}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Button(
            onClick = onSignOutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sign Out", fontWeight = FontWeight.Bold, color = BrandSurface)
        }
    }
}
