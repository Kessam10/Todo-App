package com.example.todo

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        applySavedPreferences()
        Handler(Looper.getMainLooper())
            .postDelayed({
                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
    }
    private fun applySavedPreferences() {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val language = prefs.getString("language", "English") ?: "English"
        val mode = prefs.getString("mode", "Light") ?: "Light"

        // Apply language
        val locale = when (language) {
            "Arabic" -> Locale("ar")
            else -> Locale("en")
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Apply theme
        val nightMode = when (mode) {
            "Night" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}