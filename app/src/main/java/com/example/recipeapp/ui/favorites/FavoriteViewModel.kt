package com.example.recipeapp.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.Meal
import com.example.recipeapp.data.favorites.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FavoriteRepository(application)

    val favoriteMeals: LiveData<List<Meal>> = repository.getAllFavorites().map { entities ->
        entities.map { it.toMeal() }
    }

    fun addToFavorites(meal: Meal) = viewModelScope.launch {
        repository.addFavorite(meal)
    }

    fun removeFromFavorites(meal: Meal) = viewModelScope.launch {
        repository.removeFavorite(meal)
    }

    fun checkIfFavorite(mealId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            callback(repository.isFavorite(mealId))
        }
    }
}