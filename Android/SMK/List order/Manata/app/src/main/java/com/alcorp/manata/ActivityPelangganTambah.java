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

public class ActivityPelangganTambah extends AppCompatActivity {

    Toolbar appbar;
    TextView tvTb;
    TextInputEditText edtNamaPelanggan, edtAlamat, edtNoTelp;
    String namapelanggan, alamat, notelp;
    Integer idPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan_tambah);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

        tvTb = (TextView) findViewById(R.id.tvTb);

        edtNamaPelanggan = (TextInputEditText) findViewById(R.id.tNama);
        edtAlamat = (TextInputEditText) findViewById(R.id.tAlamat);
        edtNoTelp = (TextInputEditText) findViewById(R.id.tTelp);

        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            //Insert
            idPelanggan = null;

            tvTb.setText("Tambah Pelanggan");

            edtAlamat.setText("Jl.\n" +
                    "Desa/Kelurahan\n" +
                    "Kec.\n" +
                    "Kab.");
        } else {
            tvTb.setText("Update Pelanggan");

            idPelanggan = extra.getInt("idpelanggan");
            edtNamaPelanggan.setText(extra.getString("pelanggan"));
            edtAlamat.setText(extra.getString("alamat"));
            edtNoTelp.setText(extra.getString("telp"));
        }
    }

    public void simpan(View view) {
        namapelanggan = edtNamaPelanggan.getText().toString();
        alamat = edtAlamat.getText().toString();
        notelp = edtNoTelp.getText().toString();

        if (namapelanggan.equals("") || alamat.equals("") || notelp.equals("")) {
            Toast.makeText(ActivityPelangganTambah.this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            Database db = new Database(ActivityPelangganTambah.this);
            if (idPelanggan == null) {
                if (db.insertPelanggan(namapelanggan, alamat, notelp)) {
                    Toast.makeText(ActivityPelangganTambah.this, "Tambah Data Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ActivityPelangganTambah.this, "Tambah Data Gagal", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (db.updatePelanggan(idPelanggan, namapelanggan, alamat, notelp)) {
                    Toast.makeText(ActivityPelangganTambah.this, "Berhasil Memperbarui Data", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ActivityPelangganTambah.this, "Gagal Memperbarui Data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
