package com.cristiawan.goparty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Spinner;
import android.widget.TextView;

public class Main_Splash extends AppCompatActivity {
    Handler h = new Handler();

    private TextView textView;
    private Spinner spinner;

    private String [] kategori = {"Tenda Pesta","Meja Kursi","Alat Makan","Sound System"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Main_Splash.this, Login.class);
                startActivity(i);
                finish();
            }
        }, 1000);

    }
}