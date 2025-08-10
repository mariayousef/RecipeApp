package com.example.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipeAdapter(
    private val onItemClick: (Meal) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val recipes = mutableListOf<Meal>()

    fun submitList(list: List<Meal>) {
        recipes.clear()
        recipes.addAll(list)
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        private val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)



        fun bind(meal: Meal) {
            recipeTitle.text = meal.strMeal
            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .into(recipeImage)

            itemView.setOnClickListener {
                onItemClick(meal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun getItemCount() = recipes.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }
}
