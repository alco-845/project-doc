package com.cristiawan.goparty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void SignUp(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    public void btnlogin(View view) {
        Intent intent = new Intent(Login.this, Beranda.class);
        startActivity(intent);
    }
}