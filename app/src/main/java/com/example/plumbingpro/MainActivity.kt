package com.example.plumbingpro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.plumbingpro.databinding.ActivityMainBinding
import com.example.plumbingpro.ui.calculators.CalculatorsFragment
import com.example.plumbingpro.ui.codes.CodesFragment
import com.example.plumbingpro.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.topToolbar)

        if (savedInstanceState == null) {
            loadFragment(CalculatorsFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_calculators -> CalculatorsFragment()
                R.id.nav_codes -> CodesFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> CalculatorsFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
