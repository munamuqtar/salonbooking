package com.example.salonbooking.core.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object VerifyEmail : Screen("verify_email")
    object ForgotPassword : Screen("forgot_password")
    object ProfileSetup : Screen("profile_setup")
    object MainCustomer : Screen("main_customer")
    object Categories : Screen("categories")
    object Search : Screen("search")
    object SalonListing : Screen("salon_listing/{categoryId}") {
        fun createRoute(categoryId: String) = "salon_listing/$categoryId"
    }
    object SalonDetails : Screen("salon_details/{salonId}") {
        fun createRoute(salonId: String) = "salon_details/$salonId"
    }
    object ProfessionalListing : Screen("professional_listing")
    object ProfessionalDetails : Screen("professional_details/{professionalId}") {
        fun createRoute(professionalId: String) = "professional_details/$professionalId"
    }
    object ServiceDetails : Screen("service_details/{serviceId}") {
        fun createRoute(serviceId: String) = "service_details/$serviceId"
    }
    object SalonDashboard : Screen("salon_dashboard")
    object AdminDashboard : Screen("admin_dashboard")
}
