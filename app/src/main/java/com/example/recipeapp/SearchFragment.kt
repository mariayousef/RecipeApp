package com.example.recipeapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private var originalRecipeList: List<Meal> = listOf() // هتيجي من HomeFragment أو API

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchEditText = view.findViewById(R.id.searchEdittext)
        searchRecyclerView = view.findViewById(R.id.SearchRecycleView)


        recipeAdapter = RecipeAdapter { selectedMeal ->
            val action = SearchFragmentDirections.actionSearchFragmentToRecipeDetailFragment(selectedMeal)
            findNavController().navigate(action)
        }
        searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        searchRecyclerView.adapter = recipeAdapter

        originalRecipeList = arguments?.getSerializable("recipe_list") as? List<Meal> ?: listOf()

        searchEditText.addTextChangedListener { text ->
            val query = text.toString().lowercase()
            val filteredList = originalRecipeList.filter { meal ->
                meal.strMeal.lowercase().contains(query)
            }
            recipeAdapter.submitList(filteredList)
        }

        return view
    }
}

