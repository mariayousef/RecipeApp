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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.ui.favorites.FavoriteViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

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
        val favoriteIcon = view.findViewById<ImageView>(R.id.favorite_icon)
        val playButton = view.findViewById<ImageView>(R.id.play_button)
        val videoThumbnail = view.findViewById<ImageView>(R.id.video_thumbnail)
        val videoView = view.findViewById<YouTubePlayerView>(R.id.video_view)

        Glide.with(requireContext()).load(meal.strMealThumb).into(imageView)
        titleView.text = meal.strMeal
        instructionsView.text = meal.strInstructions ?: "No instructions available"

        // ---- Favorite logic ----
        var isFavorite = false
        val viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        viewModel.checkIfFavorite(meal.idMeal) { isFav ->
            isFavorite = isFav
            favoriteIcon.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            )
        }

        favoriteIcon.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                viewModel.addToFavorites(meal)
                favoriteIcon.setImageResource(R.drawable.ic_favorite_filled)
            } else {
                viewModel.removeFromFavorites(meal)
                favoriteIcon.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        // ---- Show more/less logic ----
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

        // ---- YouTube Button: open in YouTube app ----
        youtubeButton.setOnClickListener {
            meal.strYoutube?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }

        // ---- Load YouTube Thumbnail ----
        meal.strYoutube?.let { url ->
            val videoId = Uri.parse(url).getQueryParameter("v")
            if (!videoId.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load("https://img.youtube.com/vi/$videoId/0.jpg")
                    .into(videoThumbnail)

                playButton.setOnClickListener {
                    videoThumbnail.visibility = View.GONE
                    playButton.visibility = View.GONE
                    videoView.visibility = View.VISIBLE

                    lifecycle.addObserver(videoView)
                    videoView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadVideo(videoId, 0f)
                        }
                    })
                }
            }
        }

        return view
    }
}
