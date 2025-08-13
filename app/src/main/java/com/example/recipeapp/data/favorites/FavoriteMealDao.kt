package com.example.recipeapp.data.favorites

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.data.favorites.FavoriteMealEntity

@Dao
interface FavoriteMealDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertMeal(meal: FavoriteMealEntity)

    @Delete
    suspend fun deleteMeal(meal: FavoriteMealEntity)

    @Query("SELECT * FROM favorite_meals")
    fun getAllFavorites(): LiveData<List<FavoriteMealEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE idMeal = :mealId)")
    suspend fun isFavorite(mealId: String): Boolean
}