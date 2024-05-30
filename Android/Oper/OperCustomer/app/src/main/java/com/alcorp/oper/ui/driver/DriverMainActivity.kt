package com.alcorp.oper.ui.driver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alcorp.oper.R
import com.alcorp.oper.databinding.ActivityDriverMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DriverMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDriverMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_driver_main)
        navView.setupWithNavController(navController)
    }
}