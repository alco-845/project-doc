package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class ActivityPenjualanUpdate extends AppCompatActivity {

    Toolbar appbar;
    Database db;
    Calendar calendar;
    TextInputEditText edtTgl;
    ImageButton btnTgl;
    int year, month, day;
    String sStatus="";
    Spinner spStatus;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_update);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        appbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(appbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView iv1 = (ImageView) findViewById(R.id.ivTb1);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView tvTb = (TextView) findViewById(R.id.tvTb);
        tvTb.setText("Update Penjualan");

        db = new Database(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Bundle extra = getIntent().getExtras();
        ActivityTampil.setText(v, R.id.edtNomorFakture, extra.getString("faktur"));
        ActivityTampil.setText(v, R.id.tTgl, extra.getString("tglselesai"));

        ImageButton btnTglOrder = (ImageButton)findViewById(R.id.btnTgl);
        btnTglOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        spStatus = (Spinner) findViewById(R.id.spStatus) ;
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.Ustatus, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(dataAdapter);
        spStatus.setSelection(dataAdapter.getPosition(extra.getString("status")));
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void simpan(View view) {
        String stat = "";

        if (sStatus.equals("Terkirim")){
            stat = "2";
        } else {
            stat = "1";
        }

        if (ActivityCetak.getText(v, R.id.tTgl).equals("")){
            Toast.makeText(this, "Isi Data Dengan Benar", Toast.LENGTH_SHORT).show();
        } else {
            if (db.updateOrder2(Integer.valueOf(ActivityCetak.getText(v, R.id.edtNomorFakture).substring(3)), convertDate(ActivityCetak.getText(v, R.id.tTgl)), stat, sStatus)) {
                Toast.makeText(this, "Update Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, dTerima, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ActivityTampil.setText(v, R.id.tTgl, ActivityPenjualan.setDatePickerNormal(thn,bln+1,day)) ;
        }
    };

    public String convertDate(String date){
        try {
            String[] a = date.split("/") ;
            return a[2]+a[1]+a[0];
        } catch (Exception e){
            return "";
        }
    }
}
