package com.example.memorymatch.data

import android.content.Context
import android.os.Build
import java.util.Locale

// Popis: Trieda zabezpečujúca zmenu lokalizácie aplikácie podľa zvoleného jazyka
object LocaleManager {

    // Popis: Nastaví lokalizáciu aplikácie na základe zvoleného jazyka
    fun setLocale(context: Context, language: String): Context {
        if (language == "default") {
            return context
        }

        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
            context
        }
    }
}
