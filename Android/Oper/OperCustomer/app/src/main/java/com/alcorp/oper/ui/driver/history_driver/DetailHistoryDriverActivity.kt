package com.alcorp.oper.ui.driver.history_driver

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.oper.databinding.ActivityDetailHistoryDriverBinding
import com.alcorp.oper.ui.account.AccountActivity

class DetailHistoryDriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryDriverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.title = "Detail History"
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