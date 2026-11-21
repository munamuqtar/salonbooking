package com.example.salonbooking.data.remote.image

import com.example.salonbooking.BuildConfig

data class ImageKitConfig(
    val publicKey: String = BuildConfig.IMAGEKIT_PUBLIC_KEY,
    val urlEndpoint: String = BuildConfig.IMAGEKIT_URL_ENDPOINT,
    val authEndpoint: String = BuildConfig.IMAGEKIT_AUTH_ENDPOINT
) {
    /**
     * Constructs a transformed image URL using ImageKit real-time transformation parameters.
     * Example: width=300, height=300, crop="maintain_ratio", quality=80
     */
    fun getTransformedUrl(
        originalPathOrUrl: String,
        width: Int? = null,
        height: Int? = null,
        quality: Int = 80,
        focus: String = "auto"
    ): String {
        if (originalPathOrUrl.isBlank()) return ""
        val cleanEndpoint = urlEndpoint.trimEnd('/')
        val relativePath = if (originalPathOrUrl.startsWith(cleanEndpoint)) {
            originalPathOrUrl.removePrefix(cleanEndpoint).trimStart('/')
        } else {
            originalPathOrUrl
        }

        val transforms = mutableListOf<String>()
        width?.let { transforms.add("w-$it") }
        height?.let { transforms.add("h-$it") }
        transforms.add("q-$quality")
        transforms.add("fo-$focus")

        val trString = "tr:" + transforms.joinToString(",")
        return "$cleanEndpoint/$trString/$relativePath"
    }
}
