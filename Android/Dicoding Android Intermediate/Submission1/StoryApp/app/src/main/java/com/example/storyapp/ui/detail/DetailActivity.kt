package com.example.storyapp.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailBinding
import com.example.storyapp.ui.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var token: String
    private lateinit var storyId: String
    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        supportActionBar?.title = "Detail Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = getSharedPreferences("storyApp", MODE_PRIVATE)
        token = pref.getString("token", "").toString()
        storyId = intent.getStringExtra(STORY_ID).toString()

        getData()
        load()
    }

    private fun getData() {
        detailViewModel.getDetailStory("Bearer $token", storyId)
        detailViewModel.detailStory.observe(this) {
            Glide.with(this@DetailActivity)
                .load(it.story.photoUrl)
                .placeholder(ContextCompat.getDrawable(this@DetailActivity, R.drawable.default_image))
                .error(ContextCompat.getDrawable(this@DetailActivity, R.drawable.default_image))
                .into(binding.ivImageStory)

            binding.txtName.text = it.story.name
            binding.txtDescription.text = it.story.description
        }
    }

    private fun load() {
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailViewModel.message.observe(this ) {
            Toast.makeText(this@DetailActivity, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.pbDetail.visibility = View.VISIBLE else binding.pbDetail.visibility = View.GONE
    }

    companion object {
        const val STORY_ID = "STORY_ID"
    }
}