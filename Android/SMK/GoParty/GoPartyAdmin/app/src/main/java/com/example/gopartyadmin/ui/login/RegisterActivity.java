package com.example.gopartyadmin.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gopartyadmin.R;
import com.example.gopartyadmin.koneksi.Api;
import com.example.gopartyadmin.koneksi.ApiConfig;
import com.example.gopartyadmin.model.ModelPemilik;

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
        EditText edtName = (EditText) findViewById(R.id.edtNameRegister);
        EditText edtAddress = (EditText) findViewById(R.id.edtAddressRegister);
        EditText edtPhone = (EditText) findViewById(R.id.edtPhoneRegister);

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
                Call<List<ModelPemilik>> call1 = service1.getByName(username);
                call1.enqueue(new Callback<List<ModelPemilik>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ModelPemilik>> call, @NonNull Response<List<ModelPemilik>> response) {
                        if (response.body().isEmpty()) {
                            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
                            Call<ModelPemilik> call1 = service.insertPemilik(username, password, name, address, phone);
                            call1.enqueue(new Callback<ModelPemilik>() {
                                @Override
                                public void onResponse(@NonNull Call<ModelPemilik> call, @NonNull Response<ModelPemilik> response) {
                                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(@NonNull Call<ModelPemilik> call, @NonNull Throwable t) {
                                    Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Username sudah pernah dipakai", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ModelPemilik>> call, @NonNull Throwable t) {
                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            }
        }
    }
}