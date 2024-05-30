package com.alcorp.oper.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.alcorp.oper.databinding.ActivityLoginBinding
import com.alcorp.oper.ui.customer.CustomerMainActivity
import com.alcorp.oper.ui.driver.DriverMainActivity
import com.alcorp.oper.ui.partner.PartnerMainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var spPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spRole.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                spPosition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.btnLogin.setOnClickListener {
            when (spPosition) {
                0 -> startActivity(Intent(this, CustomerMainActivity::class.java))
                1 -> startActivity(Intent(this, DriverMainActivity::class.java))
                2 -> startActivity(Intent(this, PartnerMainActivity::class.java))
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisActivity::class.java))
        }

        customDialog()
    }

    private fun customDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informasi")
        builder.setMessage("Aplikasi ini hanya prototyping saja dan belum ada fitur yang berjalan")
        builder.setPositiveButton("Ok") { _, _ -> }
        val alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }
}