package com.alcorp.manata;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityBackup extends AppCompatActivity {

    Toolbar appbar;
    View v;
    Config config;
    String dirOut, dirIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
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
        tvTb.setText("Backup");

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ActivityMain.WRITE_EXST);

        v = this.findViewById(android.R.id.content);
        config = new Config(getSharedPreferences("config", 0));

        this.dirIn = "/data/data/com.alcorp.manata/databases/";
        this.dirOut = Environment.getExternalStorageDirectory().toString() + "/Download/";
        setText();
        File file = new File(this.dirOut);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ActivityBackup.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityBackup.this, permission)) {
                ActivityCompat.requestPermissions(ActivityBackup.this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(ActivityBackup.this, new String[]{permission}, requestCode);
            }
        }
    }

    public void backup(View v) {
        String dbName = Database.nama_database;
        String dbOut = dbName + getDate("HH-mm dd-MM-yyyy");
        if (!copyFile(this.dirIn, this.dirOut, dbName).booleanValue()) {
            Toast.makeText(this, "Backup Data Gagal", Toast.LENGTH_SHORT).show();
        } else if (renameFile(this.dirOut, dbName, dbOut).booleanValue()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage((CharSequence) "Backup Data tersimpan di Folder Download");
            alert.setPositiveButton((CharSequence) "ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        } else {
            Toast.makeText(this, "Backup Data Gagal1", Toast.LENGTH_SHORT).show();
        }

    }

    public void setText() {
        ActivityTampil.setText(this.v, R.id.ePath, "Internal Storage/Download/");

    }

    public static String getDate(String type){ //Random time type : HHmmssddMMyy
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(type);
        String formattedDate = df.format(c.getTime());
        return formattedDate ;
    }

    public static Boolean copyFile(String pIn, String pOut, String name){
        try{
            File file = new File(pOut);
            if (!file.exists()) {
                file.mkdirs();
            }
            InputStream in = new FileInputStream(pIn + name);
            OutputStream out = new FileOutputStream(pOut + name);
            byte[] buffer = new byte[1024];
            while (true) {
                int read = in.read(buffer);
                if (read != -1) {
                    out.write(buffer, 0, read);
                } else {
                    in.close();
                    out.flush();
                    out.close();
                    return Boolean.valueOf(true);
                }
            }
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }
    }

    public static Boolean renameFile(String path, String namaLama, String namaBaru){
        try {
            new File(path + namaLama).renameTo(new File(path + namaBaru));
            return Boolean.valueOf(true);
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }
    }
}
