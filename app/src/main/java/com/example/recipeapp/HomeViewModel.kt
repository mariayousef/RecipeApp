package com.example.recipeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> get() = _meals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val repository = RecipeRepository()

    init {
        fetchMeals()
    }

    fun fetchMeals() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { repository.getMeals() }
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _meals.value = response.body()!!.meals
                    _error.value = null
                } else {
                    _error.value = "Failed to fetch data"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Error: ${e.message}"
            }
        }
    }
}