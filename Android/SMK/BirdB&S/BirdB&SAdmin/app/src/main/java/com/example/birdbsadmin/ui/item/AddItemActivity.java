package com.example.birdbsadmin.ui.item;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.birdbsadmin.R;
import com.example.birdbsadmin.koneksi.Api;
import com.example.birdbsadmin.koneksi.ApiConfig;
import com.example.birdbsadmin.model.ModelItem;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity {

    TextView tvTitle;
    TextInputEditText edtBirdName, edtBirdPrice;
    EditText edtBirdValue;
    ImageView btnAddJ, btnRemoveJ;

    Integer iditem, tJumlah = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

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
    }

    private void init() {
        edtBirdName = (TextInputEditText) findViewById(R.id.edtBirdName);
        edtBirdPrice = (TextInputEditText) findViewById(R.id.edtBirdPrice);
        edtBirdValue = (EditText) findViewById(R.id.edtBirdValue);

        btnAddJ = (ImageView) findViewById(R.id.ivPlus);
        btnAddJ.setOnClickListener(v -> {
            tJumlah=tJumlah+1;
            setJumlah(tJumlah);
        });
        btnRemoveJ = (ImageView) findViewById(R.id.ivMin);
        btnRemoveJ.setOnClickListener(v -> {
            tJumlah=tJumlah-1;
            setJumlah(tJumlah);
        });

        Bundle extra = getIntent().getExtras();
        if (extra == null){
            // Insert
            tJumlah = 0;

            tvTitle.setText(R.string.title_add_item);
            iditem = null;
        } else {
            tJumlah = Integer.parseInt(extra.getString("value"));
            if (tJumlah != 0) {
                btnRemoveJ.setVisibility(View.VISIBLE);
            }

            tvTitle.setText("Update Item");
            iditem = extra.getInt("iditem");
            edtBirdName.setText(extra.getString("name"));
            edtBirdPrice.setText(extra.getString("price"));
            edtBirdValue.setText(extra.getString("value"));
        }
    }

    public void submit(View view) {
        String birdName = edtBirdName.getText().toString();
        String birdPrice = edtBirdPrice.getText().toString();
        String birdValue = edtBirdValue.getText().toString();

        if (birdName.equals("") || birdPrice.equals("") || birdValue.equals("0")) {
            Toast.makeText(AddItemActivity.this, "Isi data dengan benar", Toast.LENGTH_SHORT).show();
        } else {
            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);

            SharedPreferences sh = getSharedPreferences("AdminApp", MODE_PRIVATE);
            int id = sh.getInt("id", 0);

            if (iditem == null) {
                Call<ResponseBody> call = service.insertItem(id, birdName, birdPrice, Integer.parseInt(birdValue));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        Toast.makeText(AddItemActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(AddItemActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Call<ModelItem> call = service.updateItem(iditem, id, birdName, birdPrice, Integer.parseInt(birdValue));
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
        edtBirdValue.setText(String.valueOf(jumlah));
        btnAddJ.setVisibility(View.VISIBLE);

        if (jumlah<1){
            btnRemoveJ.setVisibility(View.INVISIBLE);
        }else {
            btnRemoveJ.setVisibility(View.VISIBLE);
        }
    }
}