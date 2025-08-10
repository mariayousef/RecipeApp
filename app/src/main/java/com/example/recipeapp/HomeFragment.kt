package com.example.recipeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.query
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var progressBar: ProgressBar
    private var originalRecipeList: List<Meal> = listOf()

    interface ApiService {
        @GET("search.php?f=a")
        suspend fun getMeals(): Response<MealResponse>
    }
    private val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.meal_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recipeAdapter = RecipeAdapter { selectedMeal ->
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(selectedMeal)
            findNavController().navigate(action)
        }
        recyclerView.adapter = recipeAdapter
        fetchMeals()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.search -> {
                    val bundle = Bundle()
                    bundle.putSerializable("recipe_list", ArrayList(originalRecipeList))
                    findNavController().navigate(R.id.search, bundle)
                    true
                }
                R.id.favorite -> {
                    val bundle = Bundle()

                    findNavController().navigate(R.id.favorite, bundle)
                    true
                }
                R.id.homeFragment-> {
                    val bundle = Bundle()

                    findNavController().navigate(R.id.homeFragment, bundle)
                    true
                }
                else -> false
            }
        }

    }

    private fun fetchMeals() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { api.getMeals() }
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {

                    originalRecipeList=response.body()!!.meals
                    recipeAdapter.submitList(originalRecipeList)
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
