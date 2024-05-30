package com.alcorp.aryunas.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.aryunas.databinding.ActivityMainBinding
import com.alcorp.aryunas.ui.kamar.daftarKamar.DaftarKamarActivity
import com.alcorp.aryunas.ui.order.daftarOrder.OrderActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvKamar.setOnClickListener {
            val intent = Intent(this, DaftarKamarActivity::class.java)
            startActivity(intent)
        }

        binding.cvBooking.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }
    }
}