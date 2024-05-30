package com.example.autocareadmin.ui.item;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autocareadmin.R;
import com.example.autocareadmin.koneksi.Api;
import com.example.autocareadmin.koneksi.ApiConfig;
import com.example.autocareadmin.model.ModelItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailItemActivity extends AppCompatActivity {

    int kategori = 0, iditem = 0;
    String kategoriValue = "", name = "", price = "", detail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        setTextData();

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog
                    .setMessage("Delete this item?")
                    .setPositiveButton("Yes", (dialog12, which) -> {
                        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
                        Call<ModelItem> call = service.deleteItem(iditem);
                        call.enqueue(new Callback<ModelItem>() {
                            @Override
                            public void onResponse(@NonNull Call<ModelItem> call, @NonNull Response<ModelItem> response) {
                                Toast.makeText(DetailItemActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ModelItem> call, @NonNull Throwable t) {
                                Toast.makeText(DetailItemActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
            dialog.create();
            dialog.show();
        });

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddItemActivity.class);
            intent.putExtra("iditem", iditem);
            intent.putExtra("kategori", String.valueOf(kategori));
            intent.putExtra("name", name);
            intent.putExtra("detail", detail);
            intent.putExtra("price", price);
            startActivity(intent);
        });
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
}