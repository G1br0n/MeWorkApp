package com.example.abschlussaufgabe

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.abschlussaufgabe.databinding.ActivityMainBinding

/**
 * Die `MainActivity` ist die Hauptaktivität der App, die das Hauptlayout und die Hauptnavigation verwaltet.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer

    /**
     * Wird aufgerufen, wenn die Aktivität erstellt wird.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Initialisierung des NavHostFragments für die Hauptnavigation
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController

        // Aktivierung der Navigationsleiste am unteren Bildschirmrand. Die Standard-Sichtbarkeit ist View.GONE
        val bottomNavBar = binding.bottomNavBar
        setupWithNavController(bottomNavBar, navController)

        // Initialisierung des MediaPlayers für Klick-Sounds
        mediaPlayer = MediaPlayer.create(this, R.raw.click_sound)

        // Reaktion auf die Auswahl eines Elements in der Navigationsleiste
        binding.bottomNavBar.setOnItemSelectedListener { menuItem ->
            mediaPlayer.start() // Klick-Sound abspielen
            navController.navigate(menuItem.itemId) // Zur ausgewählten Navigationsebene navigieren
            true
        }

    }

    /**
     * Zeigt die Navigationsleiste an.
     */
    fun showNavigationBar() {
        binding.bottomNavBar.visibility = View.VISIBLE
    }

    /**
     * Blendet die Navigationsleiste aus.
     */
    fun closeNavigationBar(){
        binding.bottomNavBar.visibility = View.GONE
    }
}
