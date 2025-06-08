package com.example.memorymatch.data

import android.content.Context
import android.content.SharedPreferences

// Popis: Trieda zabezpečujúca ukladanie a načítavanie preferovaného jazyka pomocou SharedPreferences
object LanguagePreferences {

    // Popis: Názov súboru preferencií
    private const val PREF_NAME = "app_prefs"

    // Popis: Kľúč pre uložený jazyk
    private const val KEY_LANGUAGE = "selected_language"

    // Popis: Uloží zvolený jazyk do SharedPreferences
    fun saveLanguage(context: Context, language: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    // Popis: Načíta zvolený jazyk zo SharedPreferences
    fun getLanguage(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "default") ?: "default"
    }
}
