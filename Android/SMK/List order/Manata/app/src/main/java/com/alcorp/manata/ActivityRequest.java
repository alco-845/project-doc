package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ActivityRequest extends AppCompatActivity {

    Toolbar appbar;
    Database db ;
    ArrayList arrayList = new ArrayList();
    TextView tTotal, tvTb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
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

        tvTb = (TextView) findViewById(R.id.tvTb);
        tvTb.setText("Request Order");

        db = new Database(this) ;

        tTotal = (TextView) findViewById(R.id.tTotal) ;

        loadList();
    }

    public void loadList(){
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterRequest(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq("SELECT * FROM tblrequest ORDER BY idrequest DESC") ;
        if(c.getCount() > 0){
            while(c.moveToNext()){
                String idrequest = ActivityTampil.getString(c,"idrequest") ;
                String nama = ActivityTampil.getString(c,"nama") ;
                String request = ActivityTampil.getString(c,"request") ;

                String campur = idrequest+"__"+nama+"__"+request;
                arrayList.add(campur);
            }
            tTotal.setText("Jumlah Data : "+String.valueOf(c.getCount()));
        } else {
            tTotal.setText("Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    public void tambah(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_request);

        final TextInputEditText etNama = (TextInputEditText) dialog.findViewById(R.id.etNama);
        final TextInputEditText etReq = (TextInputEditText) dialog.findViewById(R.id.etReq);

        Button tambah = (Button) dialog.findViewById(R.id.bTambah);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = etNama.getText().toString();
                String req = etReq.getText().toString();

                if (nama.equals("") || req.equals("")) {
                    Toast.makeText(ActivityRequest.this, "Isi Data Dengan Benar", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.insertReq(nama, req)) {
                        Toast.makeText(ActivityRequest.this, "Tambah Data Berhasil", Toast.LENGTH_SHORT).show();
                        loadList();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ActivityRequest.this, "Tambah Data Gagal", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        dialog.show();
    }
}

class AdapterRequest extends RecyclerView.Adapter<AdapterRequest.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterRequest(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_pelanggan, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, request, telp, opt;
        ConstraintLayout click;

        public ViewHolder(View view) {
            super(view);

            nama = (TextView) view.findViewById(R.id.tvPelanggan);
            request = (TextView) view.findViewById(R.id.tvAlamat);
            telp = (TextView) view.findViewById(R.id.tvTelp);
            opt = (TextView) view.findViewById(R.id.tvOpt);
            click = (ConstraintLayout) view.findViewById(R.id.cItem);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final String[] row = data.get(i).split("__");

        viewHolder.request.setVisibility(View.GONE);
        viewHolder.opt.setVisibility(View.GONE);
        viewHolder.nama.setText("Nama : "+row[1]);
        viewHolder.telp.setText("Jenis Order : "+row[2]);

        viewHolder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database db = new Database(c);
                        if (db.deleteReq(Integer.valueOf(row[0]))){
                            data.remove(i);
                            notifyDataSetChanged();
                            ((ActivityRequest)c).loadList();
                            Toast.makeText(c, "Berhasil Menghapus Data", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(c, "Gagal Menghapus Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setMessage("Anda Yakin Ingin Menghapus Data Ini?");
                builder.show();
            }
        });
    }
}