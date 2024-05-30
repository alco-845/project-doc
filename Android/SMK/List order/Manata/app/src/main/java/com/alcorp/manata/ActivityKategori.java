package com.alcorp.manata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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

public class ActivityKategori extends AppCompatActivity {

    Toolbar appbar;
    RecyclerView listkategori;
    AdapterListKategori adapter;
    List<getterKategori> DaftarKategori;
    Database db;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
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
        tvTb.setText("Kategori");

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
                getKategori(cari);
            }
        });
    }

    public void getKategori(String keyword){
        DaftarKategori = new ArrayList<>();
        listkategori = (RecyclerView) findViewById(R.id.listkategori);
        listkategori.setHasFixedSize(true);
        listkategori.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterListKategori(this, DaftarKategori);
        listkategori.setAdapter(adapter);

        String q;

        if (TextUtils.isEmpty(keyword)){
            q = "SELECT * FROM tblkategori";
        } else {
            q = "SELECT * FROM tblkategori WHERE kategori LIKE '%"+keyword+"%' ORDER BY kategori";
        }

        Cursor cur = db.sq(q);
        if (cur.getCount() > 0){
            while(cur.moveToNext()){
                DaftarKategori.add(new getterKategori(
                        cur.getInt(cur.getColumnIndex("idkategori")),
                        cur.getString(cur.getColumnIndex("kodekategori")),
                        cur.getString(cur.getColumnIndex("kategori"))
                ));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void tambah(View view) {
        Intent intent = new Intent(this, ActivityKategoriTambah.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getKategori("");
    }

    class AdapterListKategori extends RecyclerView.Adapter<AdapterListKategori.KategoriViewHolder>{
        private Context ctxAdapter;
        private List<getterKategori> data;

        public AdapterListKategori(Context ctxAdapter, List<getterKategori> data) {
            this.ctxAdapter = ctxAdapter;
            this.data = data;
        }

        @NonNull
        @Override
        public AdapterListKategori.KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ctxAdapter).inflate(R.layout.list_item_kategori, parent, false);
            return new KategoriViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AdapterListKategori.KategoriViewHolder holder, final int position) {
            final getterKategori getter = data.get(position);
            holder.kodekatagori.setText("Kode : " + getter.getKodekategori());
            holder.kategori.setText(getter.getKategori());
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
                                    Intent intent = new Intent(ctxAdapter, ActivityKategoriTambah.class);
                                    intent.putExtra("idkategori", getter.getIdkategori());
                                    intent.putExtra("kodekategori", getter.getKodekategori());
                                    intent.putExtra("kategori", getter.getKategori());
                                    ctxAdapter.startActivity(intent);
                                    break;

                                case R.id.menu_delete:
                                    final Database db = new Database(ctxAdapter);
                                    String q = "SELECT * FROM qorder WHERE idkategori = '"+ getter.getIdkategori() +"' ";
                                    Cursor c = db.sq(q);
                                    if (c.getCount() > 0){
                                        Toast.makeText(ctxAdapter, "Kategori Ini Digunakan Didalam Menu Barang, Tidak Dapat Dihapus", Toast.LENGTH_SHORT).show();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                                        builder.setMessage("Anda Yakin Ingin Menghapus Data Ini");
                                        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (db.deleteKategori(getter.getIdkategori())){
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

        class KategoriViewHolder extends RecyclerView.ViewHolder {
            TextView kodekatagori, kategori, opt;

            public KategoriViewHolder(@NonNull View itemView) {
                super(itemView);
                kodekatagori = (TextView) itemView.findViewById(R.id.tvKodekategori);
                kategori = (TextView) itemView.findViewById(R.id.tvKategori);
                opt = (TextView) itemView.findViewById(R.id.tvOpt);
            }
        }
    }

    class getterKategori{
        private int idkategori;
        private String kodekategori;
        private String kategori;

        public getterKategori(int idkategori, String kodekategori, String kategori){
            this.idkategori = idkategori;
            this.kodekategori = kodekategori;
            this.kategori = kategori;
        }

        public int getIdkategori() {
            return idkategori;
        }

        public String getKodekategori() {
            return kodekategori;
        }

        public String getKategori() {
            return kategori;
        }
    }
}