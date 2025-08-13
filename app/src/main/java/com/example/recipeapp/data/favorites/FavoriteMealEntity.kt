package com.example.recipeapp.data.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipeapp.Meal

@Entity(tableName = "favorite_meals")
data class FavoriteMealEntity(
    @PrimaryKey val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strInstructions: String?,
    val strYoutube: String?
) {
    fun toMeal() = Meal(
        idMeal, strMeal, strMealThumb, strInstructions, strYoutube, isFavorite = true
    )
}