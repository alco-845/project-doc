package com.example.autocareuser.ui.transaksi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autocareuser.R;
import com.example.autocareuser.helper.Helper;
import com.example.autocareuser.koneksi.Api;
import com.example.autocareuser.koneksi.ApiConfig;
import com.example.autocareuser.model.ModelTransaksi;
import com.example.autocareuser.ui.login.RegisterActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailItemActivity extends AppCompatActivity {

    int kategori = 0, iditem = 0, idadmin = 0;
    String kategoriValue = "", name = "", price = "", detail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        setTextData();
    }

    public void setTextData() {
        Bundle extra = getIntent().getExtras();

        kategori = Integer.parseInt(extra.getString("kategori"));
        if (kategori == 0) {
            kategoriValue = "Service Kendaraan";
        } else if (kategori == 1) {
            kategoriValue = "Ganti Oli";
        } else if (kategori == 2) {
            kategoriValue = "Ganti Ban";
        } else if (kategori == 3) {
            kategoriValue = "Ganti Sparepart";
        }

        iditem = extra.getInt("iditem");
        idadmin = extra.getInt("idadmin");
        name = extra.getString("name");
        detail = extra.getString("detail");
        price = extra.getString("price");

        TextView tvNameDetail = (TextView) findViewById(R.id.tvItemName);
        tvNameDetail.setText(name);

        TextView tvKat = (TextView) findViewById(R.id.tvItemKat);
        tvKat.setText(kategoriValue);

        TextView tvDescDetail = (TextView) findViewById(R.id.tvItemDetail);
        tvDescDetail.setText(detail + "\n\nHarga : Rp. " + price);
    }

    public void rent(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
            .setTitle("Anda yakin ingin memesan?")
            .setMessage("Pastikan nama, alamat, dan nomor telepon anda sudah benar")
            .setPositiveButton("Ya", (dialog1, which) -> {
                SharedPreferences sh = getSharedPreferences("UserApp", MODE_PRIVATE);
                int id = sh.getInt("id", 0);

                String dateNow = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
                Call<ModelTransaksi> call = service.insertTransaksi(idadmin, id, iditem, Helper.convertDate(dateNow), "masuk");
                call.enqueue(new Callback<ModelTransaksi>() {
                    @Override
                    public void onResponse(@NonNull Call<ModelTransaksi> call, @NonNull Response<ModelTransaksi> response) {
                        Toast.makeText(DetailItemActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(DetailItemActivity.this, WaitActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ModelTransaksi> call, @NonNull Throwable t) {
                        Toast.makeText(DetailItemActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            })
            .setNegativeButton("Tidak", (dialog2, id) -> dialog2.cancel())
            .setNeutralButton("Ubah profile", (dialog3, which) -> {
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("kondisi", "update");
                startActivity(intent);
            });
            dialog.create();
            dialog.show();
    }
}