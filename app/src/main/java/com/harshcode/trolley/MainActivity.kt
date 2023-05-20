package com.harshcode.trolley

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.harshcode.trolley.activity.LoginActivity
import com.harshcode.trolley.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navController = navHostFragment!!.findNavController()

    //In the popUpMenu i have inflated our menu
        val popUpMenu = PopupMenu(this, null)
        popUpMenu.inflate(R.menu.bottom_nav)

    //In the bottomBar I have set the popUpMenu and navController
        binding.bottomBar.setupWithNavController(popUpMenu.menu, navController)

        binding.bottomBar.onItemSelected = {
            when(it){
                    0 ->{
                        i = 0
                        navController.navigate(R.id.homeFragment)
                    }
                1 -> i = 1
                2 -> i = 2
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (i == 0){
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

    //Title of the toolbar changes as we navigate from one to other
    navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
        override fun onDestinationChanged(
            controller: NavController,
            destination: NavDestination,
            arguments: Bundle?
        ) {
            title = when(destination.id){
                R.id.cartFragment -> "My Cart"
                R.id.moreFragment -> "My Dashboard"
                else -> "Trolley"
            }
        }
    })
    }
}