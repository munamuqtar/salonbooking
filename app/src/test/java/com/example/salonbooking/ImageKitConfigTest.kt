package com.example.salonbooking

import com.example.salonbooking.data.remote.image.ImageKitConfig
import org.junit.Assert.assertEquals
import org.junit.Test

class ImageKitConfigTest {

    @Test
    fun `getTransformedUrl generates correct image transformations`() {
        val config = ImageKitConfig(
            publicKey = "public_test",
            urlEndpoint = "https://ik.imagekit.io/demo_beautyhub",
            authEndpoint = "https://example.com/auth"
        )

        val url = config.getTransformedUrl(
            originalPathOrUrl = "salons/cover_123.jpg",
            width = 400,
            height = 300,
            quality = 85,
            focus = "auto"
        )

        val expected = "https://ik.imagekit.io/demo_beautyhub/tr:w-400,h-300,q-85,fo-auto/salons/cover_123.jpg"
        assertEquals(expected, url)
    }

    @Test
    fun `getTransformedUrl handles full url without duplicating endpoint`() {
        val config = ImageKitConfig(
            publicKey = "public_test",
            urlEndpoint = "https://ik.imagekit.io/demo_beautyhub",
            authEndpoint = "https://example.com/auth"
        )

        val original = "https://ik.imagekit.io/demo_beautyhub/artists/amina.jpg"
        val url = config.getTransformedUrl(
            originalPathOrUrl = original,
            width = 150,
            height = 150
        )

        val expected = "https://ik.imagekit.io/demo_beautyhub/tr:w-150,h-150,q-80,fo-auto/artists/amina.jpg"
        assertEquals(expected, url)
    }
}
