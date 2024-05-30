package com.example.birdbsuser.ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.birdbsuser.R;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelPembeli;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    TextInputEditText edtName, edtAddress, edtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        setToolbar();
        setTextData();
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.txt_update_profile);

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());

        ImageView ivCart = findViewById(R.id.btnCart);
        ivCart.setVisibility(View.GONE);
    }

    private void setTextData() {
        SharedPreferences sh = getSharedPreferences("UserApp", MODE_PRIVATE);
        int id = sh.getInt("id", 0);

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelPembeli>> call = service.getPembeliId(id);
        call.enqueue(new Callback<List<ModelPembeli>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelPembeli>> call, @NonNull Response<List<ModelPembeli>> response) {
                edtName = (TextInputEditText) findViewById(R.id.edtName);
                edtName.setText(response.body().get(0).getNama());

                edtAddress = (TextInputEditText) findViewById(R.id.edtAddress);
                edtAddress.setText(response.body().get(0).getAlamat());

                edtPhone = (TextInputEditText) findViewById(R.id.edtPhone);
                edtPhone.setText(response.body().get(0).getTelpon());
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelPembeli>> call, @NonNull Throwable t) {
                Toast.makeText(UpdateProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void update(View view) {
        String name = edtName.getText().toString();
        String address = edtAddress.getText().toString();
        String phone = edtPhone.getText().toString();

        if (name.equals("") || address.equals("") || phone.equals("")) {
            Toast.makeText(UpdateProfileActivity.this, "Isi data dengan benar", Toast.LENGTH_SHORT).show();
        } else {
            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);

            SharedPreferences sh = getSharedPreferences("UserApp", MODE_PRIVATE);
            int id = sh.getInt("id", 0);

            Call<ModelPembeli> call = service.updatePembeli(id, name, address, phone);
            call.enqueue(new Callback<ModelPembeli>() {
                @Override
                public void onResponse(@NonNull Call<ModelPembeli> call, @NonNull Response<ModelPembeli> response) {
                    Toast.makeText(UpdateProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<ModelPembeli> call, @NonNull Throwable t) {
                    Toast.makeText(UpdateProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}