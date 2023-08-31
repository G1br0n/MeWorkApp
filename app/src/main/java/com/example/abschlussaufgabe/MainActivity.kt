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

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //FragmentContainerView 1 von 2 , zuständig für die hauptnaviegation fragmete, arbeitet mit nav_graph
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController

        //Navigation BarBot Activierung. standart visibility = View.GONE
        val bottomNavBar = binding.bottomNavBar
        setupWithNavController(bottomNavBar, navController)

        // MediaPlayer Initialisierung
        mediaPlayer = MediaPlayer.create(this, R.raw.click_sound)

        binding.bottomNavBar.setOnItemSelectedListener { menuItem ->
            mediaPlayer.start()
            navController.navigate(menuItem.itemId)
            true
        }

    }




    //Fun
    fun showNavigationBar() {
        binding.bottomNavBar.visibility = View.VISIBLE
    }

    fun closeNavigationBar(){
        binding.bottomNavBar.visibility = View.GONE
    }

}