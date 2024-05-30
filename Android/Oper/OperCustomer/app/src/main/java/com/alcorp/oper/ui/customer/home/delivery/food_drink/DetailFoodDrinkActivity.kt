package com.alcorp.oper.ui.customer.home.delivery.food_drink

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.oper.databinding.ActivityDetailFoodDrinkBinding
import com.alcorp.oper.ui.customer.home.delivery.AddOrderActivity

class DetailFoodDrinkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailFoodDrinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFoodDrinkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddOrderActivity::class.java))
        }
    }
}