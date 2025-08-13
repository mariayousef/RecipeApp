package com.example.recipeapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.recipeapp.R

class FavoriteFragment : Fragment() {

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteMealsAdapter: FavoriteMealsAdapter

    private lateinit var favoriteRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView)

        setupRecyclerView()
        observeFavoriteMeals()
    }

    private fun setupRecyclerView() {
        favoriteMealsAdapter = FavoriteMealsAdapter(
            onItemClick = { meal ->
                val action = FavoriteFragmentDirections
                    .actionFavoriteToRecipeDetailFragment(meal)
                findNavController().navigate(action)
            },
            onRemoveClick = { meal ->
                favoriteViewModel.removeFromFavorites(meal)
            }
        )
        favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        favoriteRecyclerView.adapter = favoriteMealsAdapter
    }

    private fun observeFavoriteMeals() {
        favoriteViewModel.favoriteMeals.observe(viewLifecycleOwner) { meals ->
            favoriteMealsAdapter.submitList(meals)
        }
    }
}