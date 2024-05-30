package com.example.birdbsadmin.ui.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.birdbsadmin.R;
import com.example.birdbsadmin.koneksi.Api;
import com.example.birdbsadmin.koneksi.ApiConfig;
import com.example.birdbsadmin.model.ModelBeli;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    double cashback;
    String jumlah, idbeli;

    TextInputEditText edtPay, edtReturn, edtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Bundle extra = getIntent().getExtras();
        idbeli = extra.getString("idbeli");

        setToolbar();
        setTextData();

        edtReturn = (TextInputEditText) findViewById(R.id.edtReturn);
        edtPay = (TextInputEditText) findViewById(R.id.edtPay);
        edtPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtPay.getText().toString().equals("")){
                    edtPay.setText("0");
                } else{
                    calculate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.title_payment);

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());
    }

    private void setTextData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeli>> call = service.getBeliById(idbeli);
        call.enqueue(new Callback<List<ModelBeli>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeli>> call, @NonNull Response<List<ModelBeli>> response) {
                edtTotal = findViewById(R.id.edtTotal);
                edtTotal.setText(response.body().get(0).getTotal());
                jumlah = response.body().get(0).getTotal();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelBeli>> call, @NonNull Throwable t) {
                Toast.makeText(PaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void calculate() {
        double masuk = Double.parseDouble(edtPay.getText().toString());
        double jum = Double.parseDouble((jumlah));
        double kembali;
        if (masuk > jum) {
            kembali = masuk - jum;
            cashback = kembali;
            edtReturn.setText(String.valueOf(kembali));
        } else if (masuk == jum) {
            kembali = 0;
            cashback = kembali;
            edtReturn.setText(String.valueOf(kembali));
        } else {
            String min;
            kembali = jum - masuk;
            if (kembali == 0){
                min = "";
            } else {
                min = "-";
            }
            cashback = kembali;
            edtReturn.setText(min + String.valueOf(kembali));
        }
    }

    public void payment(View view) {
        if (Integer.parseInt(edtTotal.getText().toString()) > Integer.parseInt(edtPay.getText().toString())) {
            Toast.makeText(this, "Uang Pembayaran Kurang", Toast.LENGTH_SHORT).show();
        } else {
            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
            Call<ModelBeli> call = service.updateBeli(Integer.parseInt(idbeli), edtPay.getText().toString(), edtReturn.getText().toString(), "1");
            call.enqueue(new Callback<ModelBeli>() {
                @Override
                public void onResponse(@NonNull Call<ModelBeli> call, @NonNull Response<ModelBeli> response) {
                    Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PaymentActivity.this, TransactionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onFailure(@NonNull Call<ModelBeli> call, @NonNull Throwable t) {
                    Toast.makeText(PaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}