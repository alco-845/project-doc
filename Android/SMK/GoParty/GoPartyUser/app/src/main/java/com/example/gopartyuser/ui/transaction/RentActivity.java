package com.example.gopartyuser.ui.transaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gopartyuser.R;
import com.example.gopartyuser.helper.Helper;
import com.example.gopartyuser.koneksi.Api;
import com.example.gopartyuser.koneksi.ApiConfig;
import com.example.gopartyuser.model.ModelItem;
import com.example.gopartyuser.model.ModelSewa;
import com.example.gopartyuser.ui.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentActivity extends AppCompatActivity {

    EditText edtValue;
    TextInputEditText edtTglMulai, edtTglSelesai;
    ImageView btnAddJ, btnRemoveJ, ivTglMulai, ivTglSelesai;

    Calendar calendar;
    String iditem = "", idpemilik = "", jumlahItem = "", harga = "";
    int year, month, day, tJumlah = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setToolbar();
        init();
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText("Rent");

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());
    }

    private void init() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Bundle extra = getIntent().getExtras();

        idpemilik = extra.getString("idpemilik");
        iditem = extra.getString("iditem");
        jumlahItem = extra.getString("value");
        harga = extra.getString("price");

        TextView tvOwnerRent = (TextView) findViewById(R.id.tvOwnerRent);
        tvOwnerRent.setText(extra.getString("owner"));

        edtValue = (EditText) findViewById(R.id.edtValue);

        btnAddJ = (ImageView) findViewById(R.id.ivPlus);
        btnAddJ.setOnClickListener(v -> {
            tJumlah=tJumlah+1;
            setJumlah(tJumlah);

            if (tJumlah == Integer.parseInt(jumlahItem)) {
                btnAddJ.setVisibility(View.INVISIBLE);
            } else {
                btnAddJ.setVisibility(View.VISIBLE);
            }
        });
        btnRemoveJ = (ImageView) findViewById(R.id.ivMin);
        btnRemoveJ.setOnClickListener(v -> {
            tJumlah=tJumlah-1;
            setJumlah(tJumlah);

            if (tJumlah == Integer.parseInt(jumlahItem)) {
                btnAddJ.setVisibility(View.INVISIBLE);
            } else {
                btnAddJ.setVisibility(View.VISIBLE);
            }
        });

        String date_n=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        edtTglMulai = (TextInputEditText) findViewById(R.id.edtTglMulai);
        edtTglMulai.setText(date_n);

        edtTglSelesai = (TextInputEditText) findViewById(R.id.edtTglSelesai);
        edtTglSelesai.setText(date_n);

        ivTglMulai = (ImageView) findViewById(R.id.ivTglMulai);
        ivTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        ivTglSelesai = (ImageView) findViewById(R.id.ivTglSelesai);
        ivTglSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(2);
            }
        });
    }

    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, dMulai, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this, dSelesai, year, month, day);
        }
        return null;
    }

    private final DatePickerDialog.OnDateSetListener dMulai = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            edtTglMulai.setText(Helper.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private final DatePickerDialog.OnDateSetListener dSelesai = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            edtTglSelesai.setText(Helper.setDatePickerNormal(thn, bln + 1, day));
        }
    };

    private void setJumlah(Integer jumlah){
        edtValue.setText(String.valueOf(jumlah));
        btnAddJ.setVisibility(View.VISIBLE);

        if (jumlah<1){
            btnRemoveJ.setVisibility(View.INVISIBLE);
        }else {
            btnRemoveJ.setVisibility(View.VISIBLE);
        }
    }

    public void submit(View view) {
        String tglMulai = edtTglMulai.getText().toString();
        String tglSelesai = edtTglSelesai.getText().toString();
        String jumlah = edtValue.getText().toString();
        int total = Integer.parseInt(jumlah) * Integer.parseInt(harga);

        SharedPreferences sh = getSharedPreferences("UserApp", MODE_PRIVATE);
        int id = sh.getInt("id", 0);
        String name = sh.getString("name", "");

        if (jumlah.equals("0")) {
            Toast.makeText(RentActivity.this, "Jumlah tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
            Call<ModelSewa> call = service.insertSewa(Integer.parseInt(iditem), Integer.parseInt(idpemilik), id, name, Helper.convertDate(tglMulai), Helper.convertDate(tglSelesai), Integer.parseInt(jumlah), total, "sewa");
            call.enqueue(new Callback<ModelSewa>() {
                @Override
                public void onResponse(@NonNull Call<ModelSewa> call, @NonNull Response<ModelSewa> response) {
                    Call<ModelItem> updateItem = service.updateJumlahItem(Integer.parseInt(iditem), Integer.parseInt(jumlahItem) - Integer.parseInt(jumlah));
                    updateItem.enqueue(new Callback<ModelItem>() {
                        @Override
                        public void onResponse(@NonNull Call<ModelItem> call, @NonNull Response<ModelItem> response) {
                        }

                        @Override
                        public void onFailure(@NonNull Call<ModelItem> call, @NonNull Throwable t) {
                            Toast.makeText(RentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Toast.makeText(RentActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RentActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onFailure(@NonNull Call<ModelSewa> call, @NonNull Throwable t) {
                    Toast.makeText(RentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}