package com.example.autocareadmin.ui.item;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autocareadmin.R;
import com.example.autocareadmin.koneksi.Api;
import com.example.autocareadmin.koneksi.ApiConfig;
import com.example.autocareadmin.model.ModelItem;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity {

    TextInputEditText edtItemName, edtPrice;
    EditText edtDetail;
    Spinner spKat;

    Integer iditem, kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        init();
    }

    private void init() {
        ImageView btnBack = findViewById(R.id.btnBackItem);
        btnBack.setOnClickListener(v -> finish());

        edtItemName = (TextInputEditText) findViewById(R.id.edtItemName);
        edtPrice = (TextInputEditText) findViewById(R.id.edtPrice);
        edtDetail = (EditText) findViewById(R.id.edtDetail);

        spKat = (Spinner) findViewById(R.id.spKat);
        spKat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kategori = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra == null){
            // Insert
            iditem = null;
        } else {
            spKat.setSelection(Integer.parseInt(extra.getString("kategori")));

            iditem = extra.getInt("iditem");
            edtItemName.setText(extra.getString("name"));
            edtDetail.setText(extra.getString("detail"));
            edtPrice.setText(extra.getString("price"));
        }
    }

    public void submit(View view) {
        String itemName = edtItemName.getText().toString();
        String itemPrice = edtPrice.getText().toString();
        String itemSpek = edtDetail.getText().toString();

        if (itemName.equals("") || itemPrice.equals("") || itemSpek.equals("")) {
            Toast.makeText(AddItemActivity.this, "Isi data dengan benar", Toast.LENGTH_SHORT).show();
        } else {
            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);

            SharedPreferences sh = getSharedPreferences("AdminApp", MODE_PRIVATE);
            int id = sh.getInt("id", 0);

            if (iditem == null) {
                Call<ModelItem> call = service.insertItem(id, kategori, itemName, itemSpek, itemPrice);
                call.enqueue(new Callback<ModelItem>() {
                    @Override
                    public void onResponse(@NonNull Call<ModelItem> call, @NonNull Response<ModelItem> response) {
                        Toast.makeText(AddItemActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ModelItem> call, @NonNull Throwable t) {
                        Toast.makeText(AddItemActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Call<ModelItem> call = service.updateItem(iditem, id, kategori, itemName, itemSpek, itemPrice);
                call.enqueue(new Callback<ModelItem>() {
                    @Override
                    public void onResponse(@NonNull Call<ModelItem> call, @NonNull Response<ModelItem> response) {
                        Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        Toast.makeText(AddItemActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ModelItem> call, @NonNull Throwable t) {
                        Toast.makeText(AddItemActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}