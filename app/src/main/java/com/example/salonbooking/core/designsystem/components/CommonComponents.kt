package com.example.salonbooking.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.salonbooking.core.designsystem.*
import com.example.salonbooking.core.util.Constants

@Composable
fun LoadingComponent(
    modifier: Modifier = Modifier,
    message: String = "Loading..."
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = BrandPrimary,
                strokeWidth = 3.dp
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = BrandTextSecondary
            )
        }
    }
}

@Composable
fun ErrorComponent(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = "Error",
                tint = BrandError,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = BrandText,
                textAlign = TextAlign.Center
            )
            if (onRetry != null) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Try Again")
                }
            }
        }
    }
}

@Composable
fun EmptyStateComponent(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandBackground)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = BrandText,
                textAlign = TextAlign.Center
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = BrandTextSecondary,
                textAlign = TextAlign.Center
            )
            if (actionButtonText != null && onActionClick != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onActionClick,
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = actionButtonText)
                }
            }
        }
    }
}

@Composable
fun RatingComponent(
    rating: Double,
    reviewCount: Int? = null,
    modifier: Modifier = Modifier,
    iconSize: Dp = 16.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Rating",
            tint = BrandStar,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = String.format("%.1f", rating),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = BrandText
        )
        if (reviewCount != null) {
            Text(
                text = "($reviewCount)",
                style = MaterialTheme.typography.labelSmall,
                color = BrandTextSecondary
            )
        }
    }
}

@Composable
fun PriceComponent(
    price: Double,
    modifier: Modifier = Modifier,
    prefix: String = "From ",
    currency: String = Constants.DEFAULT_CURRENCY
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (prefix.isNotEmpty()) {
            Text(
                text = prefix,
                style = MaterialTheme.typography.bodySmall,
                color = BrandTextSecondary
            )
        }
        Text(
            text = "$currency${String.format("%.2f", price)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = BrandPrimary
        )
    }
}

@Composable
fun BookingStatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.uppercase()) {
        "CONFIRMED" -> BrandSuccess.copy(alpha = 0.15f) to BrandSuccess
        "PENDING" -> BrandAccent.copy(alpha = 0.2f) to BrandAccent
        "COMPLETED" -> BrandPrimary.copy(alpha = 0.15f) to BrandPrimaryDark
        "CHECKED_IN", "IN_PROGRESS" -> Color(0xFF2196F3).copy(alpha = 0.15f) to Color(0xFF1976D2)
        "CANCELLED_BY_CUSTOMER", "CANCELLED_BY_SALON", "REJECTED" -> BrandError.copy(alpha = 0.15f) to BrandError
        else -> BrandSecondary to BrandTextSecondary
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = bgColor
    ) {
        Text(
            text = status.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    contentDescription: String? = "Profile photo"
) {
    val context = LocalContext.current
    Surface(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(1.5.dp, BrandPrimary.copy(alpha = 0.4f), CircleShape),
        color = BrandSecondary
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💅",
                    fontSize = (size.value * 0.4f).sp
                )
            }
        }
    }
}

@Composable
fun ImageUploadComponent(
    currentImageUrl: String?,
    modifier: Modifier = Modifier,
    isUploading: Boolean = false,
    onPickImageClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(BrandSecondary)
            .clickable(enabled = !isUploading) { onPickImageClick() },
        contentAlignment = Alignment.Center
    ) {
        if (!currentImageUrl.isNullOrBlank()) {
            AsyncImage(
                model = currentImageUrl,
                contentDescription = "Uploaded image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Surface(
            color = Color.Black.copy(alpha = 0.35f),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isUploading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Upload Photo",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        text = { Text(text = message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(dismissText)
            }
        },
        containerColor = BrandSurface,
        shape = RoundedCornerShape(16.dp)
    )
}
