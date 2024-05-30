package com.alcorp.manata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivityPelanggan extends AppCompatActivity {

    Toolbar appbar;
    RecyclerView listpelanggan;
    AdapterListPelanggan adapter;
    List<getterPelanggan> daftarPelanggan;
    Database db;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan);
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

        ConstraintLayout wInfo = (ConstraintLayout) findViewById(R.id.wInfo);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.ripple_effect);

        String pelanggan = getIntent().getStringExtra("pelanggan");
        if (pelanggan.equals("master")){
            tvTb.setText("Pelanggan");
            wInfo.setVisibility(View.GONE);
        } else {
            tvTb.setText("Laporan Pelanggan");
            constraintLayout.setVisibility(View.GONE);
        }

        db = new Database(this);
        v = this.findViewById(android.R.id.content);

        final EditText eCari = (EditText) findViewById(R.id.eCari) ;
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String a = eCari.getText().toString() ;
                getPelanggan(a);
            }
        });
    }

    public void getPelanggan(String keyword){
        daftarPelanggan = new ArrayList<>();
        listpelanggan = (RecyclerView) findViewById(R.id.listpelanggan);
        listpelanggan.setHasFixedSize(true);
        listpelanggan.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterListPelanggan(this,daftarPelanggan);
        listpelanggan.setAdapter(adapter);

        String q;

        if (TextUtils.isEmpty(keyword)){
            q = "SELECT * FROM tblpelanggan";
        }else {
            q = "SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+keyword+"%' ORDER BY pelanggan";
        }
        Cursor cur = db.sq(q);
        if (cur.getCount() > 0){
            ActivityTampil.setText(v,R.id.tValue2,"Jumlah Data : "+String.valueOf(cur.getCount())) ;
            while(cur.moveToNext()){
                daftarPelanggan.add(new getterPelanggan(
                        cur.getInt(cur.getColumnIndex("idpelanggan")),
                        cur.getString(cur.getColumnIndex("pelanggan")),
                        cur.getString(cur.getColumnIndex("alamat")),
                        cur.getString(cur.getColumnIndex("telp"))
                ));
            }
        } else {
            ActivityTampil.setText(v,R.id.tValue2, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void tambah(View view){
        Intent intent = new Intent(this, ActivityPelangganTambah.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPelanggan("");
    }

    class AdapterListPelanggan extends RecyclerView.Adapter<AdapterListPelanggan.PelangganViewHolder>{
        private Context ctxAdapter;
        private List<getterPelanggan> data;

        public AdapterListPelanggan(Context ctx, List<getterPelanggan> viewData) {
            this.ctxAdapter = ctx;
            this.data = viewData;
        }

        @NonNull
        @Override
        public PelangganViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
            View view = inflater.inflate(R.layout.list_item_pelanggan,viewGroup,false);
            return new PelangganViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final PelangganViewHolder holder, final int i) {
            final getterPelanggan getter = data.get(i);
            holder.pelanggan.setText("Nama : "+getter.getPelanggan());
            holder.alamat.setText("Alamat : "+getter.getAlamat());
            holder.notelp.setText("No. Telepon : "+String.valueOf(getter.getNoTelp()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = "Nama : "+getter.getPelanggan() +"\n"+
                            "Alamat : "+getter.getAlamat() +"\n"+
                            "No. Telepon : "+getter.getNoTelp();

                    ClipData clipData = ClipData.newPlainText("text", text);
                    ClipboardManager clipboardManager = (ClipboardManager) ctxAdapter.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(ctxAdapter, "Berhasil Copy", Toast.LENGTH_SHORT).show();
                }
            });

            holder.opt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(ctxAdapter,holder.opt);
                    popupMenu.inflate(R.menu.option_item);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_update:
                                    Intent intent = new Intent(ctxAdapter, ActivityPelangganTambah.class);
                                    intent.putExtra("idpelanggan",getter.getIdPelanggan());
                                    intent.putExtra("pelanggan",getter.getPelanggan());
                                    intent.putExtra("alamat",getter.getAlamat());
                                    intent.putExtra("telp",String.valueOf(getter.getNoTelp()));
                                    ctxAdapter.startActivity(intent);
                                    break;

                                case R.id.menu_delete:
                                    final Database db = new Database(ctxAdapter);
                                    String q = "SELECT * FROM qorder WHERE idpelanggan = '"+getter.getIdPelanggan()+"' ";
                                    Cursor c = db.sq(q);
                                    if (c.getCount() > 0){
                                        Toast.makeText(ctxAdapter, "Pelanggan Ini Digunakan Didalam Menu Penjualan, Tidak Dapat Dihapus", Toast.LENGTH_SHORT).show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (db.deletePelanggan(getter.getIdPelanggan())){
                                                    data.remove(i);
                                                    notifyDataSetChanged();
                                                    Toast.makeText(ctxAdapter, "Berhasil Menghapus Data", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Toast.makeText(ctxAdapter, "Gagal Menghapus data", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        builder.setMessage("Anda Yakin Ingin Menghapus Data Ini");
                                        builder.show();
                                    }
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class PelangganViewHolder extends RecyclerView.ViewHolder{
            TextView pelanggan, alamat, notelp, opt;
            public PelangganViewHolder(@NonNull View itemView) {
                super(itemView);
                pelanggan=(TextView)itemView.findViewById(R.id.tvPelanggan);
                alamat=(TextView)itemView.findViewById(R.id.tvAlamat);
                notelp=(TextView)itemView.findViewById(R.id.tvTelp);
                opt=(TextView)itemView.findViewById(R.id.tvOpt);
            }
        }
    }

    static class getterPelanggan {
        private int idPelanggan;
        private String pelanggan;
        private String alamat;
        private String notelp;

        public getterPelanggan(int idPelanggan, String pelanggan, String alamat, String notelp) {
            this.idPelanggan = idPelanggan;
            this.pelanggan = pelanggan;
            this.alamat = alamat;
            this.notelp = notelp;
        }

        public int getIdPelanggan() {
            return idPelanggan;
        }

        public String getPelanggan() {
            return pelanggan;
        }

        public String getAlamat() {
            return alamat;
        }

        public String getNoTelp() {
            return notelp;
        }
    }
}
