package com.example.birdbsuser.ui.history;

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

import com.example.birdbsuser.R;
import com.example.birdbsuser.helper.Helper;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelBeli;
import com.example.birdbsuser.model.ModelBeliDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailHistoryActivity extends AppCompatActivity {

    RecyclerView recDetail;
    String idbeli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);

        recDetail = (RecyclerView) findViewById(R.id.recDetail);
        recDetail.setLayoutManager(new LinearLayoutManager(this));

        Bundle extra = getIntent().getExtras();
        idbeli = extra.getString("idbeli");

        setToolbar();
        setTextData();
        setData();
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.title_detail_history);

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());

        ImageView ivCart = findViewById(R.id.btnCart);
        ivCart.setVisibility(View.GONE);
    }

    private void setTextData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeli>> call = service.getBeliById(idbeli);
        call.enqueue(new Callback<List<ModelBeli>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeli>> call, @NonNull Response<List<ModelBeli>> response) {
                TextView tvDate = (TextView) findViewById(R.id.tvDate);
                tvDate.setText(Helper.dateToNormal(response.body().get(0).getTglbeli()));

                String status;
                if (response.body().get(0).getStatus().equals("1")) {
                    status = "Already paid";
                } else {
                    status = "Not yet paid";
                }

                TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
                tvStatus.setText("Status : " +status);

                TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
                tvTotal.setText("Total : Rp. " + response.body().get(0).getTotal());

                TextView tvPay = (TextView) findViewById(R.id.tvPay);
                tvPay.setText("Pay : Rp. " + response.body().get(0).getBayar());

                TextView tvReturn = (TextView) findViewById(R.id.tvReturn);
                tvReturn.setText("Return : Rp. " + response.body().get(0).getKembali());
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelBeli>> call, @NonNull Throwable t) {
                Toast.makeText(DetailHistoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeliDetail>> call = service.getBeliDetail(Integer.parseInt(idbeli));
        call.enqueue(new Callback<List<ModelBeliDetail>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeliDetail>> call, @NonNull Response<List<ModelBeliDetail>> response) {
                DetailAdapter adapter = new DetailAdapter(DetailHistoryActivity.this, response.body());
                recDetail.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelBeliDetail>> call, @NonNull Throwable t) {
                Toast.makeText(DetailHistoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}