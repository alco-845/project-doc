package com.alcorp.oper.ui.customer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alcorp.oper.R
import com.alcorp.oper.databinding.ActivityCustomerMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }
}