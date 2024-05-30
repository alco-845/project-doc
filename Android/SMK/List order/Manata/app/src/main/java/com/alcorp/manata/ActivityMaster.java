package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityMaster extends AppCompatActivity {

    Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        appbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView iv1 = (ImageView) findViewById(R.id.ivTb1);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tvTb = (TextView) findViewById(R.id.tvTb);
        tvTb.setText("Master");
    }

    public void identitas(View view) {
        Intent intent = new Intent(this, ActivityIdentitas.class);
        startActivity(intent);
    }

    public void kategori(View view) {
        Intent intent = new Intent(this, ActivityKategori.class);
        startActivity(intent);
    }

    public void barang(View view) {
        Intent intent = new Intent(this, ActivityBarang.class);
        intent.putExtra("barang", "master");
        startActivity(intent);
    }

    public void pelanggan(View view) {
        Intent intent = new Intent(this, ActivityPelanggan.class);
        intent.putExtra("pelanggan", "master");
        startActivity(intent);
    }
}
