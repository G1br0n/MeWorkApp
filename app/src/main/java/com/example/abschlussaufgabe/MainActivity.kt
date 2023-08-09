package com.example.abschlussaufgabe

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.abschlussaufgabe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        //Navigation BarBot Activierung. standart visibility = View.GONE
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavBar = binding.bottomNavBar
        setupWithNavController(bottomNavBar, navController)

    }

    //Fun
    fun showNavigationBar() {
        binding.bottomNavBar.visibility = View.VISIBLE
    }

    fun closeNavigationBar(){
        binding.bottomNavBar.visibility = View.GONE
    }

}