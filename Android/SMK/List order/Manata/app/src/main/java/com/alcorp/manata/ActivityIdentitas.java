package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.alcorp.manata.ActivityTampil;
import com.alcorp.manata.Database;
import com.alcorp.manata.R;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityIdentitas extends AppCompatActivity {

    Toolbar appbar;
    Database db;
    TextInputEditText edt1,edt2,edt3,edt4,edt5,edt6;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identitas);
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
        tvTb.setText("Identitas");

        db = new Database(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        edt1 = (TextInputEditText)findViewById(R.id.eNama);
        edt2 = (TextInputEditText)findViewById(R.id.eAlamat);
        edt3 = (TextInputEditText)findViewById(R.id.eTelp);
        edt4 = (TextInputEditText)findViewById(R.id.cap1);
        edt5 = (TextInputEditText)findViewById(R.id.cap2);
        edt6 = (TextInputEditText)findViewById(R.id.cap3);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText("");edt2.setText("");edt3.setText("");edt4.setText("");edt5.setText("");edt6.setText("");
//                edt1.getText().clear();edt2.getText().clear();edt3.getText().clear();edt4.getText().clear();edt5.getText().clear();edt6.getText().clear();
            }
        });

        setText();
    }

    public void setText(){
        Cursor c = db.sq("SELECT * FROM tblidentitas WHERE ididentitas = 1");
        if(c.getCount() == 1){
            c.moveToNext();
            edt1.setText(ActivityTampil.getString(c,"namatoko")) ;
            edt2.setText(ActivityTampil.getString(c,"alamattoko")) ;
            edt3.setText(ActivityTampil.getString(c,"telp")) ;
            edt4.setText(ActivityTampil.getString(c,"cap1")) ;
            edt5.setText(ActivityTampil.getString(c,"cap2")) ;
            edt6.setText(ActivityTampil.getString(c,"cap3")) ;
        }
    }

    public void simpan(View view){
        String nama = edt1.getText().toString();
        String alamat = edt2.getText().toString();
        String telepon = edt3.getText().toString();
        String cap1 = edt4.getText().toString();
        String cap2 = edt5.getText().toString();
        String cap3 = edt6.getText().toString();

        String q = "UPDATE tblidentitas SET namatoko='"+nama+"', alamattoko='"+alamat+"', telp='"+telepon+"', cap1='"+cap1+"', cap2='"+cap2+"', cap3='"+cap3+"' WHERE ididentitas=1";
        if (nama.equals("") || alamat.equals("") || telepon.equals("") || cap1.equals("") || cap2.equals("") || cap3.equals("")){
            Toast.makeText(this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
        } else {
            if(db.exc(q)){
                Toast.makeText(this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal disimpan", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
