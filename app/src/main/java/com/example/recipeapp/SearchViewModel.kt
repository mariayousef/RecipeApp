package com.example.recipeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _filteredMeals = MutableLiveData<List<Meal>>()
    val filteredMeals: LiveData<List<Meal>> get() = _filteredMeals

    private var originalRecipeList: List<Meal> = emptyList()

    fun setOriginalMeals(meals: List<Meal>) {
        originalRecipeList = meals
        _filteredMeals.value = meals // عرض الكل في البداية
    }

    fun filterMeals(query: String) {
        val filteredList = originalRecipeList.filter { meal ->
            meal.strMeal.lowercase().contains(query.lowercase())
        }
        _filteredMeals.value = filteredList
    }
}