package com.example.storyapp.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.ViewModelFactory
import com.example.storyapp.ui.add.AddStoryActivity
import com.example.storyapp.ui.login.LoginActivity

class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener, View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences
    private lateinit var prefEdit: SharedPreferences.Editor
    private lateinit var token: String
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun checkLogin() {
        if (token == null && token == "") {
            val i = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun init() {
        pref = getSharedPreferences("storyApp", MODE_PRIVATE)
        token = pref.getString("token", "").toString()

        binding.btnAdd.setOnClickListener(this)
        checkLogin()
        getData()
        load()
    }

    private fun getData() {
        mainViewModel.getListStory("Bearer $token")
        mainViewModel.listStory.observe(this) {
            val mainAdapter = MainAdapter(this, it)
            binding.rvStory.setHasFixedSize(true)
            binding.rvStory.layoutManager = GridLayoutManager(this@MainActivity, 2)
            binding.rvStory.adapter = mainAdapter
        }
    }

    private fun load() {
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.message.observe(this ) {
            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        menu.findItem(R.id.menu_logout).setOnMenuItemClickListener(this)
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                prefEdit = pref.edit()
                prefEdit.clear().apply()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                return true
            }
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbMain.visibility = View.VISIBLE else binding.pbMain.visibility = View.GONE
    }

    override fun onClick(view: View) {
        when(view) {
            binding.btnAdd -> {
                startActivityForResult(Intent(this@MainActivity, AddStoryActivity::class.java), 1)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1){
            getData()
        }
    }
}