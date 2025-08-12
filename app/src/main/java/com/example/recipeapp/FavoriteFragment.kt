package com.example.recipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteFragment : Fragment() {

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteMealsAdapter: FavoriteMealsAdapter

    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)

        setupRecyclerView()
        observeFavoriteMeals()
        observeLoadingState()
        observeErrorMessages()
    }

    private fun setupRecyclerView() {
        favoriteMealsAdapter = FavoriteMealsAdapter(
            onItemClick = { meal ->
                // TODO: Navigate to Meal Detail Fragment / Activity
            },
            onRemoveClick = { meal ->
                favoriteViewModel.removeFromFavorites(meal)
            }
        )

        favoriteRecyclerView.layoutManager = LinearLayoutManager(context)
        favoriteRecyclerView.adapter = favoriteMealsAdapter
    }

    private fun observeFavoriteMeals() {
        favoriteViewModel.favoriteMeals.observe(viewLifecycleOwner, Observer { meals ->
            favoriteMealsAdapter.submitList(meals)
        })
    }

    private fun observeLoadingState() {
        favoriteViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun observeErrorMessages() {
        favoriteViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                // TODO: Show error message (Toast or Snackbar)
            }
        }
    }
}
