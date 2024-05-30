package com.alcorp.kolamin.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.alcorp.kolamin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLogin()
        setupToolbar()
        setupAction()
    }

    private fun setupToolbar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun checkLogin() {
        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
    }

    private fun setupAction() {
        binding.cvMonitor.setOnClickListener {
            startActivity(Intent(this, MonitoringActivity::class.java))
        }

        binding.cvBerat.setOnClickListener {
            startActivity(Intent(this, BobotActivity::class.java))
        }

        binding.cvFeed.setOnClickListener {
            startActivity(Intent(this, EfeedActivity::class.java))
        }

        binding.ivLogout.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}