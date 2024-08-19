package com.example.todo

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.todo.callback.OnTaskAddedListener
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.fragment.AddTaskBottomSheetFragment
import com.example.todo.fragment.SettingsFragment
import com.example.todo.fragment.TasksFragment
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var tasksFragment: TasksFragment
    lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySavedPreferences()
        tasksFragment = TasksFragment()
        settingsFragment = SettingsFragment()
        binding.fabAdd.setOnClickListener {
            val bottomSheetFragment = AddTaskBottomSheetFragment()
            binding.fabAdd.setOnClickListener {
                val bottomSheetFragment = AddTaskBottomSheetFragment()
                bottomSheetFragment.onTaskAddedListener = object : OnTaskAddedListener {
                    override fun onTaskAdded() {
                        val fragment =
                            supportFragmentManager.findFragmentById(R.id.todo_fragment_container) as? TasksFragment
                        fragment?.refreshTasks() // Call a method to refresh the tasks
                    }
                }
                bottomSheetFragment.show(supportFragmentManager, null)
        }
            bottomSheetFragment.show(supportFragmentManager, null)
        }
        binding.bottomNavView.setOnItemSelectedListener {
            val fragment = when(it.itemId){
                R.id.tasks -> TasksFragment()
                R.id.settings -> SettingsFragment()
                else->TasksFragment()
            }
            //updateLocale()
            showFragment(fragment)

            return@setOnItemSelectedListener true
        }
        binding.bottomNavView.selectedItemId = R.id.tasks

    }
    private fun showFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(binding.todoFragmentContainer.id,fragment)
            .commit()
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
//        resources.updateConfiguration(config, resources.displayMetrics)

        // Apply theme
        val nightMode = when (mode) {
            "Night" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
