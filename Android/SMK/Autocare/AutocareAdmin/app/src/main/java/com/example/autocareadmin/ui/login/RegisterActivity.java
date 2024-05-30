package com.example.autocareadmin.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autocareadmin.R;
import com.example.autocareadmin.koneksi.Api;
import com.example.autocareadmin.koneksi.ApiConfig;
import com.example.autocareadmin.model.ModelAdmin;

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

    public void login(View view) {
        finish();
    }

    public void register(View view) {
        EditText edtUsername = (EditText) findViewById(R.id.edtRegisUsername);
        EditText edtPassword = (EditText) findViewById(R.id.edtRegisPassword);
        EditText edtConfPassword = (EditText) findViewById(R.id.edtRegisConfirmPassword);

        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String confPassword = edtConfPassword.getText().toString();

        if (username.equals("") || password.equals("") || confPassword.equals("")) {
            Toast.makeText(RegisterActivity.this, "Isi data dengan benar", Toast.LENGTH_SHORT).show();
        } else {
            if (confPassword.equals(password)) {
                ApiConfig service1 = Api.getRetrofit().create(ApiConfig.class);
                Call<List<ModelAdmin>> call1 = service1.getByName(username);
                call1.enqueue(new Callback<List<ModelAdmin>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ModelAdmin>> call, @NonNull Response<List<ModelAdmin>> response) {
                        if (response.body().isEmpty()) {
                            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
                            Call<ModelAdmin> call1 = service.insertAdmin(username, password, "name", "address", "phone");
                            call1.enqueue(new Callback<ModelAdmin>() {
                                @Override
                                public void onResponse(@NonNull Call<ModelAdmin> call, @NonNull Response<ModelAdmin> response) {
                                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(@NonNull Call<ModelAdmin> call, @NonNull Throwable t) {
                                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Username sudah pernah dipakai", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ModelAdmin>> call, @NonNull Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            }
        }
    }
}