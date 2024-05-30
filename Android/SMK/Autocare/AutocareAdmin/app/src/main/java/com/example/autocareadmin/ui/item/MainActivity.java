package com.example.autocareadmin.ui.item;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocareadmin.R;
import com.example.autocareadmin.koneksi.Api;
import com.example.autocareadmin.koneksi.ApiConfig;
import com.example.autocareadmin.model.ModelItem;
import com.example.autocareadmin.ui.login.LoginActivity;
import com.example.autocareadmin.ui.transaksi.OrderActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recItem;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SharedPreferences sh = getSharedPreferences("AdminApp", MODE_PRIVATE);
        id = sh.getInt("id", 0);
        if (id == 0) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        ImageView ivLogout = findViewById(R.id.ivLogout);
        ivLogout.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog
                    .setMessage("Logout?")
                    .setPositiveButton("Yes", (dialog12, which) -> {
                        SharedPreferences sharedPreferences = getSharedPreferences("AdminApp", MODE_PRIVATE);
                        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                        prefEdit.remove("id");
                        prefEdit.remove("username");
                        prefEdit.apply();
                        reload();
                    })
                    .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
            dialog.create();
            dialog.show();
        });

        Intent intent = new Intent(this, AddItemActivity.class);
        ImageView ivAdd = findViewById(R.id.btnAdd);
        ivAdd.setOnClickListener(v -> startActivity(intent));

        Intent i = new Intent(this, OrderActivity.class);
        ImageView ivOrder = findViewById(R.id.ivOrder);
        ivOrder.setOnClickListener(v -> startActivity(i));

        recItem = (RecyclerView) findViewById(R.id.recItem);
        recItem.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));

        EditText edtSearch = (EditText) findViewById(R.id.edtSearchHome);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = edtSearch.getText().toString();
                if (keyword.equals("")) {
                    getData();
                } else {
                    getSearchData(keyword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void getData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.getItem();
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                ItemAdapter adapter = new ItemAdapter(MainActivity.this, response.body());
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearchData(String keyword) {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.searchItem(keyword);
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                ItemAdapter adapter = new ItemAdapter(MainActivity.this, response.body());
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
                .setMessage("Exit App?")
                .setPositiveButton("Yes", (dialog12, which) -> {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);

                })
                .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
        dialog.create();
        dialog.show();
    }

    public void reload(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}