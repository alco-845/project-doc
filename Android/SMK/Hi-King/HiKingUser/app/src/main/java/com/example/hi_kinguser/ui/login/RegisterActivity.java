package com.example.hi_kinguser.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hi_kinguser.R;
import com.example.hi_kinguser.koneksi.Api;
import com.example.hi_kinguser.koneksi.ApiConfig;
import com.example.hi_kinguser.model.ModelPenyewa;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void cancel(View view) {
        finish();
    }

    public void register(View view) {
        EditText edtUsername = (EditText) findViewById(R.id.edtRegisUsername);
        EditText edtPassword = (EditText) findViewById(R.id.edtRegisPassword);
        EditText edtConfPassword = (EditText) findViewById(R.id.edtRegisConfirmPassword);
        EditText edtName = (EditText) findViewById(R.id.edtRegisEmail);
        EditText edtAddress = (EditText) findViewById(R.id.edtRegisAddress);
        EditText edtPhone = (EditText) findViewById(R.id.edtRegisPhone);

        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String confPassword = edtConfPassword.getText().toString();
        String name = edtName.getText().toString();
        String address = edtAddress.getText().toString();
        String phone = edtPhone.getText().toString();

        if (username.equals("") || password.equals("") || confPassword.equals("") || name.equals("") || address.equals("") || phone.equals("")) {
            Toast.makeText(RegisterActivity.this, "Isi data dengan benar", Toast.LENGTH_SHORT).show();
        } else {
            if (confPassword.equals(password)) {
                ApiConfig service1 = Api.getRetrofit().create(ApiConfig.class);
                Call<List<ModelPenyewa>> call1 = service1.getByName(username);
                call1.enqueue(new Callback<List<ModelPenyewa>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ModelPenyewa>> call, @NonNull Response<List<ModelPenyewa>> response) {
                        if (response.body().isEmpty()) {
                            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
                            Call<ModelPenyewa> call1 = service.insertPenyewa(username, password, name, address, phone);
                            call1.enqueue(new Callback<ModelPenyewa>() {
                                @Override
                                public void onResponse(@NonNull Call<ModelPenyewa> call, @NonNull Response<ModelPenyewa> response) {
                                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(@NonNull Call<ModelPenyewa> call, @NonNull Throwable t) {
                                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Username sudah pernah dipakai", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ModelPenyewa>> call, @NonNull Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            }
        }
    }
}