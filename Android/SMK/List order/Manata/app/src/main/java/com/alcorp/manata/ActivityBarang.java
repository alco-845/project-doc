9package com.alcorp.manata;

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

public class ActivityBarang extends AppCompatActivity {

    Toolbar appbar;
    RecyclerView listbarang;
    AdapterListBarang adapter;
    List<getterBarang> daftarBarang;
    Database db;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);
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

        String barang = getIntent().getStringExtra("barang");
        if (barang.equals("master")){
            tvTb.setText("Barang / Jasa");
            wInfo.setVisibility(View.GONE);
        } else {
            tvTb.setText("Laporan Barang / Jasa");
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
                String cari = eCari.getText().toString() ;
                getBarang(cari);
            }
        });
    }

    private void getBarang(String keyword) {
        daftarBarang = new ArrayList<>();
        listbarang = (RecyclerView) findViewById(R.id.listbarang);
        listbarang.setHasFixedSize(true);
        listbarang.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterListBarang(this, daftarBarang);
        listbarang.setAdapter(adapter);

        String q;

        if (TextUtils.isEmpty(keyword)){
            q = "SELECT * FROM tblbarang";
        }else {
            q = "SELECT * FROM tblbarang WHERE barang LIKE '%"+keyword+"%' ORDER BY barang";
        }
        Cursor cur = db.sq(q);
        if (cur.getCount() > 0){
            ActivityTampil.setText(v,R.id.tValue2,"Jumlah Data : "+String.valueOf(cur.getCount())) ;
            while(cur.moveToNext()){
                daftarBarang.add(new getterBarang(
                        cur.getInt(cur.getColumnIndex("idbarang")),
                        cur.getString(cur.getColumnIndex("barang")),
                        cur.getString(cur.getColumnIndex("jenis")),
                        cur.getString(cur.getColumnIndex("warna")),
                        cur.getString(cur.getColumnIndex("variasi")),
                        cur.getString(cur.getColumnIndex("ukuran"))
                ));
            }
        } else {
            ActivityTampil.setText(v,R.id.tValue2, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void tambah(View view) {
        Intent intent = new Intent(this, ActivityBarangTambah.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBarang("");
    }

    class AdapterListBarang extends RecyclerView.Adapter<AdapterListBarang.BarangViewHolder>{
        private Context ctxAdapter;
        private List<getterBarang> data;

        public AdapterListBarang(Context ctxAdapter, List<getterBarang> data) {
            this.ctxAdapter = ctxAdapter;
            this.data = data;
        }

        @NonNull
        @Override
        public AdapterListBarang.BarangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctxAdapter).inflate(R.layout.list_item_barang, parent, false);
            return new BarangViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AdapterListBarang.BarangViewHolder holder, final int position) {
            final getterBarang getter = data.get(position);
            holder.kategori.setText("Jenis : " + getter.getJenis());
            holder.barang.setText(getter.getBarang());

            if (getter.getJenis().equals("Jasa")){
                holder.variasi.setVisibility(View.GONE);
                holder.barang.setPadding(0, 0, 0, 16);
            } else {
                holder.variasi.setText(getter.getWarna() +"\n" +
                        getter.getVariasi() +"\n" +
                        "Ukuran : " + getter.getUkuran());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = "Jenis : " + getter.getJenis() +"\n"+
                            getter.getBarang() +"\n"+
                            getter.getWarna() +"\n"+
                            getter.getVariasi() +"\n"+
                            "Ukuran : " + getter.getUkuran();

                    ClipData clipData = ClipData.newPlainText("text", text);
                    ClipboardManager clipboardManager = (ClipboardManager) ctxAdapter.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(ctxAdapter, "Berhasil Copy", Toast.LENGTH_SHORT).show();
                }
            });

            holder.opt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(ctxAdapter, holder.opt);
                    popupMenu.inflate(R.menu.option_item);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.menu_update:
                                    Config config = new Config(getSharedPreferences("config",ctxAdapter.MODE_PRIVATE));

                                    Intent intent = new Intent(ctxAdapter, ActivityBarangTambah.class);
                                    intent.putExtra("idbarang", getter.getIdbarang());
                                    intent.putExtra("barang", getter.getBarang());
                                    intent.putExtra("jenis", getter.getJenis());
                                    intent.putExtra("posjenis", config.getCustom("posjenis" + getter.getBarang(), "0"));
                                    intent.putExtra("warna", getter.getWarna());
                                    intent.putExtra("variasi", getter.getVariasi());
                                    intent.putExtra("posukuran", config.getCustom("posukuran" + getter.getBarang(), "0"));
                                    ctxAdapter.startActivity(intent);
                                    break;

                                case R.id.menu_delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);

                                    final Database db = new Database(ctxAdapter);
                                    String q = "SELECT * FROM qorderdetail WHERE idbarang = '"+getter.getIdbarang()+"' ";
                                    Cursor c = db.sq(q);
                                    if (c.getCount() > 0){
                                        String jenis;
                                        if (getter.getJenis().equals("Jasa")){
                                            jenis = "Jasa";
                                        } else {
                                            jenis = "Barang";
                                        }
                                        builder.setTitle("Anda Yakin Ingin Menghapus Data Ini");
                                        builder.setMessage("Jika Anda Menghapus '" + jenis + "' Ini, Maka Data di Menu Penjualan Yang Menggunakan '" + jenis + "' Ini Juga Akan Ikut Terhapus");
                                    } else {
                                        builder.setMessage("Anda Yakin Ingin Menghapus Data Ini");
                                    }
                                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (db.deleteBarang(getter.getIdbarang())){
                                                data.remove(position);
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
                                    builder.show();
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

        public class BarangViewHolder extends RecyclerView.ViewHolder {
            TextView kategori, barang, variasi, opt;

            public BarangViewHolder(@NonNull View itemView) {
                super(itemView);

                kategori = (TextView) itemView.findViewById(R.id.tvKategori);
                barang = (TextView) itemView.findViewById(R.id.tvBarang);
                variasi = (TextView) itemView.findViewById(R.id.tvVariasi);
                opt = (TextView) itemView.findViewById(R.id.tvOpt);
            }
        }
    }

    static class getterBarang{
        private int idbarang;
        private String barang;
        private String jenis;
        private String warna;
        private String variasi;
        private String ukuran;

        public getterBarang(int idbarang, String barang, String jenis, String warna, String variasi, String ukuran){
            this.idbarang = idbarang;
            this.barang = barang;
            this.jenis = jenis;
            this.warna = warna;
            this.variasi = variasi;
            this.ukuran = ukuran;
        }

        public int getIdbarang() {
            return idbarang;
        }

        public String getBarang() {
            return barang;
        }

        public String getJenis() {
            return jenis;
        }

        public String getWarna() {
            return warna;
        }

        public String getVariasi() {
            return variasi;
        }

        public String getUkuran() {
            return ukuran;
        }
    }
}