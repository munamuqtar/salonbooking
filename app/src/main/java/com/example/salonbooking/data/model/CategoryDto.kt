package com.example.salonbooking.data.model

import com.example.salonbooking.domain.model.Category
import com.google.firebase.firestore.PropertyName

data class CategoryDto(
    @get:PropertyName("categoryId") @set:PropertyName("categoryId")
    var categoryId: String = "",

    @get:PropertyName("name") @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("iconEmoji") @set:PropertyName("iconEmoji")
    var iconEmoji: String = "✨",

    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl")
    var imageUrl: String = "",

    @get:PropertyName("isActive") @set:PropertyName("isActive")
    var isActive: Boolean = true,

    @get:PropertyName("sortOrder") @set:PropertyName("sortOrder")
    var sortOrder: Int = 0
) {
    fun toDomain(): Category {
        return Category(
            categoryId = categoryId,
            name = name,
            description = description,
            iconEmoji = iconEmoji,
            imageUrl = imageUrl,
            isActive = isActive,
            sortOrder = sortOrder
        )
    }

    companion object {
        fun fromDomain(category: Category): CategoryDto {
            return CategoryDto(
                categoryId = category.categoryId,
                name = category.name,
                description = category.description,
                iconEmoji = category.iconEmoji,
                imageUrl = category.imageUrl,
                isActive = category.isActive,
                sortOrder = category.sortOrder
            )
        }
    }
}
