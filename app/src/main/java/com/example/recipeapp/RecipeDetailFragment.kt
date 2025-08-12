package com.example.recipeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
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
        val toggleView = view.findViewById<TextView>(R.id.toggle_description)
        val favoriteIcon = view.findViewById<ImageView>(R.id.favorite_icon)
        val thumbnailView = view.findViewById<ImageView>(R.id.video_thumbnail)
        val playButton = view.findViewById<ImageView>(R.id.play_button)
        val playerView = view.findViewById<YouTubePlayerView>(R.id.video_view)
        val youtubeButton = view.findViewById<MaterialButton>(R.id.youtube_button)

        Glide.with(requireContext()).load(meal.strMealThumb).into(imageView)
        titleView.text = meal.strMeal
        instructionsView.text = meal.strInstructions ?: "No instructions available"

        var isFavorite = false
        favoriteIcon.setOnClickListener {
            isFavorite = !isFavorite
            favoriteIcon.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            )
        }

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

        meal.strYoutube?.let { url ->
            val videoId = Uri.parse(url).getQueryParameter("v")
            if (!videoId.isNullOrEmpty()) {
                val thumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"
                Glide.with(requireContext()).load(thumbnailUrl).into(thumbnailView)

                lifecycle.addObserver(playerView)

                playButton.setOnClickListener {
                    thumbnailView.visibility = View.GONE
                    playButton.visibility = View.GONE
                    playerView.visibility = View.VISIBLE

                    playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadVideo(videoId, 0f)
                        }
                    })
                }

                youtubeButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                }
            } else {
                youtubeButton.visibility = View.GONE
            }
        } ?: run {
            youtubeButton.visibility = View.GONE
        }

        return view
    }
}
