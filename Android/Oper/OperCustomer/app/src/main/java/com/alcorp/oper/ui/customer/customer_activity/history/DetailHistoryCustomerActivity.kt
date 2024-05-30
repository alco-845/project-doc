package com.alcorp.oper.ui.customer.customer_activity.history

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.oper.databinding.ActivityDetailHistoryCustomerBinding

class DetailHistoryCustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryCustomerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar.toolbar)
        binding.toolbar.toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.title = "Detail History"
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