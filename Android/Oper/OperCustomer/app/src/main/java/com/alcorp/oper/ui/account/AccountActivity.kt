package com.alcorp.oper.ui.account

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.oper.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSmall.toolbar)
        binding.toolbarSmall.toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.title = "My Account"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.ivEdit.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        binding.tvFaq.setOnClickListener {
            startActivity(Intent(this, FaqActivity::class.java))
        }

        binding.tvLanguage.setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }

        binding.tvBank.setOnClickListener {
            startActivity(Intent(this, EditBankActivity::class.java))
        }

        binding.tvNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}