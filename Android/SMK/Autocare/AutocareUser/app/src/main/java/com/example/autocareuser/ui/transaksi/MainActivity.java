package com.example.autocareuser.ui.transaksi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocareuser.R;
import com.example.autocareuser.koneksi.Api;
import com.example.autocareuser.koneksi.ApiConfig;
import com.example.autocareuser.model.ModelItem;
import com.example.autocareuser.ui.login.LoginActivity;
import com.example.autocareuser.ui.login.RegisterActivity;

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

        SharedPreferences sh = getSharedPreferences("UserApp", MODE_PRIVATE);
        id = sh.getInt("id", 0);
        if (id == 0) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

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

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void profile(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("kondisi", "update");
        startActivity(intent);
    }
}