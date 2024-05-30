package com.alcorp.manata;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityTambahPemasukan extends AppCompatActivity {

    Button btnSimpan;
    ImageButton btntgltransaksi;
    TextInputEditText edtTglTransaksi,edtKeterangan;
    View v;
    Database db;
    Toolbar appbar;

    int year, month, day ;
    Calendar calendar ;

    String faktur="00000000",saldo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pemasukan);
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
        tvTb.setText("Pemasukan");

        db=new Database(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        cek();
        getTotal();

        String date_n=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ActivityTampil.setText(v, R.id.edtTglTransaksi,date_n);
        btntgltransaksi = (ImageButton)findViewById(R.id.ibtnTglTransaksi);
        btntgltransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void cek(){
        Cursor c = db.sq("SELECT * FROM tbltransaksi WHERE keterangantransaksi = 0");
        if (c.getCount()>0){
            c.moveToLast();
            String faktur = ActivityTampil.getString(c, "fakturtransaksi");
            ActivityTampil.setText(v, R.id.edtNomorFaktur, faktur);
        } else {
            getFaktur();
        }
    }

    private void getFaktur(){
        List<Integer> idtransaksi = new ArrayList<Integer>();
        String q="SELECT idtransaksi FROM tbltransaksi";
        Cursor c = db.sq(q);
        if (c.moveToNext()){
            do {
                idtransaksi.add(c.getInt(0));
            }while (c.moveToNext());
        }
        String tempFaktur="";
        int IdFaktur=0;
        if (c.getCount()==0){
            tempFaktur=faktur.substring(0,faktur.length()-1)+"1";
        }else {
            IdFaktur = idtransaksi.get(c.getCount()-1)+1;
            tempFaktur = faktur.substring(0,faktur.length()-String.valueOf(IdFaktur).length())+String.valueOf(IdFaktur);
        }
        ActivityTampil.setText(v,R.id.edtNomorFakturTransaksi,tempFaktur);
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
            ActivityTampil.setText(v, R.id.edtTglTransaksi, ActivityPenjualan.setDatePickerNormal(thn,bln+1,day)) ;
        }
    };

    private void keluar(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage("Anda yakin ingin keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ActivityTambahPemasukan.this, ActivityTransaksi.class) ;
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
                startActivity(i);
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();

    }

    @Override
    public void onBackPressed() {
        keluar();
    }

    public String convertDate(String date){
        String[] a = date.split("/") ;
        return a[2]+a[1]+a[0];
    }

    public void simpan(View view){
        String sald;
        String eFaktur = ActivityCetak.getText(v, R.id.edtNomorFakturTransaksi);
        String eTglT = ActivityCetak.getText(v, R.id.edtTglTransaksi);
        String eHMasuk = ActivityCetak.getText(v, R.id.edtHargaMasuk);
        String eKet = ActivityCetak.getText(v, R.id.edtKeteranganTransaksi);

        if (TextUtils.isEmpty(eFaktur) || TextUtils.isEmpty(eHMasuk) || Double.parseDouble(eHMasuk)==0 || TextUtils.isEmpty(eKet) || eFaktur.equals("0")){
            Toast.makeText(this, "Masukan Data Dengan Benar", Toast.LENGTH_SHORT).show();
        } else {
            Cursor cursor = db.sq("SELECT * FROM tbltransaksi");
            if (cursor.getCount()>0){
                cursor.moveToLast();
                sald = ActivityTampil.getString(cursor, "saldo");
            } else {
                sald = "0";
            }
            double saldo = Double.parseDouble(eHMasuk) + Double.parseDouble(sald);
            String notransaksi = String.valueOf(eFaktur);
            String stat = "0";

            if (db.insertTransMasuk(convertDate(eTglT), notransaksi, eFaktur, eKet, eHMasuk, String.valueOf(saldo), stat)){
                Toast.makeText(this, "Sukses", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getTotal(){
        double sald = 0.0;
        Cursor c = db.sq("SELECT * FROM tbltransaksi");
        if (c.getCount()==0){
            saldo= String.valueOf(0);
            sald = Double.parseDouble(saldo);
        } else {
            c.moveToLast();
            saldo=ActivityTampil.getString(c, "saldo");
            sald = Double.parseDouble(saldo);
        }
        ActivityTampil.setText(v, R.id.tvSaldo, "Rp. "+ActivityPenjualan.removeE(sald));
    }
}
