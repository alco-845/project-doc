package com.example.hi_kingadmin.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hi_kingadmin.R;
import com.example.hi_kingadmin.ui.login.LoginActivity;
import com.example.hi_kingadmin.ui.login.RegisterActivity;

public class CoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);

        SharedPreferences sh = getSharedPreferences("AdminApp", MODE_PRIVATE);
        int id = sh.getInt("id", 0);
        if (id != 0) {
            Intent intent = new Intent(CoverActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}