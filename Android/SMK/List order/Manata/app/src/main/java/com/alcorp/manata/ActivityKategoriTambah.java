package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class ActivityKategoriTambah extends AppCompatActivity {

    Toolbar appbar;
    TextInputEditText edtKodeKategori, edtKategori;
    String kodekategori, kategori;
    Integer idkategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_tambah);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        appbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView ivTb = (ImageView) findViewById(R.id.ivTb1);
        ivTb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tvTb = (TextView) findViewById(R.id.tvTb);

        edtKodeKategori = (TextInputEditText) findViewById(R.id.tKodeKategori);
        edtKategori = (TextInputEditText) findViewById(R.id.tKategori);

        Bundle extra = getIntent().getExtras();
        if (extra == null){
            // Insert
            tvTb.setText("Insert Kategori");

            idkategori = null;
        } else {
            tvTb.setText("Update Kategori");

            idkategori = extra.getInt("idkategori");
            edtKodeKategori.setText(extra.getString("kodekategori"));
            edtKategori.setText(extra.getString("kategori"));
        }
    }

    public void simpan(View view) {
        kodekategori = edtKodeKategori.getText().toString();
        kategori = edtKategori.getText().toString();

        if (kodekategori.equals("") || kategori.equals("")) {
            Toast.makeText(ActivityKategoriTambah.this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            Database db = new Database(ActivityKategoriTambah.this);
            if (idkategori == null) {
                if (db.insertKategori(kodekategori, kategori)) {
                    Toast.makeText(ActivityKategoriTambah.this, "Tambah Data Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ActivityKategoriTambah.this, "Tambah Data Gagal", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (db.updateKategori(idkategori, kodekategori, kategori)) {
                    Toast.makeText(ActivityKategoriTambah.this, "Berhasil Memperbarui Data", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ActivityKategoriTambah.this, "Gagal Memperbarui Data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}