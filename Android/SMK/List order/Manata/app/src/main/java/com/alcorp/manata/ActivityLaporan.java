package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityLaporan extends AppCompatActivity {

    Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

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
        tvTb.setText("Laporan");
    }

    public void barang(View view) {
        Intent intent = new Intent(this, ActivityBarang.class);
        intent.putExtra("barang", "laporan");
        startActivity(intent);
    }

    public void pelanggan(View view) {
        Intent intent = new Intent(this, ActivityPelanggan.class);
        intent.putExtra("pelanggan", "laporan");
        startActivity(intent);
    }

    public void perstatus(View view) {
        Intent intent = new Intent(this, ActivityTampil.class);
        intent.putExtra("type", "status");
        startActivity(intent);
    }

    public void permetode(View view) {
        Intent intent = new Intent(this, ActivityTampil.class);
        intent.putExtra("type", "metode");
        startActivity(intent);
    }

    public void kirim(View view) {
        Intent intent = new Intent(this, ActivityKirim.class);
        startActivity(intent);
    }

    public void pendapatan(View view) {
        Intent intent = new Intent(this, ActivityTampil.class);
        intent.putExtra("type", "pendapatan");
        startActivity(intent);
    }

    public void keuangan(View view) {
        Intent intent = new Intent(this, ActivityLaporanKeuangan.class);
        startActivity(intent);
    }
}
