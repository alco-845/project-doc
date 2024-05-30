package com.example.storyapp.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.main.MainActivity
import com.example.storyapp.ui.regis.RegisActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun checkLogin() {
        pref = getSharedPreferences("storyApp", MODE_PRIVATE)
        val token = pref.getString("token", "")
        if (token != null && token != "") {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun init() {
        checkLogin()
        supportActionBar?.title = "Login"
        binding.tvRegis.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view) {
            binding.tvRegis -> {
                val intent = Intent(this, RegisActivity::class.java)
                startActivity(intent)
            }
            binding.btnLogin -> {
                val email = binding.edtEmailLogin.text.toString().trim()
                val password = binding.edtPassLogin.text.toString().trim()

                loginViewModel.signInUser(email, password)
                loginViewModel.loginUser.observe(this) {
                    val name = it.loginResult.name
                    val token = it.loginResult.token

                    prefEdit = pref.edit()
                    prefEdit.putString("name", name)
                    prefEdit.putString("token", token)
                    prefEdit.apply()

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                loginViewModel.isLoading.observe(this) {
                    showLoading(it)
                }

                loginViewModel.message.observe(this ) {
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbLogin.visibility = View.VISIBLE else binding.pbLogin.visibility = View.GONE
    }
}