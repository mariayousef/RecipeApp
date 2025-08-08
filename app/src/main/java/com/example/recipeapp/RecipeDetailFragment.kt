package com.example.recipeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide

class RecipeDetailFragment : Fragment() {

    private val args: RecipeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)

        val meal = args.meal

        val imageView = view.findViewById<ImageView>(R.id.detail_image)
        val titleView = view.findViewById<TextView>(R.id.detail_title)
        val instructionsView = view.findViewById<TextView>(R.id.detail_instructions)
        val youtubeButton = view.findViewById<Button>(R.id.youtube_button)
        val toggleView = view.findViewById<TextView>(R.id.toggle_description)

        Glide.with(requireContext()).load(meal.strMealThumb).into(imageView)
        titleView.text = meal.strMeal
        instructionsView.text = meal.strInstructions ?: "No instructions available"

        var isExpanded = false

        instructionsView.post {
            val layout = instructionsView.layout
            if (layout != null) {
                val lineCount = layout.lineCount
                if (lineCount > 3) {
                    instructionsView.maxLines = 3
                    instructionsView.ellipsize = TextUtils.TruncateAt.END
                    toggleView.visibility = View.VISIBLE
                } else {
                    toggleView.visibility = View.GONE
                }
            }
        }

        toggleView.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                instructionsView.maxLines = Int.MAX_VALUE
                instructionsView.ellipsize = null
                toggleView.text = "Hide"
            } else {
                instructionsView.maxLines = 3
                instructionsView.ellipsize = TextUtils.TruncateAt.END
                toggleView.text = "Show more"
            }
        }

        youtubeButton.setOnClickListener {
            meal.strYoutube?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }

        return view
    }
}
