package com.example.birdbsadmin.ui.transaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionActivity extends AppCompatActivity {

    RecyclerView recTransaction;
    Spinner spinner;
    Button btnDari, btnKe;

    String dari, ke;
    Calendar calendar ;
    int year,month, day ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        recTransaction = (RecyclerView) findViewById(R.id.recTransaction);
        recTransaction.setLayoutManager(new LinearLayoutManager(this));

        btnDari = (Button) findViewById(R.id.btnDari);
        btnKe = (Button) findViewById(R.id.btnKe);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dari = Helper.setDatePicker(year,month+1,day) ;
        ke = Helper.setDatePicker(year,month+1,day) ;

        String now = Helper.setDatePickerNormal(year,month+1,day) ;
        btnDari.setText(now);
        btnKe.setText(now);

        setToolbar();

        spinner = (Spinner) findViewById(R.id.spTampil) ;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getData();
                } else if (position == 1) {
                    getFilteredData(position);
                } else if (position == 2) {
                    getFilteredData(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setToolbar() {
        Toolbar appbar = findViewById(R.id.customToolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.title_transaction);

        ImageView ivBack = findViewById(R.id.btnBack);
        ivBack.setOnClickListener(v -> finish());
    }

    private void getData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeli>> call = service.getBeli();
        call.enqueue(new Callback<List<ModelBeli>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeli>> call, @NonNull Response<List<ModelBeli>> response) {
                Toast.makeText(TransactionActivity.this, "Loaded", Toast.LENGTH_SHORT).show();

                TransactionAdapter adapter = new TransactionAdapter(TransactionActivity.this, response.body());
                recTransaction.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelBeli>> call, @NonNull Throwable t) {
                Toast.makeText(TransactionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilteredData(int position) {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeli>> call = service.filterBeli(position);
        call.enqueue(new Callback<List<ModelBeli>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeli>> call, @NonNull Response<List<ModelBeli>> response) {
                TransactionAdapter adapter = new TransactionAdapter(TransactionActivity.this, response.body());
                recTransaction.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelBeli>> call, @NonNull Throwable t) {
                Toast.makeText(TransactionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //start date time picker
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if(id == 2){
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            btnDari.setText(Helper.setDatePickerNormal(thn,bln+1,day));
            dari = Helper.setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            btnKe.setText(Helper.setDatePickerNormal(thn,bln+1,day));
            ke = Helper.setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };
    //end date time picker

    private void filtertgl() {
        if (spinner.getSelectedItemPosition() == 0) {
            getData();
        } else if (spinner.getSelectedItemPosition() == 1) {
            getFilteredData(spinner.getSelectedItemPosition());
        } else if (spinner.getSelectedItemPosition() == 2) {
            getFilteredData(spinner.getSelectedItemPosition());
        }
    }

    public void transactionDetail(View view) {
        Intent intent = new Intent(TransactionActivity.this, DetailTransactionActivity.class);
        startActivity(intent);
    }
}