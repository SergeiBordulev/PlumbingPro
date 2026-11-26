package com.example.plumbingpro.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plumbingpro.R
import com.example.plumbingpro.databinding.FragmentSettingsBinding
import com.example.plumbingpro.util.LocaleHelper
import com.example.plumbingpro.util.PreferencesManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = PreferencesManager(requireContext())

        setupThemeGroup()
        setupLanguageGroup()
        setupUnitsGroup()
    }

    private fun setupThemeGroup() {
        when (prefs.themeMode) {
            PreferencesManager.THEME_LIGHT -> binding.radioThemeLight.isChecked = true
            PreferencesManager.THEME_DARK -> binding.radioThemeDark.isChecked = true
            else -> binding.radioThemeSystem.isChecked = true
        }

        binding.radioGroupTheme.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.radioThemeLight -> PreferencesManager.THEME_LIGHT
                R.id.radioThemeDark -> PreferencesManager.THEME_DARK
                else -> PreferencesManager.THEME_SYSTEM
            }
            if (mode != prefs.themeMode) {
                prefs.themeMode = mode
                LocaleHelper.applyTheme(mode)
            }
        }
    }

    private fun setupLanguageGroup() {
        when (prefs.languageCode) {
            PreferencesManager.LANG_RU -> binding.radioLangRu.isChecked = true
            PreferencesManager.LANG_EN -> binding.radioLangEn.isChecked = true
            PreferencesManager.LANG_ES -> binding.radioLangEs.isChecked = true
            else -> binding.radioLangSystem.isChecked = true
        }

        binding.radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            val lang = when (checkedId) {
                R.id.radioLangRu -> PreferencesManager.LANG_RU
                R.id.radioLangEn -> PreferencesManager.LANG_EN
                R.id.radioLangEs -> PreferencesManager.LANG_ES
                else -> PreferencesManager.LANG_SYSTEM
            }
            if (lang != prefs.languageCode) {
                prefs.languageCode = lang
                LocaleHelper.applyLanguage(lang)
            }
        }
    }

    private fun setupUnitsGroup() {
        if (prefs.unitSystem == PreferencesManager.UNIT_IMPERIAL) {
            binding.radioUnitImperial.isChecked = true
        } else {
            binding.radioUnitMetric.isChecked = true
        }

        binding.radioGroupUnits.setOnCheckedChangeListener { _, checkedId ->
            val unit = if (checkedId == R.id.radioUnitImperial) {
                PreferencesManager.UNIT_IMPERIAL
            } else {
                PreferencesManager.UNIT_METRIC
            }
            prefs.unitSystem = unit
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
