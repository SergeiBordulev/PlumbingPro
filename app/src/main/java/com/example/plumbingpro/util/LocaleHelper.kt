package com.example.plumbingpro.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleHelper {

    fun applyLocale(context: Context) {
        val prefs = PreferencesManager(context)
        val langCode = prefs.languageCode
        applyLanguage(langCode)
    }

    fun applyLanguage(langCode: String) {
        val locales = if (langCode == PreferencesManager.LANG_SYSTEM) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(langCode)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }

    fun applyTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }
}
