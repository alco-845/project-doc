package com.example.storyapp.ui.regis

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivityRegisBinding
import com.example.storyapp.ui.ViewModelFactory

class RegisActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisBinding
    private val regisViewModel: RegisViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        supportActionBar?.title = "Register"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tvLogin.setOnClickListener(this)
        binding.btnRegis.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view) {
            binding.tvLogin -> {
                finish()
            }

            binding.btnRegis -> {
                val username = binding.edtUsername.text.toString().trim()
                val email = binding.edtEmailRegis.text.toString().trim()
                val password = binding.edtPassRegis.text.toString().trim()

                regisViewModel.signUpUser(username, email, password)
                regisViewModel.regisUser.observe(this) { finish() }
                regisViewModel.isLoading.observe(this) {
                    showLoading(it)
                }

                regisViewModel.message.observe(this ) {
                    Toast.makeText(this@RegisActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbRegis.visibility = View.VISIBLE else binding.pbRegis.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}