package com.alcorp.oper.ui.partner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alcorp.oper.R
import com.alcorp.oper.databinding.ActivityPartnerMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PartnerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPartnerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPartnerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_partner_main)
        navView.setupWithNavController(navController)
    }
}