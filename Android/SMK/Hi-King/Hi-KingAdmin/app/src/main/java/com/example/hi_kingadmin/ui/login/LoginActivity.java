package com.example.hi_kingadmin.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hi_kingadmin.R;
import com.example.hi_kingadmin.koneksi.Api;
import com.example.hi_kingadmin.koneksi.ApiConfig;
import com.example.hi_kingadmin.model.ModelPemilik;
import com.example.hi_kingadmin.ui.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sh = getSharedPreferences("AdminApp", MODE_PRIVATE);
        int id = sh.getInt("id", 0);
        if (id != 0) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void cancel(View view) {
        finish();
    }

    public void login(View view) {
        EditText edtUsername = (EditText) findViewById(R.id.edtLoginUsername);
        EditText edtPassword = (EditText) findViewById(R.id.edtLoginPassword);

        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "Isi dengan benar", Toast.LENGTH_SHORT).show();
        } else {
            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
            Call<List<ModelPemilik>> call = service.getByName(username);
            call.enqueue(new Callback<List<ModelPemilik>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelPemilik>> call, @NonNull Response<List<ModelPemilik>> response) {
                    if (response.body().isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Akun tidak ada", Toast.LENGTH_SHORT).show();
                    } else if (response.body().get(0).getPassword().equals(password)) {
                        SharedPreferences sharedPreferences = getSharedPreferences("AdminApp", MODE_PRIVATE);
                        SharedPreferences.Editor prefEdit = sharedPreferences.edit();

                        prefEdit.putInt("id", response.body().get(0).getIdpemilik());
                        prefEdit.putString("username", response.body().get(0).getUsername());
                        prefEdit.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Password Salah", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelPemilik>> call, @NonNull Throwable t) {
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}