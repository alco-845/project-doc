package com.alcorp.manata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ActivityPenjualanCari extends AppCompatActivity {

    Toolbar appbar;
    Database db;
    String type;

    List<ActivityPelanggan.getterPelanggan> daftarPelanggan;
    AdapterListPelangganCari adapterPelanggan;

    List<ActivityBarang.getterBarang> daftarBarang;
    AdapterListBarangCari adapterBarang;

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
        wInfo.setVisibility(View.GONE);

        db = new Database(this);
        type = getIntent().getStringExtra("cari");

        if (type.equals("pelanggan")) {
            tvTb.setText("Cari Pelanggan");
            getPelanggan("");
        } else if (type.equals("barang")) {
            tvTb.setText("Cari Barang / Jasa");
            getBarang("");
        }

        final EditText edtCari = (EditText) findViewById(R.id.eCari);
        edtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = edtCari.getText().toString();
                if (type.equals("pelanggan")){
                    getPelanggan(keyword);
                } else if (type.equals("barang")){
                    getBarang(keyword);
                }
            }
        });
    }

    //--------------------------------------------------------------------------------------------------------//
    public void getPelanggan(String keyword) {
        daftarPelanggan = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listpelanggan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterPelanggan = new AdapterListPelangganCari(this, daftarPelanggan);
        recyclerView.setAdapter(adapterPelanggan);

        String q;

        if (TextUtils.isEmpty(keyword)){
            q = "SELECT * FROM tblpelanggan";
        }else {
            q = "SELECT * FROM tblpelanggan WHERE pelanggan LIKE '%"+keyword+"%' ORDER BY pelanggan";
        }
        Cursor cur = db.sq(q);
        while(cur.moveToNext()){
            daftarPelanggan.add(new ActivityPelanggan.getterPelanggan(
                    cur.getInt(cur.getColumnIndex("idpelanggan")),
                    cur.getString(cur.getColumnIndex("pelanggan")),
                    cur.getString(cur.getColumnIndex("alamat")),
                    cur.getString(cur.getColumnIndex("telp"))
            ));
        }
        adapterPelanggan.notifyDataSetChanged();
    }

    //--------------------------------------------------------------------------------------------------------//
    public void getBarang(String keyword) {
        daftarBarang = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listpelanggan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterBarang = new AdapterListBarangCari(this, daftarBarang);
        recyclerView.setAdapter(adapterBarang);

        String q;

        if (TextUtils.isEmpty(keyword)){
            q = "SELECT * FROM tblbarang";
        }else {
            q = "SELECT * FROM tblbarang WHERE barang LIKE '%"+keyword+"%' ORDER BY barang";
        }
        Cursor cur = db.sq(q);
        while(cur.moveToNext()){
            daftarBarang.add(new ActivityBarang.getterBarang(
                    cur.getInt(cur.getColumnIndex("idbarang")),
                    cur.getString(cur.getColumnIndex("barang")),
                    cur.getString(cur.getColumnIndex("jenis")),
                    cur.getString(cur.getColumnIndex("warna")),
                    cur.getString(cur.getColumnIndex("variasi")),
                    cur.getString(cur.getColumnIndex("ukuran"))
            ));
        }
        adapterBarang.notifyDataSetChanged();
    }


    public void tambah(View view){
        if (type.equals("pelanggan")){
            Intent intent = new Intent(this, ActivityPelangganTambah.class);
            startActivity(intent);
        } else if (type.equals("barang")){
            Intent intent = new Intent(this, ActivityBarangTambah.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type.equals("pelanggan")){
            getPelanggan("");
        } else if (type.equals("barang")){
            getBarang("");
        }
    }
}

//--------------------------------------------------------------------------------------------------------//
class AdapterListPelangganCari extends RecyclerView.Adapter<AdapterListPelangganCari.PelangganCariViewHolder> {
    private Context ctxAdapter;
    private List<ActivityPelanggan.getterPelanggan> data;

    public AdapterListPelangganCari(Context ctxAdapter, List<ActivityPelanggan.getterPelanggan> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public PelangganCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view = inflater.inflate(R.layout.list_item_pelanggan, viewGroup, false);
        return new PelangganCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PelangganCariViewHolder holder, int i) {
        final ActivityPelanggan.getterPelanggan getter = data.get(i);

        holder.nama.setText("Nama : "+getter.getPelanggan());
        holder.alamat.setText("Alamat : "+getter.getAlamat());
        holder.telp.setText("No. Telepon : "+getter.getNoTelp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter, ActivityPenjualan.class);
                terima.putExtra("idpelanggan", getter.getIdPelanggan());
                terima.putExtra("pelanggan", getter.getPelanggan());
                ((ActivityPenjualanCari) ctxAdapter).setResult(1, terima);
                ((ActivityPenjualanCari) ctxAdapter).finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class PelangganCariViewHolder extends RecyclerView.ViewHolder{
        private TextView nama,alamat,telp,opt;
        public PelangganCariViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = (TextView)itemView.findViewById(R.id.tvPelanggan);
            alamat = (TextView)itemView.findViewById(R.id.tvAlamat);
            telp = (TextView)itemView.findViewById(R.id.tvTelp);
            opt = (TextView)itemView.findViewById(R.id.tvOpt);
            opt.setVisibility(View.GONE);
        }
    }
}

//--------------------------------------------------------------------------------------------------------//
class AdapterListBarangCari extends RecyclerView.Adapter<AdapterListBarangCari.BarangCariViewHolder> {
    private Context ctxAdapter;
    private List<ActivityBarang.getterBarang> data;

    public AdapterListBarangCari(Context ctxAdapter, List<ActivityBarang.getterBarang> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public BarangCariViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctxAdapter);
        View view = inflater.inflate(R.layout.list_item_barang, viewGroup, false);
        return new BarangCariViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarangCariViewHolder holder, int i) {
        final ActivityBarang.getterBarang getter = data.get(i);

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
            public void onClick(View v) {
                Intent terima = new Intent(ctxAdapter, ActivityPenjualan.class);
                terima.putExtra("idbarang", getter.getIdbarang());
                terima.putExtra("barang", getter.getBarang());
                ((ActivityPenjualanCari) ctxAdapter).setResult(2, terima);
                ((ActivityPenjualanCari) ctxAdapter).finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class BarangCariViewHolder extends RecyclerView.ViewHolder{
        private TextView kategori, barang, variasi, opt;
        public BarangCariViewHolder(@NonNull View itemView) {
            super(itemView);
            kategori = (TextView) itemView.findViewById(R.id.tvKategori);
            barang = (TextView) itemView.findViewById(R.id.tvBarang);
            variasi = (TextView) itemView.findViewById(R.id.tvVariasi);
            opt = (TextView)itemView.findViewById(R.id.tvOpt);
            opt.setVisibility(View.GONE);
        }
    }
}