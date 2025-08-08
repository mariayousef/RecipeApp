package com.example.recipeapp

import retrofit2.Response
import retrofit2.http.GET

interface RecipeApi {
    @GET("search.php?f=a")
    suspend fun getMeals(): Response<MealResponse>
}
