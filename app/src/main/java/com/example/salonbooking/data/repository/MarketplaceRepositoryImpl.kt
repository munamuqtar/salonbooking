package com.example.salonbooking.data.repository

import com.example.salonbooking.core.util.Constants
import com.example.salonbooking.core.util.Resource
import com.example.salonbooking.data.model.CategoryDto
import com.example.salonbooking.data.model.ProfessionalDto
import com.example.salonbooking.data.model.SalonDto
import com.example.salonbooking.data.model.ServiceDto
import com.example.salonbooking.domain.model.*
import com.example.salonbooking.domain.repository.MarketplaceRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketplaceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : MarketplaceRepository {

    // Seeded beauty categories matching prompt specifications
    private val defaultCategories = listOf(
        Category("cat_henna", "Henna & Bridal Henna", "Intricate traditional & modern bridal henna designs", "✨", "https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=600&q=80", true, 1),
        Category("cat_nails", "Manicure & Pedicure", "Classic & spa hand and foot luxury treatments", "💅", "https://images.unsplash.com/photo-1519014816548-bf5fe059798b?auto=format&fit=crop&w=600&q=80", true, 2),
        Category("cat_nail_art", "Nail Extensions & Art", "Gel, acrylic, polygel, and custom 3D nail artistry", "💎", "https://images.unsplash.com/photo-1604654894610-df63bc536371?auto=format&fit=crop&w=600&q=80", true, 3),
        Category("cat_hair_cut", "Haircuts & Hairstyling", "Designer cuts, blowouts, updos, and styling", "✂️", "https://images.unsplash.com/photo-1562322140-8baeececf3df?auto=format&fit=crop&w=600&q=80", true, 4),
        Category("cat_hair_care", "Hair Washing & Treatments", "Keratin, botox, deep conditioning, and scalp care", "🧴", "https://images.unsplash.com/photo-1527799820374-dcf8d9d4a388?auto=format&fit=crop&w=600&q=80", true, 5),
        Category("cat_makeup", "Makeup & Bridal Makeup", "Full glam, soft glam, bridal, and editorial looks", "💄", "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=600&q=80", true, 6),
        Category("cat_massage", "Body Massage", "Swedish, deep tissue, hot stone, and aromatherapy", "💆‍♀️", "https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=600&q=80", true, 7),
        Category("cat_body_care", "Body Washing & Care", "Moroccan bath, body scrub, and exfoliation", "🫧", "https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=600&q=80", true, 8),
        Category("cat_skincare", "Facials & Skincare", "HydraFacial, anti-aging, chemical peel, and glow therapy", "🧖‍♀️", "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=600&q=80", true, 9),
        Category("cat_waxing", "Waxing & Threading", "Full body waxing, precision brow shaping", "🌿", "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&w=600&q=80", true, 10),
        Category("cat_bridal_pkg", "Bridal Beauty Packages", "All-inclusive royal bridal transformations", "👑", "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&w=600&q=80", true, 11)
    )

    private val defaultBanners = listOf(
        Banner("b1", "Royal Bridal Henna 20% Off", "Book specialist Amina for your big day", "Bridal Season", "https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=800&q=80", "CATEGORY", "cat_henna"),
        Banner("b2", "HydraFacial Glow Week", "Zero queue guarantee at Maison de Beaute", "Exclusive", "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=800&q=80", "SALON", "salon_1"),
        Banner("b3", "Nail Couture & Russian Manicure", "Choose nail artist Elena for bespoke art", "Trending", "https://images.unsplash.com/photo-1604654894610-df63bc536371?auto=format&fit=crop&w=800&q=80", "CATEGORY", "cat_nail_art")
    )

    private val defaultSalons = listOf(
        Salon(
            salonId = "salon_1",
            ownerId = "owner_1",
            name = "Maison de Beauté Luxury Lounge",
            description = "Premier beauty lounge specializing in royal bridal henna, French skincare, and bespoke hair styling. Dedicated private suites.",
            logoUrl = "https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=300&q=80",
            coverImageUrl = "https://images.unsplash.com/photo-1527799820374-dcf8d9d4a388?auto=format&fit=crop&w=1000&q=80",
            phone = "+971 4 394 8888",
            address = "Villa 14, Al Wasl Road, Jumeirah",
            city = "Dubai",
            openingHours = "10:00 AM - 09:00 PM",
            approvalStatus = "APPROVED",
            isActive = true,
            ratingAverage = 4.9,
            ratingCount = 142,
            startingPrice = 45.0,
            servicesSummary = listOf("Bridal Henna", "HydraFacial", "Gel Extensions", "Hair Botox"),
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=800&q=80"
            ),
            categoryIds = listOf("cat_henna", "cat_skincare", "cat_nails", "cat_hair_cut", "cat_bridal_pkg")
        ),
        Salon(
            salonId = "salon_2",
            ownerId = "owner_2",
            name = "Amina Henna & Beauty Studio",
            description = "Internationally acclaimed henna design sanctuary. Organic Rajasthani henna, jagua gel, and bridal transformations.",
            logoUrl = "https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=300&q=80",
            coverImageUrl = "https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=1000&q=80",
            phone = "+971 4 228 1122",
            address = "Floor 2, City Walk Boulevard",
            city = "Dubai",
            openingHours = "09:30 AM - 08:30 PM",
            approvalStatus = "APPROVED",
            isActive = true,
            ratingAverage = 5.0,
            ratingCount = 318,
            startingPrice = 35.0,
            servicesSummary = listOf("Bridal Henna", "Arabic Mehendi", "Moroccan Bath", "Manicure"),
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&w=800&q=80"
            ),
            categoryIds = listOf("cat_henna", "cat_bridal_pkg", "cat_body_care")
        ),
        Salon(
            salonId = "salon_3",
            ownerId = "owner_3",
            name = "L'Atelier Hair & Nail Sanctuary",
            description = "High-fashion haircuts, balayage master colorists, and precision European Russian manicure nail extensions.",
            logoUrl = "https://images.unsplash.com/photo-1519014816548-bf5fe059798b?auto=format&fit=crop&w=300&q=80",
            coverImageUrl = "https://images.unsplash.com/photo-1562322140-8baeececf3df?auto=format&fit=crop&w=1000&q=80",
            phone = "+971 4 456 7890",
            address = "Marina Gate 2, Dubai Marina",
            city = "Dubai",
            openingHours = "10:00 AM - 10:00 PM",
            approvalStatus = "APPROVED",
            isActive = true,
            ratingAverage = 4.8,
            ratingCount = 96,
            startingPrice = 50.0,
            servicesSummary = listOf("Russian Manicure", "Nail Extensions", "Balayage", "Blowdry"),
            galleryUrls = listOf(
                "https://images.unsplash.com/photo-1604654894610-df63bc536371?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1562322140-8baeececf3df?auto=format&fit=crop&w=800&q=80"
            ),
            categoryIds = listOf("cat_nails", "cat_nail_art", "cat_hair_cut", "cat_hair_care")
        )
    )

    private val defaultProfessionals = listOf(
        Professional(
            professionalId = "prof_1",
            salonId = "salon_2",
            salonName = "Amina Henna & Beauty Studio",
            displayName = "Amina Al-Mansoor",
            bio = "Master Henna Artist with 12 years specializing in Gulf, Indian, and contemporary bridal lace henna.",
            photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80",
            specialty = "Bridal Henna & Arabic Art",
            portfolioUrls = listOf(
                "https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=800&q=80",
                "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&w=800&q=80"
            ),
            serviceIds = listOf("srv_henna_1", "srv_henna_2"),
            workingHours = mapOf("Monday-Saturday" to "10:00 - 19:00"),
            ratingAverage = 5.0,
            ratingCount = 280,
            startingPrice = 60.0
        ),
        Professional(
            professionalId = "prof_2",
            salonId = "salon_1",
            salonName = "Maison de Beauté Luxury Lounge",
            displayName = "Fatima Zahra",
            bio = "Licensed aesthetician and skin therapist certified in medical HydraFacial and anti-pigmentation skincare.",
            photoUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?auto=format&fit=crop&w=400&q=80",
            specialty = "Clinical Facials & Skincare",
            portfolioUrls = listOf(
                "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=800&q=80"
            ),
            serviceIds = listOf("srv_skin_1", "srv_skin_2"),
            workingHours = mapOf("Sunday-Friday" to "10:00 - 18:00"),
            ratingAverage = 4.9,
            ratingCount = 114,
            startingPrice = 85.0
        ),
        Professional(
            professionalId = "prof_3",
            salonId = "salon_3",
            salonName = "L'Atelier Hair & Nail Sanctuary",
            displayName = "Elena Rostova",
            bio = "Master Nail Artist trained in Moscow. Expert in dry Russian e-file manicure and intricate hand-painted nail extensions.",
            photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
            specialty = "Russian Manicure & Nail Extensions",
            portfolioUrls = listOf(
                "https://images.unsplash.com/photo-1604654894610-df63bc536371?auto=format&fit=crop&w=800&q=80"
            ),
            serviceIds = listOf("srv_nail_1", "srv_nail_2"),
            workingHours = mapOf("Tuesday-Sunday" to "11:00 - 20:00"),
            ratingAverage = 4.95,
            ratingCount = 89,
            startingPrice = 55.0
        ),
        Professional(
            professionalId = "prof_4",
            salonId = "salon_1",
            salonName = "Maison de Beauté Luxury Lounge",
            displayName = "Layla Kabbani",
            bio = "Celebrity bridal makeup artist. Enhancing natural radiance with premium long-wear cosmetics.",
            photoUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=400&q=80",
            specialty = "Bridal & Glam Makeup",
            portfolioUrls = listOf(
                "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=800&q=80"
            ),
            serviceIds = listOf("srv_makeup_1"),
            workingHours = mapOf("Monday-Saturday" to "09:00 - 17:00"),
            ratingAverage = 4.88,
            ratingCount = 67,
            startingPrice = 110.0
        )
    )

    private val defaultServices = listOf(
        BeautyService(
            serviceId = "srv_henna_1",
            salonId = "salon_2",
            salonName = "Amina Henna & Beauty Studio",
            categoryId = "cat_henna",
            categoryName = "Henna & Bridal Henna",
            name = "Royal Bridal Henna (Both Hands & Feet)",
            description = "Elaborate bespoke bridal design extending to elbows and mid-calf. Includes organic dark-stain aftercare oil.",
            price = 180.0,
            durationMinutes = 150,
            imageUrls = listOf("https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=800&q=80"),
            assignedProfessionalIds = listOf("prof_1")
        ),
        BeautyService(
            serviceId = "srv_henna_2",
            salonId = "salon_2",
            salonName = "Amina Henna & Beauty Studio",
            categoryId = "cat_henna",
            categoryName = "Henna & Bridal Henna",
            name = "Arabic Floral Henna (Both Palms & Back)",
            description = "Modern Gulf floral motifs and negative-space shading.",
            price = 60.0,
            durationMinutes = 45,
            imageUrls = listOf("https://images.unsplash.com/photo-1563245372-f21724e3856d?auto=format&fit=crop&w=800&q=80"),
            assignedProfessionalIds = listOf("prof_1")
        ),
        BeautyService(
            serviceId = "srv_skin_1",
            salonId = "salon_1",
            salonName = "Maison de Beauté Luxury Lounge",
            categoryId = "cat_skincare",
            categoryName = "Facials & Skincare",
            name = "HydraFacial Deluxe Glow Treatment",
            description = "Deep cleansing, vortex extraction, hyaluronic infusion, and LED light therapy for instantaneous radiance.",
            price = 120.0,
            durationMinutes = 60,
            imageUrls = listOf("https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?auto=format&fit=crop&w=800&q=80"),
            assignedProfessionalIds = listOf("prof_2")
        ),
        BeautyService(
            serviceId = "srv_nail_1",
            salonId = "salon_3",
            salonName = "L'Atelier Hair & Nail Sanctuary",
            categoryId = "cat_nail_art",
            categoryName = "Nail Extensions & Art",
            name = "Russian Gel Extensions + Chrome Art",
            description = "Flawless diamond cuticle cleanup with sculptured gel extensions and custom mirror chrome finish.",
            price = 95.0,
            durationMinutes = 90,
            imageUrls = listOf("https://images.unsplash.com/photo-1604654894610-df63bc536371?auto=format&fit=crop&w=800&q=80"),
            assignedProfessionalIds = listOf("prof_3")
        ),
        BeautyService(
            serviceId = "srv_makeup_1",
            salonId = "salon_1",
            salonName = "Maison de Beauté Luxury Lounge",
            categoryId = "cat_makeup",
            categoryName = "Makeup & Bridal Makeup",
            name = "Glam Evening & Event Makeup",
            description = "Flawless contoured base, Mink lashes, sculpted brows, and waterproof setting.",
            price = 110.0,
            durationMinutes = 60,
            imageUrls = listOf("https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=800&q=80"),
            assignedProfessionalIds = listOf("prof_4")
        )
    )

    override fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading)
        try {
            val snapshot = firestore.collection(Constants.CATEGORIES_COLLECTION)
                .whereEqualTo("isActive", true)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val list = snapshot.documents.mapNotNull { it.toObject(CategoryDto::class.java)?.toDomain() }
                    .sortedBy { it.sortOrder }
                emit(Resource.Success(list))
            } else {
                emit(Resource.Success(defaultCategories))
            }
        } catch (_: Exception) {
            emit(Resource.Success(defaultCategories))
        }
    }

    override fun getBanners(): Flow<Resource<List<Banner>>> = flow {
        emit(Resource.Loading)
        emit(Resource.Success(defaultBanners))
    }

    override fun getSalons(categoryId: String?, city: String?): Flow<Resource<List<Salon>>> = flow {
        emit(Resource.Loading)
        try {
            val snapshot = firestore.collection(Constants.SALONS_COLLECTION)
                .whereEqualTo("approvalStatus", "APPROVED")
                .whereEqualTo("isActive", true)
                .get()
                .await()

            var list = if (!snapshot.isEmpty) {
                snapshot.documents.mapNotNull { it.toObject(SalonDto::class.java)?.toDomain() }
            } else {
                defaultSalons
            }

            if (!categoryId.isNullOrBlank()) {
                list = list.filter { it.categoryIds.contains(categoryId) }
            }
            if (!city.isNullOrBlank()) {
                list = list.filter { it.city.equals(city, ignoreCase = true) }
            }

            emit(Resource.Success(list))
        } catch (_: Exception) {
            var filtered = defaultSalons
            if (!categoryId.isNullOrBlank()) {
                filtered = filtered.filter { it.categoryIds.contains(categoryId) }
            }
            if (!city.isNullOrBlank()) {
                filtered = filtered.filter { it.city.equals(city, ignoreCase = true) }
            }
            emit(Resource.Success(filtered))
        }
    }

    override fun getSalonDetails(salonId: String): Flow<Resource<Salon>> = flow {
        emit(Resource.Loading)
        try {
            val doc = firestore.collection(Constants.SALONS_COLLECTION)
                .document(salonId)
                .get()
                .await()

            val salonDto = doc.toObject(SalonDto::class.java)
            if (salonDto != null) {
                emit(Resource.Success(salonDto.toDomain()))
            } else {
                val fallback = defaultSalons.firstOrNull { it.salonId == salonId }
                    ?: defaultSalons.first()
                emit(Resource.Success(fallback))
            }
        } catch (_: Exception) {
            val fallback = defaultSalons.firstOrNull { it.salonId == salonId } ?: defaultSalons.first()
            emit(Resource.Success(fallback))
        }
    }

    override fun getServicesForSalon(salonId: String): Flow<Resource<List<BeautyService>>> = flow {
        emit(Resource.Loading)
        try {
            val snapshot = firestore.collection(Constants.SERVICES_COLLECTION)
                .whereEqualTo("salonId", salonId)
                .whereEqualTo("isActive", true)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val list = snapshot.documents.mapNotNull { it.toObject(ServiceDto::class.java)?.toDomain() }
                emit(Resource.Success(list))
            } else {
                val list = defaultServices.filter { it.salonId == salonId }
                emit(Resource.Success(list.ifEmpty { defaultServices }))
            }
        } catch (_: Exception) {
            val list = defaultServices.filter { it.salonId == salonId }
            emit(Resource.Success(list.ifEmpty { defaultServices }))
        }
    }

    override fun getProfessionalsForSalon(salonId: String): Flow<Resource<List<Professional>>> = flow {
        emit(Resource.Loading)
        try {
            val snapshot = firestore.collection(Constants.PROFESSIONALS_COLLECTION)
                .whereEqualTo("salonId", salonId)
                .whereEqualTo("isActive", true)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val list = snapshot.documents.mapNotNull { it.toObject(ProfessionalDto::class.java)?.toDomain() }
                emit(Resource.Success(list))
            } else {
                val list = defaultProfessionals.filter { it.salonId == salonId }
                emit(Resource.Success(list.ifEmpty { defaultProfessionals }))
            }
        } catch (_: Exception) {
            val list = defaultProfessionals.filter { it.salonId == salonId }
            emit(Resource.Success(list.ifEmpty { defaultProfessionals }))
        }
    }

    override fun getTopProfessionals(specialty: String?): Flow<Resource<List<Professional>>> = flow {
        emit(Resource.Loading)
        var list = defaultProfessionals
        if (!specialty.isNullOrBlank()) {
            list = list.filter { it.specialty.contains(specialty, ignoreCase = true) }
        }
        emit(Resource.Success(list))
    }

    override fun getProfessionalDetails(professionalId: String): Flow<Resource<Professional>> = flow {
        emit(Resource.Loading)
        try {
            val doc = firestore.collection(Constants.PROFESSIONALS_COLLECTION)
                .document(professionalId)
                .get()
                .await()

            val dto = doc.toObject(ProfessionalDto::class.java)
            if (dto != null) {
                emit(Resource.Success(dto.toDomain()))
            } else {
                val fallback = defaultProfessionals.firstOrNull { it.professionalId == professionalId }
                    ?: defaultProfessionals.first()
                emit(Resource.Success(fallback))
            }
        } catch (_: Exception) {
            val fallback = defaultProfessionals.firstOrNull { it.professionalId == professionalId }
                ?: defaultProfessionals.first()
            emit(Resource.Success(fallback))
        }
    }

    override fun getServiceDetails(serviceId: String): Flow<Resource<BeautyService>> = flow {
        emit(Resource.Loading)
        try {
            val doc = firestore.collection(Constants.SERVICES_COLLECTION)
                .document(serviceId)
                .get()
                .await()

            val dto = doc.toObject(ServiceDto::class.java)
            if (dto != null) {
                emit(Resource.Success(dto.toDomain()))
            } else {
                val fallback = defaultServices.firstOrNull { it.serviceId == serviceId }
                    ?: defaultServices.first()
                emit(Resource.Success(fallback))
            }
        } catch (_: Exception) {
            val fallback = defaultServices.firstOrNull { it.serviceId == serviceId }
                ?: defaultServices.first()
            emit(Resource.Success(fallback))
        }
    }

    override fun searchMarketplace(
        query: String,
        city: String?,
        categoryId: String?
    ): Flow<Resource<SearchResults>> = flow {
        emit(Resource.Loading)
        val q = query.trim().lowercase()

        val matchingSalons = defaultSalons.filter { salon ->
            val matchesQuery = q.isEmpty() ||
                    salon.name.lowercase().contains(q) ||
                    salon.description.lowercase().contains(q) ||
                    salon.servicesSummary.any { it.lowercase().contains(q) }
            val matchesCity = city.isNullOrBlank() || salon.city.equals(city, ignoreCase = true)
            val matchesCat = categoryId.isNullOrBlank() || salon.categoryIds.contains(categoryId)
            matchesQuery && matchesCity && matchesCat
        }

        val matchingProfessionals = defaultProfessionals.filter { prof ->
            val matchesQuery = q.isEmpty() ||
                    prof.displayName.lowercase().contains(q) ||
                    prof.specialty.lowercase().contains(q) ||
                    prof.bio.lowercase().contains(q)
            matchesQuery
        }

        val matchingServices = defaultServices.filter { srv ->
            val matchesQuery = q.isEmpty() ||
                    srv.name.lowercase().contains(q) ||
                    srv.description.lowercase().contains(q) ||
                    srv.categoryName.lowercase().contains(q)
            val matchesCat = categoryId.isNullOrBlank() || srv.categoryId == categoryId
            matchesQuery && matchesCat
        }

        emit(Resource.Success(SearchResults(
            salons = matchingSalons,
            professionals = matchingProfessionals,
            services = matchingServices
        )))
    }
}
