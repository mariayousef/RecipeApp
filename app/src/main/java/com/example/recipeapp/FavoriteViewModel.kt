package com.example.recipeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Repository interface
interface FavoriteRepository {
    suspend fun getFavoriteMeals(): List<Meal>
    suspend fun addFavorite(meal: Meal)
    suspend fun removeFavorite(mealId: String)
}

// Mock implementation
class MockFavoriteRepository : FavoriteRepository {
    private val favorites = mutableListOf<Meal>()

    override suspend fun getFavoriteMeals(): List<Meal> {
        delay(500) // Simulate network/database delay
        return favorites.toList()
    }

    override suspend fun addFavorite(meal: Meal) {
        favorites.add(meal.copy(isFavorite = true))
    }

    override suspend fun removeFavorite(mealId: String) {
        favorites.removeAll { it.idMeal == mealId }
    }
}

class FavoriteViewModel(
    private val repository: FavoriteRepository = MockFavoriteRepository()
) : ViewModel() {

    private val _favoriteMeals = MutableLiveData<List<Meal>>()
    val favoriteMeals: LiveData<List<Meal>> get() = _favoriteMeals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        loadFavoriteMeals()
    }

    fun loadFavoriteMeals() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val meals = repository.getFavoriteMeals()
                _favoriteMeals.value = meals
            } catch (e: Exception) {
                _errorMessage.value = "Could not load favorites. Please try again."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeFromFavorites(meal: Meal) {
        viewModelScope.launch {
            try {
                repository.removeFavorite(meal.idMeal)
                loadFavoriteMeals()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to remove favorite."
            }
        }
    }

    fun addToFavorites(meal: Meal) {
        viewModelScope.launch {
            try {
                repository.addFavorite(meal)
                loadFavoriteMeals()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add favorite."
            }
        }
    }
}
