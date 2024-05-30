package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityTransaksi extends AppCompatActivity {

    Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

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
        tvTb.setText("Transaksi");
    }

    public void penjualan(View view) {
        Intent intent = new Intent(this, ActivityPenjualan.class);
        startActivity(intent);
    }

    public void request(View view) {
        Intent intent = new Intent(this, ActivityRequest.class);
        startActivity(intent);
    }

    public void bayar(View view) {
        Intent intent = new Intent(this, ActivityBayar.class);
        startActivity(intent);
    }

    public void pemasukan(View view) {
        Intent intent = new Intent(this, ActivityTambahPemasukan.class);
        startActivity(intent);
    }

    public void pengeluaran(View view) {
        Intent intent = new Intent(this, ActivityTambahPengeluaran.class);
        startActivity(intent);
    }
}
