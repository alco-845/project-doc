package com.alcorp.oper.ui.driver.select_order

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.oper.databinding.ActivityDetailSelectOrderBinding
import com.alcorp.oper.ui.account.AccountActivity

class DetailSelectOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSelectOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSelectOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.title = "Select Order"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.ivProfile.setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}