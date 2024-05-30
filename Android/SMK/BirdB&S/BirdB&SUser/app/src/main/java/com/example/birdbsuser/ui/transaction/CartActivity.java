package com.example.birdbsuser.ui.transaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsuser.R;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelBeli;
import com.example.birdbsuser.model.ModelBeliDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    int idbeli = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setToolbar();
        setData();
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.txt_cart);

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());

        ImageView ivCart = findViewById(R.id.btnCart);
        ivCart.setVisibility(View.GONE);
    }

    public void setData() {
        SharedPreferences sh = getSharedPreferences("UserApp", MODE_PRIVATE);
        idbeli = sh.getInt("idbeli", 0);
        String total = sh.getString("total", "0");

        TextView tvTotal = (TextView) findViewById(R.id.tvTotalCart);
        tvTotal.setText("Rp. " + total);

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeliDetail>> call = service.getBeliDetail(idbeli);
        call.enqueue(new Callback<List<ModelBeliDetail>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeliDetail>> call, @NonNull Response<List<ModelBeliDetail>> response) {
                CartAdapter adapter = new CartAdapter(CartActivity.this, response.body());
                RecyclerView recItem = (RecyclerView) findViewById(R.id.recCart);
                recItem.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelBeliDetail>> call, @NonNull Throwable t) {
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void submit(View view) {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeliDetail>> call = service.getBeliDetail(idbeli);
        call.enqueue(new Callback<List<ModelBeliDetail>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeliDetail>> call, @NonNull Response<List<ModelBeliDetail>> response) {
                if (response.body().isEmpty()) {
                    Toast.makeText(CartActivity.this, "Cart tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(CartActivity.this);
                    dialog
                            .setTitle("Done shopping?")
                            .setMessage("The cart will be cleared")
                            .setPositiveButton("Yes", (dialog12, which) -> {
                                ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
                                Call<ModelBeli> call1 = service.updateBeliStatus(idbeli, 2);
                                call1.enqueue(new Callback<ModelBeli>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ModelBeli> call, @NonNull Response<ModelBeli> response) {
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ModelBeli> call, @NonNull Throwable t) {
                                        Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                SharedPreferences sharedPreferences = getSharedPreferences("UserApp", MODE_PRIVATE);
                                SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                                prefEdit.remove("idbeli");
                                prefEdit.remove("total");
                                prefEdit.apply();

                                reload();
                            })
                            .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
                    dialog.create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelBeliDetail>> call, @NonNull Throwable t) {
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reload(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}