package com.example.plumbingpro

import android.app.Application
import com.example.plumbingpro.util.LocaleHelper
import com.example.plumbingpro.util.PreferencesManager

class PlumbingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val prefs = PreferencesManager(this)
        LocaleHelper.applyTheme(prefs.themeMode)
        LocaleHelper.applyLocale(this)
    }
}
