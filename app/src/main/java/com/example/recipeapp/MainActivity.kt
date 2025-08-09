package com.example.recipeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getSharedPreferences("RecipeAppPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            if (isLoggedIn) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main, HomeFragment())
                    .commit()
            } else {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)

            }
        }, 3000)
    }
}
