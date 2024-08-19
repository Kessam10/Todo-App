package com.example.todo.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.Spinner
//import androidx.fragment.app.Fragment
//import com.example.todo.R
//import com.example.todo.databinding.FragmentSettingBinding
//
//class SettingsFragment:Fragment() {
//    lateinit var binding: FragmentSettingBinding
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentSettingBinding.inflate(inflater,container,false)
//
//        val languages = resources.getStringArray(R.array.languages)
//        val arrayAdapterLang = ArrayAdapter(requireContext(),R.layout.language_list_item,languages)
//        binding.autoCompleteTextView.setAdapter(arrayAdapterLang)
//
//        val mode = resources.getStringArray(R.array.mode)
//        val arrayAdapterMode = ArrayAdapter(requireContext(),R.layout.language_list_item,mode)
//        binding.autoCompleteTextView1.setAdapter(arrayAdapterMode)
//        binding.autoCompleteTextView1.setOnItemClickListener { parent, view, position, id ->  }
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(binding.root, savedInstanceState)
//
//    }
//
//
//}
//import android.content.Context
//import android.content.res.Configuration
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.fragment.app.Fragment
//import com.example.todo.R
//import com.example.todo.databinding.FragmentSettingBinding
//import java.util.Locale
//
//@Suppress("DEPRECATION")
//class SettingsFragment : Fragment() {
//    lateinit var binding: FragmentSettingBinding
//    private val PREFS_NAME = "app_prefs"
//    private val LANGUAGE_KEY = "language"
//    private val MODE_KEY = "mode"
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        binding = FragmentSettingBinding.inflate(inflater, container, false)
//
//        val languages = resources.getStringArray(R.array.languages)
//        val arrayAdapterLang = ArrayAdapter(requireContext(), R.layout.language_list_item, languages)
//        binding.autoCompleteTextView.setAdapter(arrayAdapterLang)
//
//        val modes = resources.getStringArray(R.array.mode)
//        val arrayAdapterMode = ArrayAdapter(requireContext(), R.layout.language_list_item, modes)
//        binding.autoCompleteTextView1.setAdapter(arrayAdapterMode)
//
//        loadPreferences()
//
//        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
//            val selectedLanguage = parent.getItemAtPosition(position).toString()
//            savePreference(LANGUAGE_KEY, selectedLanguage)
//            updateLocale(selectedLanguage)
//            requireActivity().recreate()
//        }
//
//        binding.autoCompleteTextView1.setOnItemClickListener { parent, _, position, _ ->
//            val selectedMode = parent.getItemAtPosition(position).toString()
//            savePreference(MODE_KEY, selectedMode)
//            updateTheme(selectedMode)
//        }
//
//        return binding.root
//    }
//
//    private fun savePreference(key: String, value: String) {
//        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        with(prefs.edit()) {
//            putString(key, value)
//            apply()
//        }
//    }
//
//    private fun loadPreferences() {
//        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//        val language = prefs.getString(LANGUAGE_KEY, "English") ?: "English"
//        val mode = prefs.getString(MODE_KEY, "Light") ?: "Light"
//
//        // Apply the saved preferences
//        binding.autoCompleteTextView.setText(language, false)
//        binding.autoCompleteTextView1.setText(mode, false)
//
//        updateLocale(language)
//        updateTheme(mode)
//    }
//
//    private fun updateLocale(language: String) {
//        val locale = when (language) {
//            "Arabic" -> Locale("ar")
//            else -> Locale("en")
//        }
//        Locale.setDefault(locale)
//        val config = Configuration()
//        config.locale = locale
//        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
//    }
//
//    private fun updateTheme(mode: String) {
//        val nightMode = when (mode) {
//            "Night" -> AppCompatDelegate.MODE_NIGHT_YES
//            else -> AppCompatDelegate.MODE_NIGHT_NO
//        }
//        AppCompatDelegate.setDefaultNightMode(nightMode)
//    }
//}
//

//fun isLight(): Boolean {
//    Log.e("TAG", "isLight: ${AppCompatDelegate.getDefaultNightMode()}")
//    return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO ||
//            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//}

//if (isLight()) {
//    switchModeButton.text = getString(R.string.dark_mode)
//} else {
//    switchModeButton.text = getString(R.string.light_mode)
//}

//onclick
//if (isLight()) {
//    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//    switchModeButton.text =  getString(R.string.light_mode)
//} else {
//    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//    switchModeButton.text =  getString(R.string.dark_mode)
//}
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.todo.R
import com.example.todo.databinding.FragmentSettingBinding
import java.util.Locale

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding
    private val PREFS_NAME = "app_prefs"
    private val LANGUAGE_KEY = "language"
    private val MODE_KEY = "mode"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        val languages = resources.getStringArray(R.array.languages)
        val arrayAdapterLang = ArrayAdapter(requireContext(), R.layout.language_list_item, languages)
        binding.autoCompleteTextView.setAdapter(arrayAdapterLang)

        val modes = resources.getStringArray(R.array.mode)
        val arrayAdapterMode = ArrayAdapter(requireContext(), R.layout.language_list_item, modes)
        binding.autoCompleteTextView1.setAdapter(arrayAdapterMode)

        loadPreferences()

        binding.autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedLanguage = parent.getItemAtPosition(position).toString()
            savePreference(LANGUAGE_KEY, selectedLanguage)
            updateLocale(selectedLanguage)
            // Recreate the activity to apply language change
            requireActivity().recreate()
        }

        binding.autoCompleteTextView1.setOnItemClickListener { parent, _, position, _ ->
            val selectedMode = parent.getItemAtPosition(position).toString()
            savePreference(MODE_KEY, selectedMode)
            updateTheme(selectedMode)
        }

        return binding.root
    }

    private fun savePreference(key: String, value: String) {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun loadPreferences() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val language = prefs.getString(LANGUAGE_KEY, "English") ?: "English"
        val mode = prefs.getString(MODE_KEY, "Light") ?: "Light"

        // Apply the saved preferences
        binding.autoCompleteTextView.setText(language, false)
        binding.autoCompleteTextView1.setText(mode, false)

        updateLocale(language)
        updateTheme(mode)
    }

    private fun updateLocale(language: String) {
        val locale = when (language) {
            "Arabic" -> Locale("ar")
            else -> Locale("en")
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }

    private fun updateTheme(mode: String) {
        val nightMode = when (mode) {
            "Night" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
