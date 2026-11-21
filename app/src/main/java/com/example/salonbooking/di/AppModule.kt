package com.example.salonbooking.di

import com.example.salonbooking.data.remote.image.ImageKitConfig
import com.example.salonbooking.data.remote.image.ImageKitUploadManager
import com.example.salonbooking.data.repository.AuthRepositoryImpl
import com.example.salonbooking.data.repository.UserRepositoryImpl
import com.example.salonbooking.domain.repository.AuthRepository
import com.example.salonbooking.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideImageKitConfig(): ImageKitConfig {
        return ImageKitConfig()
    }

    @Provides
    @Singleton
    fun provideImageKitUploadManager(
        okHttpClient: OkHttpClient,
        gson: Gson,
        config: ImageKitConfig
    ): ImageKitUploadManager {
        return ImageKitUploadManager(okHttpClient, gson, config)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideMarketplaceRepository(
        firestore: FirebaseFirestore
    ): com.example.salonbooking.domain.repository.MarketplaceRepository {
        return com.example.salonbooking.data.repository.MarketplaceRepositoryImpl(firestore)
    }
}
