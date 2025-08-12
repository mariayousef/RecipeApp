package com.example.recipeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private val viewModel: SearchViewModel by viewModels()

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

        // جلب القائمة من الـ Bundle
        val originalRecipeList = arguments?.getSerializable("recipe_list") as? List<Meal> ?: listOf()
        viewModel.setOriginalMeals(originalRecipeList)

        // مراقبة التغييرات في القائمة المصفاة
        viewModel.filteredMeals.observe(viewLifecycleOwner) { meals ->
            recipeAdapter.submitList(meals)
        }

        // تصفية الوصفات بناءً على النص المدخل
        searchEditText.addTextChangedListener { text ->
            viewModel.filterMeals(text.toString())
        }

        return view
    }
}