package com.example.salonbooking.data.remote.image

import com.example.salonbooking.core.util.Resource
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class ImageKitAuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("expire") val expire: Long,
    @SerializedName("signature") val signature: String
)

data class ImageKitUploadResponse(
    @SerializedName("fileId") val fileId: String,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String?,
    @SerializedName("height") val height: Int?,
    @SerializedName("width") val width: Int?,
    @SerializedName("size") val size: Long?
)

@Singleton
class ImageKitUploadManager @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    private val config: ImageKitConfig
) {
    companion object {
        private const val IMAGEKIT_UPLOAD_URL = "https://upload.imagekit.io/api/v1/files/upload"
        private const val MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024 // 5 MB free tier safety limit
        private val ALLOWED_EXTENSIONS = listOf("jpg", "jpeg", "png", "webp")
    }

    /**
     * Uploads an image file to ImageKit using client-side authorized upload.
     */
    suspend fun uploadImage(
        file: File,
        folder: String = "/avatars",
        customFileName: String? = null
    ): Resource<ImageKitUploadResponse> = withContext(Dispatchers.IO) {
        try {
            // Validation 1: File existence
            if (!file.exists()) {
                return@withContext Resource.Error("File does not exist: ${file.path}")
            }

            // Validation 2: File size limit
            if (file.length() > MAX_FILE_SIZE_BYTES) {
                return@withContext Resource.Error("File size exceeds 5MB limit. Please choose a smaller image.")
            }

            // Validation 3: File extension
            val extension = file.extension.lowercase()
            if (extension !in ALLOWED_EXTENSIONS) {
                return@withContext Resource.Error("Unsupported image format: .$extension. Allowed: JPG, PNG, WEBP.")
            }

            val fileName = customFileName ?: "${UUID.randomUUID()}.$extension"

            // Step 1: Obtain Auth Parameters from secure backend (or fallback auth signature)
            val authParams = fetchAuthParameters()

            // Step 2: Build Multipart request to ImageKit Upload API
            val mediaType = when (extension) {
                "png" -> "image/png"
                "webp" -> "image/webp"
                else -> "image/jpeg"
            }.toMediaTypeOrNull()

            val fileRequestBody = file.asRequestBody(mediaType)

            val multipartBuilder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, fileRequestBody)
                .addFormDataPart("fileName", fileName)
                .addFormDataPart("publicKey", config.publicKey)
                .addFormDataPart("signature", authParams.signature)
                .addFormDataPart("expire", authParams.expire.toString())
                .addFormDataPart("token", authParams.token)
                .addFormDataPart("folder", folder)
                .addFormDataPart("useUniqueFileName", "true")

            val request = Request.Builder()
                .url(IMAGEKIT_UPLOAD_URL)
                .post(multipartBuilder.build())
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                return@withContext Resource.Error("ImageKit upload error HTTP ${response.code}: $responseBody")
            }

            val uploadResult = gson.fromJson(responseBody, ImageKitUploadResponse::class.java)
            Resource.Success(uploadResult)
        } catch (e: Exception) {
            Resource.Error("Upload failed: ${e.localizedMessage ?: "Unknown network error"}", e)
        }
    }

    /**
     * Obtains secure authentication parameters from the backend auth endpoint.
     */
    private fun fetchAuthParameters(): ImageKitAuthResponse {
        return try {
            val request = Request.Builder()
                .url(config.authEndpoint)
                .get()
                .build()
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string().orEmpty()
                gson.fromJson(body, ImageKitAuthResponse::class.java)
            } else {
                // Development fallback token structure
                ImageKitAuthResponse(
                    token = UUID.randomUUID().toString(),
                    expire = (System.currentTimeMillis() / 1000) + 1800,
                    signature = "dev_signature_demo"
                )
            }
        } catch (_: Exception) {
            ImageKitAuthResponse(
                token = UUID.randomUUID().toString(),
                expire = (System.currentTimeMillis() / 1000) + 1800,
                signature = "dev_signature_demo"
            )
        }
    }
}
