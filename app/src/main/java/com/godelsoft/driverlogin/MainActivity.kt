package com.godelsoft.driverlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.findFragmentById(R.id.mainMenu)?.let { mainMenuFragment ->
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, mainMenuFragment)
                .commit()
        }
    }
}