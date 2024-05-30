package com.example.birdbsuser.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.birdbsuser.R;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelPembeli;

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
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void regis(View view) {
        EditText edtUsername = (EditText) findViewById(R.id.edtUsernameRegister);
        EditText edtPassword = (EditText) findViewById(R.id.edtPasswordRegister);
        EditText edtConfPassword = (EditText) findViewById(R.id.edtConfirmPasswordRegister);
        EditText edtNama = (EditText) findViewById(R.id.edtNamaRegister);
        EditText edtAlamat = (EditText) findViewById(R.id.edtAlamatRegister);
        EditText edtTelpon = (EditText) findViewById(R.id.edtTelponRegister);

        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String confPassword = edtConfPassword.getText().toString();
        String nama = edtNama.getText().toString();
        String alamat = edtAlamat.getText().toString();
        String telpon = edtTelpon.getText().toString();

        if (username.equals("") || password.equals("") || confPassword.equals("") || nama.equals("") || alamat.equals("") || telpon.equals("")) {
            Toast.makeText(RegisterActivity.this, "Isi data dengan benar", Toast.LENGTH_SHORT).show();
        } else {
            if (confPassword.equals(password)) {
                ApiConfig service1 = Api.getRetrofit().create(ApiConfig.class);
                Call<List<ModelPembeli>> call1 = service1.getByName(username);
                call1.enqueue(new Callback<List<ModelPembeli>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ModelPembeli>> call, @NonNull Response<List<ModelPembeli>> response) {
                        if (response.body().isEmpty()) {
                            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
                            Call<ModelPembeli> call1 = service.insertPembeli(username, password, nama, alamat, telpon);
                            call1.enqueue(new Callback<ModelPembeli>() {
                                @Override
                                public void onResponse(@NonNull Call<ModelPembeli> call, @NonNull Response<ModelPembeli> response) {
                                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(@NonNull Call<ModelPembeli> call, @NonNull Throwable t) {
                                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Username sudah pernah dipakai", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ModelPembeli>> call, @NonNull Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            }
        }
    }
}