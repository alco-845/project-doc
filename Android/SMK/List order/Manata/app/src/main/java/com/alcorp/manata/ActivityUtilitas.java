package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ActivityUtilitas extends AppCompatActivity {

    Toolbar appbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilitas);

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
        tvTb.setText("Utilitas");
    }

    public void backup(View view) {
        Intent intent = new Intent(this, ActivityBackup.class);
        startActivity(intent);
    }

    public void restore(View view) {
        Intent intent = new Intent(this, ActivityRestore.class);
        startActivity(intent);
    }

    public void reset(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda Yakin Ingin Mereset Data Aplikasi Ini? ");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        reset1();
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void reset1(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Reset Akan Menghapus Semua Data Dalam Aplikasi Ini");
        alertDialogBuilder.setPositiveButton("Reset",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        new File("data/data/com.alcorp.manata/databases/"+"db_data").delete() ;
                        Database db = new Database(ActivityUtilitas.this) ;
                        db.cektbl() ;
                        Intent i=new Intent(ActivityUtilitas.this,ActivitySplash.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(ActivityUtilitas.this, "Reset Data Berhasil, Aplikasi Terestart", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Batal",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
