package com.example.autocareadmin.ui.transaksi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autocareadmin.R;
import com.example.autocareadmin.koneksi.Api;
import com.example.autocareadmin.koneksi.ApiConfig;
import com.example.autocareadmin.model.ModelTransaksi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptOrderActivity extends AppCompatActivity {

    TextView tvNama, tvAlamat, tvTelpon, tvItem;
    Integer iditem, idtransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order);

        init();
    }

    private void init() {
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        tvNama = findViewById(R.id.tvNamaPelanggan);
        tvAlamat = findViewById(R.id.tvAlamatPelanggan);
        tvTelpon = findViewById(R.id.tvTelpPelanggan);
        tvItem = findViewById(R.id.tvPermintaan);

        Bundle extra = getIntent().getExtras();

        idtransaksi = extra.getInt("idtransaksi");
        iditem = extra.getInt("iditem");
        tvNama.setText(extra.getString("name"));
        tvAlamat.setText(extra.getString("address"));
        tvTelpon.setText(extra.getString("phone"));
        tvItem.setText(extra.getString("item"));
    }

    public void confirm(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog
                .setMessage("Accept this order?")
                .setPositiveButton("Yes", (dialog12, which) -> {
                    ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
                    Call<ModelTransaksi> call = service.updateTransaksi(idtransaksi, "selesai");
                    call.enqueue(new Callback<ModelTransaksi>() {
                        @Override
                        public void onResponse(@NonNull Call<ModelTransaksi> call, @NonNull Response<ModelTransaksi> response) {
                            Intent intent = new Intent(AcceptOrderActivity.this, ConfirmActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(@NonNull Call<ModelTransaksi> call, @NonNull Throwable t) {
                            Toast.makeText(AcceptOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
        dialog.create();
        dialog.show();
    }
}