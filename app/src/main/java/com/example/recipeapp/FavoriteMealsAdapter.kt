package com.example.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class FavoriteMealsAdapter(
    private val onItemClick: ((Meal) -> Unit)? = null,
    private val onRemoveClick: ((Meal) -> Unit)? = null
) : ListAdapter<Meal, FavoriteMealsAdapter.FavoriteMealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_meal, parent, false)
        return FavoriteMealViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteMealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteMealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mealNameText: TextView = itemView.findViewById(R.id.mealNameText)
        private val strMealThumb: ImageView = itemView.findViewById(R.id.mealImage)
        private val removeButton: Button = itemView.findViewById(R.id.removeButton)

        fun bind(meal: Meal) {
            mealNameText.text = meal.strMeal

            // Example if using Glide or Coil:
            // Glide.with(mealImage.context).load(meal.strMealThumb).into(mealImage)

            itemView.setOnClickListener {
                onItemClick?.invoke(meal)
            }

            removeButton.setOnClickListener {
                onRemoveClick?.invoke(meal)
            }
        }
    }

    class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
}
