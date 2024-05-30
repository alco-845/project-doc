package com.example.gopartyuser.ui.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gopartyuser.R;
import com.example.gopartyuser.koneksi.Api;
import com.example.gopartyuser.koneksi.ApiConfig;
import com.example.gopartyuser.model.ModelPemilik;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransactionActivity extends AppCompatActivity {

    int kategori = 0, iditem = 0, idpemilik = 0;
    String kategoriValue = "", name = "", price = "", value = "", owner = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);

        setToolbar();
        setTextData();

        Button btnRent = findViewById(R.id.btnRent);
        btnRent.setOnClickListener(v -> {
            Intent intent = new Intent(this, RentActivity.class);
            intent.putExtra("owner", owner);
            intent.putExtra("idpemilik", String.valueOf(idpemilik));
            intent.putExtra("iditem", String.valueOf(iditem));
            intent.putExtra("value", value);
            intent.putExtra("price", price);
            startActivity(intent);
        });
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText("Detail");

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());
    }

    public void setTextData() {
        Bundle extra = getIntent().getExtras();

        kategori = Integer.parseInt(extra.getString("kategori"));
        if (kategori == 0) {
            kategoriValue = "Tenda Pesta";
        } else if (kategori == 1) {
            kategoriValue = "Meja Kursi";
        } else if (kategori == 2) {
            kategoriValue = "Alat Makan";
        } else {
            kategoriValue = "Sound System";
        }

        iditem = extra.getInt("iditem");
        idpemilik = extra.getInt("idpemilik");
        name = extra.getString("name");
        price = extra.getString("price");
        value = extra.getString("value");

        TextView tvDescDetail = (TextView) findViewById(R.id.tvDescDetail);

        String ket = extra.getString("ket");
        Button btnRent = (Button) findViewById(R.id.btnRent);
        if (ket.equals("history")) {
            btnRent.setVisibility(View.GONE);
            tvDescDetail.setText("Kategori : " + kategoriValue + "\nRp. " + price + "\nQty : " + value);
        } else {
            btnRent.setVisibility(View.VISIBLE);
            tvDescDetail.setText("Kategori : " + kategoriValue + "\nRp. " + price + " / day" + "\nQty : " + value);
        }


        TextView tvNameDetail = (TextView) findViewById(R.id.tvNameDetail);
        tvNameDetail.setText(name);

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelPemilik>> call = service.getPemilikId(idpemilik);
        call.enqueue(new Callback<List<ModelPemilik>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelPemilik>> call, @NonNull Response<List<ModelPemilik>> response) {
                owner = "Rental Owner : \n" + response.body().get(0).getNama() + "\n" + response.body().get(0).getAlamat() + "\n" + response.body().get(0).getTelpon();

                TextView tvOwnerDetail = (TextView) findViewById(R.id.tvOwnerDetail);
                tvOwnerDetail.setText(owner);
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelPemilik>> call, @NonNull Throwable t) {
                Toast.makeText(DetailTransactionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}