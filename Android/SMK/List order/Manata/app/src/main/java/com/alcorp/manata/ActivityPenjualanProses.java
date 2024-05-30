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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityPenjualanProses extends AppCompatActivity {

    Toolbar appbar;
    View v;
    Database db;
    double pay, cashback;
    String faktur, jumlah, sMetode, sBayar;
    Spinner spMetode;
    TextInputLayout tKembali, tTglJatuh;
    TextInputEditText etTglJatuh;
    ImageView ivTglJatuh;
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_proses);
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
        tvTb.setText("Bayar");

        db=new Database(this);
        v = this.findViewById(android.R.id.content);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        pay = 0;

        faktur = getIntent().getStringExtra("faktur");
        ActivityTampil.setText(v, R.id.faktur, faktur);

        spMetode = (Spinner) findViewById(R.id.spMetode);

        tKembali = (TextInputLayout) findViewById(R.id.input4);

        tTglJatuh = (TextInputLayout) findViewById(R.id.textInputLayout2);
        etTglJatuh = (TextInputEditText) findViewById(R.id.tglJatuhTempo);

        ivTglJatuh = (ImageView) findViewById(R.id.ivTglJatuh);

        sBayar = getIntent().getStringExtra("bayar");

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            etTglJatuh.setText(extra.getString("tanggal"));
        }

            spMetode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0 && sBayar.equals("penjualan")) {
                    tKembali.setHint("Kekurangan");
                    tTglJatuh.setHint("Tanggal Jatuh Tempo");
                } else if (pos == 0 && sBayar.equals("hutang")){
                    tKembali.setHint("Kekurangan");
                    tTglJatuh.setHint("Tanggal Jatuh Tempo");
                } else if (pos == 1){
                    tKembali.setHint("Kembali");
                    tTglJatuh.setHint("Tanggal Selesai");
                } else if (pos == 2){
                    tKembali.setHint("Kembali");
                    tTglJatuh.setHint("Tanggal Selesai");
                }
                sMetode = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (sBayar.equals("hutang")){
            ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.metod_hutang, android.R.layout.simple_spinner_item);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMetode.setAdapter(dataAdapter);

            tKembali.setHint("kekurangan");

            TextInputLayout tDiskon = (TextInputLayout) findViewById(R.id.input5);
            tDiskon.setVisibility(View.GONE);
        }

        ivTglJatuh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        final TextInputEditText diskon = (TextInputEditText) findViewById(R.id.diskonorder);
        diskon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(diskon.getText().toString().equals("")){
                    diskon.setText("0");
                } else{
                    calculate();
                }
            }
        });

        final TextInputEditText jumlahbayar = (TextInputEditText) findViewById(R.id.jumlahbayar);
        jumlahbayar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(jumlahbayar.getText().toString().equals("")){
                    jumlahbayar.setText("0");
                } else{
                    calculate();
                }
            }
        });
        setText();
        calculate();
    }

    public void calculate() {
        double diskon = Double.parseDouble(ActivityCetak.getText(v, R.id.diskonorder));
        double masuk = Double.parseDouble(ActivityCetak.getText(v, R.id.jumlahbayar));
        double jum = Double.parseDouble(jumlah);
        double kembali;
        if (jum > diskon){
            kembali = jum - diskon;
            ActivityTampil.setText(v, R.id.totalbayar, ActivityPenjualan.removeE(kembali));

            if (masuk > jum) {
                kembali = masuk - jum - diskon;
                cashback = kembali;
                ActivityTampil.setText(v, R.id.kembali, ActivityPenjualan.removeE(kembali));
            } else if (masuk == jum) {
                kembali = 0;
                cashback = kembali;
                ActivityTampil.setText(v, R.id.kembali, ActivityPenjualan.removeE(kembali));
            } else {
                String min;
                kembali = jum - masuk - diskon;
                if (kembali == 0){
                    min = "";
                } else {
                    min = "-";
                }
                cashback = kembali;
                ActivityTampil.setText(v, R.id.kembali, min + ActivityPenjualan.removeE(kembali));
            }

        } else if (jum < diskon){
            Toast.makeText(ActivityPenjualanProses.this, "Diskon Tidak Boleh Lebih Dari Total Bayar", Toast.LENGTH_SHORT).show();
            ActivityTampil.setText(v, R.id.diskonorder, "0");
        }
    }

    public void setText() {
        double total=0.0;
        Cursor cur = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + faktur.substring(3));
        double sum=0.0;
        if (cur.moveToFirst()){
            sum = cur.getDouble(0);
        }
        total=total+sum;

        Cursor c = db.sq("SELECT * FROM tblorder WHERE faktur = '"+faktur+"' ");
        if (c.getCount() > 0 && sBayar.equals("penjualan")) {
            c.moveToNext();
            pay = Double.parseDouble(ActivityTampil.getString(c, "total"));
//            jumlah = ActivityTampil.getString(c, "total");
            jumlah= String.valueOf(total);
            ActivityTampil.setText(v, R.id.totalbayar, ActivityPenjualan.removeE(jumlah));
        } else if(c.getCount() > 0 && sBayar.equals("hutang")) {
            c.moveToNext();
            pay = Double.parseDouble(ActivityTampil.getString(c, "kurang"));
            jumlah = ActivityTampil.getString(c, "kurang");
            ActivityTampil.setText(v, R.id.totalbayar, ActivityPenjualan.removeE(jumlah));
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
            ActivityTampil.setText(v, R.id.tglJatuhTempo, ActivityPenjualan.setDatePickerNormal(thn,bln+1,day)) ;
        }
    };

    public void bayar(View view) {
        double total=0.0;
        Cursor cursor = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + faktur.substring(3));
        double sum=0.0;
        if (cursor.moveToFirst()){
            sum = cursor.getDouble(0);
        }
        total=total+sum;

        List<Integer> idtrans = new ArrayList<Integer>();
        String e="SELECT idtransaksi FROM tbltransaksi";
        String fa = "00000000";
        Cursor cu = db.sq(e);
        if (cu.moveToNext()){
            do {
                idtrans.add(cu.getInt(0));
            }while (cu.moveToNext());
        }
        String tempFaktur="", tempfa="";
        int IdFaktur=0;
        if (cu.getCount()==0){
            tempFaktur=faktur.substring(0,faktur.length()-1)+"1";
            tempfa=fa.substring(0,fa.length()-1)+"1";
        }else {
            IdFaktur = idtrans.get(cu.getCount()-1)+1;
            tempFaktur = faktur.substring(0,faktur.length()-String.valueOf(IdFaktur).length())+String.valueOf(IdFaktur);
            tempfa = fa.substring(0,fa.length()-String.valueOf(IdFaktur).length())+String.valueOf(IdFaktur);
        }

        String sald;

        Cursor c = db.sq("SELECT * FROM tblorder");
        c.moveToLast();
        String hmasuk = ActivityTampil.getString(c, "total");
        String kekurangan = ActivityTampil.getString(c, "kurang");

        Cursor cur = db.sq("SELECT * FROM tbltransaksi");
        if (cur.getCount()>0){
            cur.moveToLast();
            sald = ActivityTampil.getString(cur, "saldo");
        } else {
            sald = "0";
        }

        String lunas, stat = "0", keterangan = "";
        if(sMetode.equals("Tunai DP")) {
            lunas = "1";
            keterangan = "Penjualan DP";
        } else if (sMetode.equals("Cicil")){
            lunas = "1";
            keterangan = "Cicilan";
        } else {
            lunas = "2";
            keterangan = "Penjualan";
        }

        double masuk = Double.parseDouble(ActivityCetak.getText(v, R.id.jumlahbayar));
        double jum = Double.parseDouble(jumlah);
        double saldo = total + Double.parseDouble(sald) - Double.parseDouble(ActivityCetak.getText(v, R.id.diskonorder));
        double saldoDP = masuk + Double.parseDouble(sald);

        double saldoLun = Double.parseDouble(kekurangan) + Double.parseDouble(sald);

        String kembali = "", kurang = "";
        if (masuk >= jum) {
            kurang = "0";
            kembali = String.valueOf(cashback).substring(0, String.valueOf(cashback).length()-2);
        } else {
            kurang = String.valueOf(cashback).substring(0, String.valueOf(cashback).length()-2);
            kembali = "0";
        }

        String penjualan = "UPDATE tblorder SET tglselesai='"+convertDate(ActivityCetak.getText(v, R.id.tglJatuhTempo))+"', diskonorder='"+ActivityCetak.getText(v, R.id.diskonorder)+"', bayar='"+ActivityCetak.getText(v, R.id.jumlahbayar)+"', kembali='"+kembali+"', kurang='"+kurang+"',metode='"+sMetode+"', lunas='"+lunas+"' WHERE faktur='"+faktur+"' ";
        String cicil = "UPDATE tblorder SET tglselesai='"+convertDate(ActivityCetak.getText(v, R.id.tglJatuhTempo))+"', bayar=bayar+'"+ActivityCetak.getText(v, R.id.jumlahbayar)+"', kembali='"+kembali+"', kurang='"+kurang+"',metode='"+sMetode+"', lunas='"+lunas+"' WHERE faktur='"+faktur+"' ";
        String hutang = "UPDATE tblorder SET tglselesai='"+convertDate(ActivityCetak.getText(v, R.id.tglJatuhTempo))+"', bayar=bayar+'"+ActivityCetak.getText(v, R.id.jumlahbayar)+"',kembali=('"+ActivityCetak.getText(v, R.id.jumlahbayar)+"')-kurang, kurang='"+kurang+"', metode='"+sMetode+"', lunas='"+lunas+"' WHERE faktur='"+faktur+"' ";

        String date_now = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        if (sMetode.equals("Tunai DP")){
            if (masuk > jum) {
                Toast.makeText(this, "Uang Pembayaran Lebih", Toast.LENGTH_SHORT).show();
            } else if (ActivityCetak.getText(v, R.id.jumlahbayar).equals("0")) {
                Toast.makeText(this, "Uang Pembayaran Tidak Boleh 0", Toast.LENGTH_SHORT).show();
            } else if (ActivityCetak.getText(v, R.id.tglJatuhTempo).equals("")) {
                Toast.makeText(this, "Tanggal Jatuh Tempo Wajib di isi", Toast.LENGTH_SHORT).show();
            } else {
                if (db.exc(penjualan) && db.insertTransMasuk(convertDate(date_now), tempFaktur, tempfa, keterangan, ActivityCetak.getText(v, R.id.jumlahbayar), String.valueOf(saldoDP), stat)) {
                    open();
                } else {
                    Toast.makeText(this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (sMetode.equals("Tunai Lunas") && sBayar.equals("penjualan")) {
            if (masuk < jum) {
                Toast.makeText(this, "Uang Pembayaran Kurang", Toast.LENGTH_SHORT).show();
            } else if (ActivityCetak.getText(v, R.id.tglJatuhTempo).equals("")) {
                Toast.makeText(this, "Tanggal Selesai Wajib di isi", Toast.LENGTH_SHORT).show();
            } else {
                if (db.exc(penjualan) && db.insertTransMasuk(convertDate(date_now), tempFaktur, tempfa, keterangan, hmasuk, String.valueOf(saldo), stat)) {
                    open();
                } else {
                    Toast.makeText(this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (sMetode.equals("Tunai Lunas") && sBayar.equals("hutang")) {
            if (masuk < jum) {
                Toast.makeText(this, "Uang Pembayaran Kurang", Toast.LENGTH_SHORT).show();
            } else if (ActivityCetak.getText(v, R.id.tglJatuhTempo).equals("")) {
                Toast.makeText(this, "Tanggal Selesai Wajib di isi", Toast.LENGTH_SHORT).show();
            } else {
                if (db.exc(hutang) && db.insertTransMasuk(convertDate(date_now), tempFaktur, tempfa, "Pelunasan", kekurangan, String.valueOf(saldoLun), stat)) {
                    open();
                } else {
                    Toast.makeText(this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (sMetode.equals("Bank Tranfer") && sBayar.equals("penjualan")){
            if (masuk < jum) {
                Toast.makeText(this, "Uang Pembayaran Kurang", Toast.LENGTH_SHORT).show();
            } else if (ActivityCetak.getText(v, R.id.tglJatuhTempo).equals("")) {
                Toast.makeText(this, "Tanggal Selesai Wajib di isi", Toast.LENGTH_SHORT).show();
            } else {
                if (db.exc(penjualan) && db.insertTransMasuk(convertDate(date_now), tempFaktur, tempfa, keterangan, hmasuk, String.valueOf(saldo), String.valueOf(stat))) {
                    open();
                } else {
                    Toast.makeText(this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (sMetode.equals("Bank Tranfer") && sBayar.equals("hutang")) {
            if (masuk < jum) {
                Toast.makeText(this, "Uang Pembayaran Kurang", Toast.LENGTH_SHORT).show();
            } else if (ActivityCetak.getText(v, R.id.tglJatuhTempo).equals("")) {
                Toast.makeText(this, "Tanggal Selesai Wajib di isi", Toast.LENGTH_SHORT).show();
            } else {
                if (db.exc(hutang) && db.insertTransMasuk(convertDate(date_now), tempFaktur, tempfa, "Pelunasan", kekurangan, String.valueOf(saldoLun), stat)) {
                    open();
                } else {
                    Toast.makeText(this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (sMetode.equals("Cicil") && sBayar.equals("hutang")) {
            if (masuk > jum) {
                Toast.makeText(this, "Uang Pembayaran Lebih", Toast.LENGTH_SHORT).show();
            } else if (ActivityCetak.getText(v, R.id.jumlahbayar).equals("0")) {
                Toast.makeText(this, "Uang Pembayaran Tidak Boleh 0", Toast.LENGTH_SHORT).show();
            } else if (ActivityCetak.getText(v, R.id.tglJatuhTempo).equals("")) {
                Toast.makeText(this, "Tanggal Selesai Wajib di isi", Toast.LENGTH_SHORT).show();
            } else {
                if (db.exc(cicil) && db.insertTransMasuk(convertDate(date_now), tempFaktur, tempfa, keterangan, ActivityCetak.getText(v, R.id.jumlahbayar), String.valueOf(saldoDP), stat)) {
                    open();
                } else {
                    Toast.makeText(this, "Pembayaran Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public void open() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda Ingin Mencetak Struk?");
        builder.setPositiveButton("Cetak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //yes
                        Intent i = new Intent(ActivityPenjualanProses.this, ActivityCetak.class);
                        i.putExtra("faktur", faktur);
                        i.putExtra("type", "bayar");
                        startActivity(i);
                    }
                });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(ActivityPenjualanProses.this, ActivityTransaksi.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        builder.show();
    }

    public String convertDate(String date){
        try {
            String[] a = date.split("/") ;
            return a[2]+a[1]+a[0];
        } catch (Exception e){
            return "";
        }
    }
}
