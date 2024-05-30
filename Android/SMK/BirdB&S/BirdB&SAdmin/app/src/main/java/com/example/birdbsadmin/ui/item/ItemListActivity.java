package com.example.birdbsadmin.ui.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsadmin.R;
import com.example.birdbsadmin.koneksi.Api;
import com.example.birdbsadmin.koneksi.ApiConfig;
import com.example.birdbsadmin.model.ModelItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemListActivity extends AppCompatActivity {

    RecyclerView recItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        recItem = (RecyclerView) findViewById(R.id.recBird);
        recItem.setLayoutManager(new LinearLayoutManager(this));

        setToolbar();
        getData();
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.title_list_item);

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());
    }

    private void getData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.getItem();
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                Toast.makeText(ItemListActivity.this, "Loaded", Toast.LENGTH_SHORT).show();

                ItemAdapter adapter = new ItemAdapter(ItemListActivity.this, response.body());
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(ItemListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void add(View view) {
        Intent intent = new Intent(ItemListActivity.this, AddItemActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}