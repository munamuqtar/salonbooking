package com.example.salonbooking

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SalonBookingApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
