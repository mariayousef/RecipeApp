package com.example.recipeapp.data.favorites

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.recipeapp.Meal

class FavoriteRepository(context: Context) {
    private val favoriteDao = FavoriteDatabase.getDatabase(context).favoriteMealDao()

    fun getAllFavorites(): LiveData<List<FavoriteMealEntity>> = favoriteDao.getAllFavorites()

    suspend fun addFavorite(meal: Meal) {
        favoriteDao.insertMeal(
            FavoriteMealEntity(
                meal.idMeal, meal.strMeal, meal.strMealThumb, meal.strInstructions, meal.strYoutube
            )
        )
    }

    suspend fun removeFavorite(meal: Meal) {
        favoriteDao.deleteMeal(
            FavoriteMealEntity(
                meal.idMeal, meal.strMeal, meal.strMealThumb, meal.strInstructions, meal.strYoutube
            )
        )
    }

    suspend fun isFavorite(mealId: String): Boolean = favoriteDao.isFavorite(mealId)
}
