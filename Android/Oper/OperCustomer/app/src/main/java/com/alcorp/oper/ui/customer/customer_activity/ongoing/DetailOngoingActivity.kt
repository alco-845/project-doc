package com.alcorp.oper.ui.customer.customer_activity.ongoing

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.oper.databinding.ActivityDetailOngoingBinding

class DetailOngoingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOngoingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOngoingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.title = "Detail On Going"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}