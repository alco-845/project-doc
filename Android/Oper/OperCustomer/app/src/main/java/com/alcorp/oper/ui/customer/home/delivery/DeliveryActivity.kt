package com.alcorp.oper.ui.customer.home.delivery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alcorp.oper.R
import com.alcorp.oper.databinding.ActivityDeliveryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DeliveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeliveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDeliveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_delivery)
        navView.setupWithNavController(navController)
    }
}