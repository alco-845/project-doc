package com.alcorp.testmodul2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class BerandaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)
    }

    fun continueToProfile(view: View) {
        startActivity(Intent(this, ProfileActivity::class.java))
    }
}