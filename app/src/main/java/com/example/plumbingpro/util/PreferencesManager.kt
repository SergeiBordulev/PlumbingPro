package com.example.plumbingpro.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "plumbing_pro_prefs"
        private const val KEY_THEME = "key_theme"
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_UNITS = "key_units"

        const val THEME_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        const val THEME_LIGHT = AppCompatDelegate.MODE_NIGHT_NO
        const val THEME_DARK = AppCompatDelegate.MODE_NIGHT_YES

        const val LANG_SYSTEM = "system"
        const val LANG_RU = "ru"
        const val LANG_EN = "en"
        const val LANG_ES = "es"

        const val UNIT_METRIC = "metric"
        const val UNIT_IMPERIAL = "imperial"
    }

    var themeMode: Int
        get() = prefs.getInt(KEY_THEME, THEME_SYSTEM)
        set(value) = prefs.edit().putInt(KEY_THEME, value).apply()

    var languageCode: String
        get() = prefs.getString(KEY_LANGUAGE, LANG_SYSTEM) ?: LANG_SYSTEM
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    var unitSystem: String
        get() = prefs.getString(KEY_UNITS, UNIT_METRIC) ?: UNIT_METRIC
        set(value) = prefs.edit().putString(KEY_UNITS, value).apply()
}
