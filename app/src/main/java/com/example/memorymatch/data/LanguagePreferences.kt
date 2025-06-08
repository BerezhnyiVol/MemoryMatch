package com.example.memorymatch.data

import android.content.Context
import android.content.SharedPreferences

object LanguagePreferences {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_LANGUAGE = "selected_language"

    fun saveLanguage(context: Context, language: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun getLanguage(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "default") ?: "default"
    }
}
