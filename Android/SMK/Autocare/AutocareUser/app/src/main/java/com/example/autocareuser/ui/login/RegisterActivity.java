package com.example.autocareuser.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autocareuser.R;
import com.example.autocareuser.koneksi.Api;
import com.example.autocareuser.koneksi.ApiConfig;
import com.example.autocareuser.model.ModelPelanggan;
import com.example.autocareuser.ui.transaksi.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword, edtConfPassword, edtName, edtAddress, edtPhone;
    String kondisi, username, password, confPassword, name, address, phone;
    int id;

    ApiConfig service;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        sh = getSharedPreferences("UserApp", MODE_PRIVATE);
        id = sh.getInt("id", 0);
        if (id == 0) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        service = Api.getRetrofit().create(ApiConfig.class);

        Bundle extra = getIntent().getExtras();
        kondisi = extra.getString("kondisi");

        if (kondisi.equals("regis")) {
            Button btnLogout = findViewById(R.id.btnLogout);
            btnLogout.setVisibility(View.GONE);
        } else {
            TextView tvTittle = findViewById(R.id.tvTittle);
            tvTittle.setText("Update User");

            TextView tvLogin = findViewById(R.id.tvLogin);
            tvLogin.setVisibility(View.GONE);

            Call<List<ModelPelanggan>> call = service.getPelangganId(id);
            call.enqueue(new Callback<List<ModelPelanggan>>() {
                @Override
                public void onResponse(Call<List<ModelPelanggan>> call, Response<List<ModelPelanggan>> response) {
                    edtUsername.setText(response.body().get(0).getUsername());
                    edtPassword.setText(response.body().get(0).getPassword());
                    edtName.setText(response.body().get(0).getNama());
                    edtAddress.setText(response.body().get(0).getAlamat());
                    edtPhone.setText(response.body().get(0).getTelpon());
                }

                @Override
                public void onFailure(Call<List<ModelPelanggan>> call, Throwable t) {

                }
            });
        }
    }

    private void init() {
        edtUsername = (EditText) findViewById(R.id.edtRegisUsername);
        edtPassword = (EditText) findViewById(R.id.edtRegisPassword);
        edtConfPassword = (EditText) findViewById(R.id.edtRegisConfirmPassword);
        edtName = (EditText) findViewById(R.id.edtRegisName);
        edtAddress = (EditText) findViewById(R.id.edtRegisAddress);
        edtPhone = (EditText) findViewById(R.id.edtRegisPhone);

        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();
        confPassword = edtConfPassword.getText().toString();
        name = edtName.getText().toString();
        address = edtAddress.getText().toString();
        phone = edtPhone.getText().toString();
    }

    public void logout(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
                .setMessage("Logout?")
                .setPositiveButton("Yes", (dialog12, which) -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserApp", MODE_PRIVATE);
                    SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                    prefEdit.remove("id");
                    prefEdit.remove("username");
                    prefEdit.apply();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
        dialog.create();
        dialog.show();
    }

    public void login(View view) {
        finish();
    }

    public void register(View view) {
        init();
        if (username.equals("") || password.equals("") || confPassword.equals("") || name.equals("") || address.equals("") || phone.equals("")) {
            Toast.makeText(RegisterActivity.this, "Isi data dengan benar", Toast.LENGTH_SHORT).show();
        } else {
            if (confPassword.equals(password)) {
                if (kondisi.equals("regis")) {
                    Call<List<ModelPelanggan>> call1 = service.getByName(username);
                    call1.enqueue(new Callback<List<ModelPelanggan>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<ModelPelanggan>> call, @NonNull Response<List<ModelPelanggan>> response) {
                            if (response.body().isEmpty()) {
                                Call<ModelPelanggan> call1 = service.insertPelanggan(username, password, name, address, phone);
                                call1.enqueue(new Callback<ModelPelanggan>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ModelPelanggan> call, @NonNull Response<ModelPelanggan> response) {
                                        Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ModelPelanggan> call, @NonNull Throwable t) {
                                        Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Username sudah pernah dipakai", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<ModelPelanggan>> call, @NonNull Throwable t) {
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Call<ModelPelanggan> updatePelanggan = service.updatePelanggan(id, username, password, name, address, phone);
                    updatePelanggan.enqueue(new Callback<ModelPelanggan>() {
                        @Override
                        public void onResponse(@NonNull Call<ModelPelanggan> call, @NonNull Response<ModelPelanggan> response) {
                            SharedPreferences.Editor prefEdit = sh.edit();
                            prefEdit.putString("name", name);
                            prefEdit.apply();

                            Toast.makeText(RegisterActivity.this, "Success update", Toast.LENGTH_SHORT).show();
                            reload();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ModelPelanggan> call, @NonNull Throwable t) {
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void reload() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}