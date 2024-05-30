package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityMain extends AppCompatActivity {

    static Integer WRITE_EXST = 0x3 ;
    Database db;
    Toolbar appbar;
    TextView title1, title2, title3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(appbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        db = new Database(this);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);

        ImageView iv1 = (ImageView) findViewById(R.id.ivTb1);
        iv1.setVisibility(View.GONE);

        title1 = (TextView) findViewById(R.id.textView2);
        title2 = (TextView) findViewById(R.id.textView6);
        title3 = (TextView) findViewById(R.id.textView);

        setTitle();
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ActivityMain.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this, permission)) {
                ActivityCompat.requestPermissions(ActivityMain.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(ActivityMain.this, new String[]{permission}, requestCode);
            }
        }
    }


    public void master(View view) {
        Intent intent = new Intent(this, ActivityMaster.class);
        startActivity(intent);
    }

    public void transaksi(View view) {
        Intent intent = new Intent(this, ActivityTransaksi.class);
        startActivity(intent);
    }

    public void laporan(View view) {
        Intent intent = new Intent(this, ActivityLaporan.class);
        startActivity(intent);
    }

    public void utilitas(View view) {
        Intent intent = new Intent(this, ActivityUtilitas.class);
        startActivity(intent);
    }

    public void setTitle(){
        Cursor c = db.sq("SELECT * FROM qorder WHERE status = 'Konfirmasi' AND bayar>0 ");
        if(c.getCount() > 0){
            title1.setText("Data Status Konfirmasi : " + c.getCount());
        } else {
            title1.setText("Data Status Konfirmasi : 0");
        }

        Cursor cursor = db.sq("SELECT * FROM qorder WHERE status = 'Selesai'");
        if(cursor.getCount() > 0){
            title2.setText("Barang Perlu dikirim : " + cursor.getCount());
        } else {
            title2.setText("Barang Perlu dikirim : 0");
        }

        Cursor cur = db.sq("SELECT * FROM tblrequest");
        if(cur.getCount() > 0){
            title3.setText("Data Request : " + cur.getCount());
        } else {
            title3.setText("Data Request : 0");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle();
    }

    private void keluar(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.create();
        alert.setMessage("Apakah Anda Yakin Ingin Keluar?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }

    @Override
    public void onBackPressed() {
        keluar();
    }
}
