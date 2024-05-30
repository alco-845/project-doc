package com.example.autocareadmin.ui.transaksi;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocareadmin.R;
import com.example.autocareadmin.koneksi.Api;
import com.example.autocareadmin.koneksi.ApiConfig;
import com.example.autocareadmin.model.ModelTransaksi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    RecyclerView recOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recOrder = (RecyclerView) findViewById(R.id.recOrder);
        recOrder.setLayoutManager(new LinearLayoutManager(this));

        getData();
    }

    public void getData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelTransaksi>> call = service.getTransaksiStatus("masuk");
        call.enqueue(new Callback<List<ModelTransaksi>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelTransaksi>> call, @NonNull Response<List<ModelTransaksi>> response) {
                OrderAdapter adapter = new OrderAdapter(OrderActivity.this, response.body());
                recOrder.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelTransaksi>> call, @NonNull Throwable t) {
                Toast.makeText(OrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}