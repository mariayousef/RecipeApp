package com.example.recipeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var progressBar: ProgressBar
    private val viewModel: HomeViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        if (!NetworkUtils.isInternetAvailable(requireContext())) {
            NetworkUtils.showNoInternetToast(requireContext())
            requireActivity().onBackPressed()
            return view
        }

        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.meal_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recipeAdapter = RecipeAdapter { selectedMeal ->
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(selectedMeal)
            findNavController().navigate(action)
        }
        recyclerView.adapter = recipeAdapter

        // مراقبة التغييرات في البيانات
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            recipeAdapter.submitList(meals)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigation = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.search -> {
                    val bundle = Bundle()
                    viewModel.meals.value?.let { meals ->
                        bundle.putSerializable("recipe_list", ArrayList(meals))
                    }
                    findNavController().navigate(R.id.search, bundle)
                    true
                }
                R.id.favorite -> {
                    val bundle = Bundle()
                    viewModel.meals.value?.let { meals ->
                        bundle.putSerializable("recipe_list", ArrayList(meals))
                    }
                    findNavController().navigate(R.id.favorite, bundle)
                    true
                }
                R.id.homeFragment -> {
                    true
                }
                else -> false
            }
        }
    }
}