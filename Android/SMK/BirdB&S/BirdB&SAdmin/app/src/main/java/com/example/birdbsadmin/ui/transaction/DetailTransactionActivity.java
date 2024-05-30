package com.example.birdbsadmin.ui.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsadmin.R;
import com.example.birdbsadmin.helper.Helper;
import com.example.birdbsadmin.koneksi.Api;
import com.example.birdbsadmin.koneksi.ApiConfig;
import com.example.birdbsadmin.model.ModelBeli;
import com.example.birdbsadmin.model.ModelBeliDetail;
import com.example.birdbsadmin.model.ModelPembeli;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransactionActivity extends AppCompatActivity {

    RecyclerView recDetail;
    String idbeli, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaction);

        recDetail = (RecyclerView) findViewById(R.id.recDetail);
        recDetail.setLayoutManager(new LinearLayoutManager(this));

        Bundle extra = getIntent().getExtras();
        idbeli = extra.getString("idbeli");
        status = extra.getString("status");
        if (status.equals("1")) {
            Button btnPay = findViewById(R.id.btnPay);
            btnPay.setVisibility(View.GONE);
        }

        setToolbar();
        setTextData();
        getData();
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.title_detail_transaction);

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());
    }

    private void setTextData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeli>> call = service.getBeliById(idbeli);
        call.enqueue(new Callback<List<ModelBeli>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeli>> call, @NonNull Response<List<ModelBeli>> response) {
                TextView tvDate = (TextView) findViewById(R.id.tvDate);
                tvDate.setText(Helper.dateToNormal(response.body().get(0).getTglbeli()));

                Call<List<ModelPembeli>> call1 = service.getPembeliId(response.body().get(0).getIdpembeli());
                call1.enqueue(new Callback<List<ModelPembeli>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<ModelPembeli>> call, @NonNull Response<List<ModelPembeli>> responsee) {
                        TextView tvName = (TextView) findViewById(R.id.tvName);
                        tvName.setText(responsee.body().get(0).getNama());

                        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
                        tvAddress.setText(responsee.body().get(0).getAlamat());

                        TextView tvPhone = (TextView) findViewById(R.id.tvPhone);
                        tvPhone.setText(responsee.body().get(0).getTelpon());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<ModelPembeli>> call, @NonNull Throwable t) {
                        Toast.makeText(DetailTransactionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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
                Toast.makeText(DetailTransactionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeliDetail>> call = service.getBeliDetail(idbeli);
        call.enqueue(new Callback<List<ModelBeliDetail>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeliDetail>> call, @NonNull Response<List<ModelBeliDetail>> response) {
                Toast.makeText(DetailTransactionActivity.this, "Loaded", Toast.LENGTH_SHORT).show();

                DetailAdapter adapter = new DetailAdapter(DetailTransactionActivity.this, response.body());
                recDetail.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelBeliDetail>> call, @NonNull Throwable t) {
                Toast.makeText(DetailTransactionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void payment(View view) {
        Intent intent = new Intent(DetailTransactionActivity.this, PaymentActivity.class);
        intent.putExtra("idbeli", idbeli);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTextData();
    }
}