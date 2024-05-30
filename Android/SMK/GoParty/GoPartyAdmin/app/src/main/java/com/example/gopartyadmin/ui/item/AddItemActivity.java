package com.example.gopartyadmin.ui.item;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gopartyadmin.R;
import com.example.gopartyadmin.koneksi.Api;
import com.example.gopartyadmin.koneksi.ApiConfig;
import com.example.gopartyadmin.model.ModelItem;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity {

    TextView tvTitle;
    TextInputEditText edtItemName, edtPrice;
    EditText edtValue;
    ImageView btnAddJ, btnRemoveJ;
    Spinner spKat;

    Integer iditem, tJumlah = 0, kategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setToolbar();
        init();
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvTitle = findViewById(R.id.tvToolbar);

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());

        ImageView ivCart = findViewById(R.id.btnAdd);
        ivCart.setVisibility(View.GONE);
    }

    private void init() {
        edtItemName = (TextInputEditText) findViewById(R.id.edtItemName);
        edtPrice = (TextInputEditText) findViewById(R.id.edtPrice);
        edtValue = (EditText) findViewById(R.id.edtSearch);

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

        btnAddJ = (ImageView) findViewById(R.id.btnAddItem);
        btnAddJ.setOnClickListener(v -> {
            tJumlah=tJumlah+1;
            setJumlah(tJumlah);
        });
        btnRemoveJ = (ImageView) findViewById(R.id.btnMinItem);
        btnRemoveJ.setOnClickListener(v -> {
            tJumlah=tJumlah-1;
            setJumlah(tJumlah);
        });

        Bundle extra = getIntent().getExtras();
        if (extra == null){
            // Insert
            tJumlah = 0;

            tvTitle.setText("Add Item");
            iditem = null;
        } else {
            tJumlah = Integer.parseInt(extra.getString("value"));
            if (tJumlah != 0) {
                btnRemoveJ.setVisibility(View.VISIBLE);
            }

            spKat.setSelection(Integer.parseInt(extra.getString("kategori")));

            tvTitle.setText("Update Item");
            iditem = extra.getInt("iditem");
            edtItemName.setText(extra.getString("name"));
            edtPrice.setText(extra.getString("price"));
            edtValue.setText(extra.getString("value"));
        }
    }

    public void submit(View view) {
        String itemName = edtItemName.getText().toString();
        String itemPrice = edtPrice.getText().toString();
        String itemValue = edtValue.getText().toString();

        if (itemName.equals("") || itemPrice.equals("") || itemValue.equals("0")) {
            Toast.makeText(AddItemActivity.this, "Isi data dengan benar", Toast.LENGTH_SHORT).show();
        } else {
            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);

            SharedPreferences sh = getSharedPreferences("AdminApp", MODE_PRIVATE);
            int id = sh.getInt("id", 0);

            if (iditem == null) {
                Call<ModelItem> call = service.insertItem(id, kategori, itemName, itemPrice, Integer.parseInt(itemValue));
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
                Call<ModelItem> call = service.updateItem(iditem, id, kategori, itemName, itemPrice, Integer.parseInt(itemValue));
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
            }
        }
    }

    private void setJumlah(Integer jumlah){
        edtValue.setText(String.valueOf(jumlah));
        btnAddJ.setVisibility(View.VISIBLE);

        if (jumlah<1){
            btnRemoveJ.setVisibility(View.INVISIBLE);
        }else {
            btnRemoveJ.setVisibility(View.VISIBLE);
        }
    }
}