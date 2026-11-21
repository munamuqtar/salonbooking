package com.example.salonbooking.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.salonbooking.domain.model.UserRole
import com.example.salonbooking.features.admin_dashboard.AdminDashboardScreen
import com.example.salonbooking.features.auth.*
import com.example.salonbooking.features.categories.CategoriesScreen
import com.example.salonbooking.features.home.MarketplaceBottomNavContainer
import com.example.salonbooking.features.marketplace.MarketplaceViewModel
import com.example.salonbooking.features.professionals.ProfessionalDetailsScreen
import com.example.salonbooking.features.professionals.ProfessionalListingScreen
import com.example.salonbooking.features.salons.SalonDetailsScreen
import com.example.salonbooking.features.salons.SalonListingScreen
import com.example.salonbooking.features.salon_dashboard.SalonDashboardScreen
import com.example.salonbooking.features.search.SearchScreen
import com.example.salonbooking.features.services.ServiceDetailsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel(),
    marketplaceViewModel: MarketplaceViewModel = hiltViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val marketplaceState by marketplaceViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                authState = authState,
                onNavigateToNext = { destination ->
                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToRegister = { role ->
                    authViewModel.setSelectedRole(role)
                    navController.navigate(Screen.Register.route)
                },
                onExploreAsGuest = {
                    navController.navigate(Screen.MainCustomer.route)
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                authState = authState,
                onLoginClick = { email, pass ->
                    authViewModel.signIn(email, pass)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = { role ->
                    val destination = when (role) {
                        UserRole.SALON_OWNER -> Screen.SalonDashboard.route
                        UserRole.ADMIN -> Screen.AdminDashboard.route
                        else -> Screen.MainCustomer.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onClearError = {
                    authViewModel.clearMessages()
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                authState = authState,
                initialRole = authState.selectedRole,
                onRegisterClick = { name, email, pass, phone, role ->
                    authViewModel.signUp(name, email, pass, phone, role)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.VerifyEmail.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onClearError = {
                    authViewModel.clearMessages()
                }
            )
        }

        composable(Screen.VerifyEmail.route) {
            VerifyEmailScreen(
                authState = authState,
                onResendVerificationEmail = {
                    authViewModel.sendEmailVerification()
                },
                onCheckVerificationStatus = {
                    authViewModel.reloadUser()
                },
                onProceedToProfileSetup = {
                    navController.navigate(Screen.ProfileSetup.route) {
                        popUpTo(Screen.VerifyEmail.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                authState = authState,
                onSendResetLink = { email ->
                    authViewModel.sendPasswordReset(email)
                },
                onNavigateBack = {
                    authViewModel.clearMessages()
                    navController.popBackStack()
                },
                onClearMessages = {
                    authViewModel.clearMessages()
                }
            )
        }

        composable(Screen.ProfileSetup.route) {
            ProfileSetupScreen(
                authState = authState,
                onSaveProfile = { name, phone, photoUrl ->
                    authViewModel.updateProfile(name, phone, photoUrl)
                },
                onUploadPhoto = { file ->
                    authViewModel.uploadProfilePhoto(file)
                },
                onCompleteSetup = { role ->
                    val destination = when (role) {
                        UserRole.SALON_OWNER -> Screen.SalonDashboard.route
                        UserRole.ADMIN -> Screen.AdminDashboard.route
                        else -> Screen.MainCustomer.route
                    }
                    navController.navigate(destination) {
                        popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                    }
                }
            )
        }

        // Phase 2 Marketplace Main Container
        composable(Screen.MainCustomer.route) {
            MarketplaceBottomNavContainer(
                user = authState.user,
                marketplaceState = marketplaceState,
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onCategoryClick = { category ->
                    marketplaceViewModel.selectCategory(category)
                    navController.navigate(Screen.SalonListing.createRoute(category.categoryId))
                },
                onViewAllCategoriesClick = {
                    navController.navigate(Screen.Categories.route)
                },
                onSalonClick = { salonId ->
                    marketplaceViewModel.loadSalonDetails(salonId)
                    navController.navigate(Screen.SalonDetails.createRoute(salonId))
                },
                onProfessionalClick = { profId ->
                    marketplaceViewModel.loadProfessionalDetails(profId)
                    navController.navigate(Screen.ProfessionalDetails.createRoute(profId))
                },
                onServiceClick = { srvId ->
                    marketplaceViewModel.loadServiceDetails(srvId)
                    navController.navigate(Screen.ServiceDetails.createRoute(srvId))
                },
                onCitySelected = { city ->
                    marketplaceViewModel.selectCity(city)
                },
                onSearchQueryChanged = { query ->
                    marketplaceViewModel.onSearchQueryChanged(query)
                },
                onSignOutClick = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.MainCustomer.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Categories.route) {
            CategoriesScreen(
                categories = marketplaceState.categories,
                onCategorySelected = { category ->
                    marketplaceViewModel.selectCategory(category)
                    navController.navigate(Screen.SalonListing.createRoute(category.categoryId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                searchQuery = marketplaceState.searchQuery,
                searchResults = marketplaceState.searchResults,
                onQueryChange = { query ->
                    marketplaceViewModel.onSearchQueryChanged(query)
                },
                onSalonClick = { salonId ->
                    marketplaceViewModel.loadSalonDetails(salonId)
                    navController.navigate(Screen.SalonDetails.createRoute(salonId))
                },
                onProfessionalClick = { profId ->
                    marketplaceViewModel.loadProfessionalDetails(profId)
                    navController.navigate(Screen.ProfessionalDetails.createRoute(profId))
                },
                onServiceClick = { srvId ->
                    marketplaceViewModel.loadServiceDetails(srvId)
                    navController.navigate(Screen.ServiceDetails.createRoute(srvId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SalonListing.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) {
            SalonListingScreen(
                category = marketplaceState.selectedCategory,
                salons = marketplaceState.salonList,
                isLoading = marketplaceState.isLoading,
                onSalonClick = { salonId ->
                    marketplaceViewModel.loadSalonDetails(salonId)
                    navController.navigate(Screen.SalonDetails.createRoute(salonId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SalonDetails.route,
            arguments = listOf(navArgument("salonId") { type = NavType.StringType })
        ) { backStackEntry ->
            val salonId = backStackEntry.arguments?.getString("salonId").orEmpty()
            SalonDetailsScreen(
                salon = marketplaceState.selectedSalon,
                services = marketplaceState.selectedSalonServices,
                team = marketplaceState.selectedSalonProfessionals,
                isLoading = marketplaceState.isLoading,
                onProfessionalClick = { profId ->
                    marketplaceViewModel.loadProfessionalDetails(profId)
                    navController.navigate(Screen.ProfessionalDetails.createRoute(profId))
                },
                onServiceClick = { srvId ->
                    marketplaceViewModel.loadServiceDetails(srvId)
                    navController.navigate(Screen.ServiceDetails.createRoute(srvId))
                },
                onBookClick = {
                    // Navigate to first service or stay on services tab
                    marketplaceState.selectedSalonServices.firstOrNull()?.let { firstSrv ->
                        marketplaceViewModel.loadServiceDetails(firstSrv.serviceId)
                        navController.navigate(Screen.ServiceDetails.createRoute(firstSrv.serviceId))
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ProfessionalListing.route) {
            ProfessionalListingScreen(
                professionals = marketplaceState.topProfessionals,
                isLoading = marketplaceState.isLoading,
                onProfessionalClick = { profId ->
                    marketplaceViewModel.loadProfessionalDetails(profId)
                    navController.navigate(Screen.ProfessionalDetails.createRoute(profId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ProfessionalDetails.route,
            arguments = listOf(navArgument("professionalId") { type = NavType.StringType })
        ) {
            ProfessionalDetailsScreen(
                professional = marketplaceState.selectedProfessional,
                services = marketplaceState.selectedSalonServices,
                isLoading = marketplaceState.isLoading,
                onServiceSelectedForBooking = { srv, prof ->
                    // Service & Professional chosen; passes forward into Phase 3 booking flow
                    marketplaceViewModel.loadServiceDetails(srv.serviceId)
                    navController.navigate(Screen.ServiceDetails.createRoute(srv.serviceId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ServiceDetails.route,
            arguments = listOf(navArgument("serviceId") { type = NavType.StringType })
        ) {
            ServiceDetailsScreen(
                service = marketplaceState.selectedService,
                availableProfessionals = marketplaceState.selectedSalonProfessionals,
                isLoading = marketplaceState.isLoading,
                onProfessionalSelectedForBooking = { srv, prof ->
                    // Prepared for Phase 3 calendar & slot reservation
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.SalonDashboard.route) {
            SalonDashboardScreen(
                user = authState.user,
                onSignOutClick = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.SalonDashboard.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(
                user = authState.user,
                onSignOutClick = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.AdminDashboard.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
