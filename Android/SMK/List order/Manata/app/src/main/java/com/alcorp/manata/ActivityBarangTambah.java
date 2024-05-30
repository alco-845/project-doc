package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.List;

public class ActivityBarangTambah extends AppCompatActivity {

    Database db;
    Config config;
    Toolbar appbar;
    TextView judUkuran;
    TextInputLayout inpWarna, inpUkuran;
    TextInputEditText edtBarang, edtWarna, edtVariasi;
    Spinner spJenis, spUkuran;
    Integer idbarang, posJenis, posUkuran;
    String sKat = "", sJenis = "", sUkuran = "", barang, warna, variasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang_tambah);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db = new Database(this);
        config = new Config(getSharedPreferences("config",this.MODE_PRIVATE));

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

        edtBarang = (TextInputEditText) findViewById(R.id.tNamaBarang);
        edtWarna = (TextInputEditText) findViewById(R.id.tWarna);
        edtVariasi = (TextInputEditText) findViewById(R.id.tVariasi);

        spJenis = (Spinner) findViewById(R.id.spJenis);
        spUkuran = (Spinner) findViewById(R.id.spUkuran);

        judUkuran = (TextView) findViewById(R.id.tvJudUkuran);
        inpWarna = (TextInputLayout) findViewById(R.id.input2);
        inpUkuran = (TextInputLayout) findViewById(R.id.input3);

        spJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 1){
                    inpWarna.setVisibility(View.GONE);
                    inpUkuran.setVisibility(View.GONE);
                    spUkuran.setVisibility(View.GONE);
                    judUkuran.setVisibility(View.GONE);
                } else {
                    inpWarna.setVisibility(View.VISIBLE);
                    inpUkuran.setVisibility(View.VISIBLE);
                    spUkuran.setVisibility(View.VISIBLE);
                    judUkuran.setVisibility(View.VISIBLE);
                }
                sJenis = parent.getItemAtPosition(pos).toString();
                posJenis = parent.getSelectedItemPosition();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spUkuran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                sUkuran = parent.getItemAtPosition(pos).toString();
                posUkuran = parent.getSelectedItemPosition();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            //Insert
            idbarang = null;

            tvTb.setText("Tambah Barang / Jasa");

            edtWarna.setText("Warna : ");
        } else {
            tvTb.setText("Update Barang / Jasa");

            idbarang = extra.getInt("idbarang");
            edtBarang.setText(extra.getString("barang"));
            edtWarna.setText(extra.getString("warna"));
            edtVariasi.setText(extra.getString("variasi"));

            spJenis.setSelection(Integer.parseInt(extra.getString("posjenis")));
            spUkuran.setSelection(Integer.parseInt(extra.getString("posukuran")));

            if (extra.getString("jenis").equals("Jasa")){
                inpWarna.setVisibility(View.GONE);
                inpUkuran.setVisibility(View.GONE);
                spUkuran.setVisibility(View.GONE);
                judUkuran.setVisibility(View.GONE);

                edtWarna.setText("Warna : ");
                spUkuran.setSelection(0);
            } else {
                inpWarna.setVisibility(View.VISIBLE);
                inpUkuran.setVisibility(View.VISIBLE);
                spUkuran.setVisibility(View.VISIBLE);
                judUkuran.setVisibility(View.VISIBLE);
            }
        }
    }

    public void simpan(View view) {
        barang = edtBarang.getText().toString();
        warna = edtWarna.getText().toString();
        variasi = edtVariasi.getText().toString();

        if (sJenis.equals("Jasa")){
            if (barang.equals("")) {
                Toast.makeText(ActivityBarangTambah.this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
            } else {
                if (idbarang == null) {
                    if (db.insertBarang(barang, sJenis, "", "", "")) {
                        config.setCustom("posjenis" + barang, String.valueOf(posJenis));
                        config.setCustom("posukuran" + barang, String.valueOf(posUkuran));
                        Toast.makeText(ActivityBarangTambah.this, "Tambah Data Berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityBarangTambah.this, "Tambah Data Gagal", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (db.updateBarang(idbarang, barang, sJenis, "", "", "")) {
                        config.setCustom("posjenis" + barang, String.valueOf(posJenis));
                        config.setCustom("posukuran" + barang, String.valueOf(posUkuran));
                        Toast.makeText(ActivityBarangTambah.this, "Berhasil Memperbarui Data", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityBarangTambah.this, "Gagal Memperbarui Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            if (barang.equals("") || warna.equals("") || variasi.equals("")) {
                Toast.makeText(ActivityBarangTambah.this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
            } else {
                if (idbarang == null) {
                    if (db.insertBarang(barang, sJenis, warna, variasi, sUkuran)) {
                        config.setCustom("posjenis" + barang, String.valueOf(posJenis));
                        config.setCustom("posukuran" + barang, String.valueOf(posUkuran));
                        Toast.makeText(ActivityBarangTambah.this, "Tambah Data Berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityBarangTambah.this, "Tambah Data Gagal", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (db.updateBarang(idbarang, barang, sJenis, warna, variasi, sUkuran)) {
                        config.setCustom("posjenis" + barang, String.valueOf(posJenis));
                        config.setCustom("posukuran" + barang, String.valueOf(posUkuran));
                        Toast.makeText(ActivityBarangTambah.this, "Berhasil Memperbarui Data", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ActivityBarangTambah.this, "Gagal Memperbarui Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

}