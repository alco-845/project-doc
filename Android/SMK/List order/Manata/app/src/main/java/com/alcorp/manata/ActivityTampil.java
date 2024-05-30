package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.alcorp.manata.ActivityPenjualan.removeE;
import static com.alcorp.manata.ActivityPenjualan.setDatePickerNormal;

public class ActivityTampil extends AppCompatActivity {

    Toolbar appbar;
    View v ;
    Database db ;
    ArrayList arrayList = new ArrayList();
    String type, dari, ke;
    Calendar calendar ;
    int year,month, day ;
    EditText eCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil);
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

        v = this.findViewById(android.R.id.content);
        db = new Database(this) ;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        type = getIntent().getStringExtra("type");

        Spinner spinner = (Spinner) findViewById(R.id.spItems) ;
        eCari = (EditText) findViewById(R.id.eCari) ;
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (type.equals("status")) {
                    arrayList.clear();
                    loadList2(eCari.getText().toString(), "");
                } else if (type.equals("metode")) {
                    arrayList.clear();
                    loadList3(eCari.getText().toString(), "");
                } else if (type.equals("pendapatan")) {
                    arrayList.clear();
                    loadList4(eCari.getText().toString(), "");
                }
            }
        });

        setText();

        List<String> categories = new ArrayList<>();
        if (type.equals("status")){
            tvTb.setText("Laporan per Status");
            loadList2("", "p");

            categories = new ArrayList<String>();
            categories.add("Semua");
            categories.add("Konfirmasi");
            categories.add("Proses Desain");
            categories.add("Proses Produksi");
            categories.add("Selesai");
        } else if (type.equals("metode")){
            tvTb.setText("Laporan per Metode");
            loadList3("", "p");

            categories = new ArrayList<String>();
            categories.add("Semua");
            categories.add("Tunai DP");
            categories.add("Cicil");
            categories.add("Tunai Lunas");
            categories.add("Bank Transfer");
        } else if (type.equals("pendapatan")){
            tvTb.setText("Laporan Pendapatan");
            loadList4("", "p");

            spinner.setVisibility(View.GONE);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (type.equals("status")) {
                    arrayList.clear();
                    loadList2(eCari.getText().toString(), "");
                } else if (type.equals("metode")) {
                    arrayList.clear();
                    loadList3(eCari.getText().toString(), "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void setText(){
        dari = setDatePicker(year,month+1,day) ;
        ke = setDatePicker(year,month+1,day) ;
        String now = setDatePickerNormal(year,month+1,day) ;
        setText(v,R.id.eKe, now) ;
        setText(v,R.id.eDari, now) ;
    }

    public void loadList2(String cari, String isi){
        String item = getSpinnerItem(v,R.id.spItems) ;
        String q = "";

        if(cari != "" && isi.equals("") && item.equals("Semua")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Semua")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi.equals("") && item.equals("Semua")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND pelanggan LIKE '%" + cari + "%' AND tglorder BETWEEN '" + dari + "' AND '" + ke + "' ORDER BY tglorder DESC";

        } else if(cari != "" && isi.equals("") && item.equals("Konfirmasi")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Konfirmasi' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Konfirmasi")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Konfirmasi' ORDER BY tglorder DESC";
        } else if (cari.equals("") && item.equals("Konfirmasi")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Konfirmasi' AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";

        } else if(cari != "" && isi.equals("") && item.equals("Proses Desain")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Proses Desain' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Proses Desain")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Proses Desain' ORDER BY tglorder DESC";
        } else if (cari.equals("") && item.equals("Proses Desain")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Proses Desain' AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";

        } else if(cari != "" && isi.equals("") && item.equals("Proses Produksi")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Proses Produksi' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Proses Produksi")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Proses Produksi' ORDER BY tglorder DESC";
        } else if (cari.equals("") && item.equals("Proses Produksi")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Proses Produksi' AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";

        } else if(cari != "" && isi.equals("") && item.equals("Selesai")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Selesai' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Selesai")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Selesai' ORDER BY tglorder DESC";
        } else if (cari.equals("") && item.equals("Selesai")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND status = 'Selesai' AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recItem) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterItemList(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q) ;
        if(c.getCount() > 0){
            double jum=0;
            while(c.moveToNext()){
                String faktur = getString(c, "faktur");
                String pelanggan = getString(c,"pelanggan") ;
                String alamat = getString(c,"alamat") ;
                String telp = getString(c,"telp") ;
                String keterangan = getString(c,"keterangan") ;
                String tglorder = getString(c,"tglorder") ;
                String tglselesai = getString(c,"tglselesai") ;
                String stat = getString(c, "status");
                String metode = getString(c, "metode");
                String diskonOrder = getString(c, "diskonorder");
                String total = getString(c, "total");
                String bayar = getString(c, "bayar");
                String kembali = getString(c, "kembali");
                String kurang = getString(c, "kurang");
                String lunas = getString(c, "lunas");
                String kat = getString(c, "kategori");

                String campur = faktur+"__"+pelanggan+"__"+alamat+"__"+telp+"__"+keterangan+"__"+dateToNormal(tglorder)+"__" +dateToNormal(tglselesai)+"__" +stat+"__"+kat+"__"+metode+"__" +total+"__"+ActivityPenjualan.removeE(bayar)+"__" +ActivityPenjualan.removeE(kembali)+"__" +lunas+"__" +ActivityPenjualan.removeE(kurang)+"__" +diskonOrder;
                arrayList.add(campur);

                double tot=0.0;
                Cursor cur = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + faktur.substring(3));
                double sum=0.0;
                if (cur.moveToFirst()){
                    sum = cur.getDouble(0);
                }
                tot = tot+sum;

                jum += tot - Double.parseDouble(diskonOrder);
            }
            ActivityTampil.setText(v, R.id.tValue1, "Total Penjualan :");
            ActivityTampil.setText(v, R.id.tValue2, "Rp. " + ActivityPenjualan.removeE(jum));
            ActivityTampil.setText(v, R.id.tValue3, "");
            ActivityTampil.setText(v, R.id.tjumlah, "Jumlah Data : " + String.valueOf(c.getCount()));
            ActivityTampil.setText(v, R.id.tjumlah2, "");
        } else {
            ActivityTampil.setText(v, R.id.tValue1, "Total Penjualan :");
            ActivityTampil.setText(v, R.id.tValue2, "Rp. 0");
            ActivityTampil.setText(v, R.id.tValue3, "");
            ActivityTampil.setText(v, R.id.tjumlah, "Jumlah Data : 0");
            ActivityTampil.setText(v, R.id.tjumlah2, "");
        }
        adapter.notifyDataSetChanged();
    }

    public void loadList3(String cari, String isi){
        String item = getSpinnerItem(v,R.id.spItems) ;
        String q = "";

        if(cari != "" && isi.equals("") && item.equals("Semua")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Semua")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi.equals("") && item.equals("Semua")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND pelanggan LIKE '%" + cari + "%' AND tglorder BETWEEN '" + dari + "' AND '" + ke + "' ORDER BY tglorder DESC";

        } else if(cari != "" && isi.equals("") && item.equals("Tunai DP")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Tunai DP' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Tunai DP")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Tunai DP' ORDER BY tglorder DESC";
        } else if (cari.equals("") && item.equals("Tunai DP")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Tunai DP' AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";

        } else if(cari != "" && isi.equals("") && item.equals("Cicil")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Cicil' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Cicil")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Cicil' ORDER BY tglorder DESC";
        } else if (cari.equals("") && item.equals("Cicil")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Cicil' AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";

        } else if(cari != "" && isi.equals("") && item.equals("Cicil")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Cicil' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Cicil")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Cicil' ORDER BY tglorder DESC";
        } else if (cari.equals("") && item.equals("Cicil")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Cicil' AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";

        } else if(cari != "" && isi.equals("") && item.equals("Tunai Lunas")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Tunai Lunas' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Tunai Lunas")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Tunai Lunas' ORDER BY tglorder DESC";
        } else if (cari.equals("") && item.equals("Tunai Lunas")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Tunai Lunas' AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";

        } else if(cari != "" && isi.equals("") && item.equals("Bank Transfer")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Bank Transfer' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "" && item.equals("Bank Transfer")) {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Bank Transfer' ORDER BY tglorder DESC";
        } else if (cari.equals("") && item.equals("Bank Transfer")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND metode = 'Bank Transfer' AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recItem) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterItemList(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q) ;
        if(c.getCount() > 0){
            double jum=0;
            while(c.moveToNext()){
                String faktur = ActivityTampil.getString(c, "faktur");
                String pelanggan = getString(c,"pelanggan") ;
                String alamat = getString(c,"alamat") ;
                String telp = getString(c,"telp") ;
                String keterangan = getString(c,"keterangan") ;
                String tglorder = getString(c,"tglorder") ;
                String tglselesai = getString(c,"tglselesai") ;
                String stat = getString(c, "status");
                String metode = getString(c, "metode");
                String diskonOrder = getString(c, "diskonorder");
                String total = getString(c, "total");
                String bayar = getString(c, "bayar");
                String kembali = getString(c, "kembali");
                String kurang = getString(c, "kurang");
                String lunas = getString(c, "lunas");
                String kat = getString(c, "kategori");

                String campur = faktur+"__"+pelanggan+"__"+alamat+"__"+telp+"__"+keterangan+"__"+dateToNormal(tglorder)+"__" +dateToNormal(tglselesai)+"__" +stat+"__"+kat+"__"+metode+"__" +total+"__"+ActivityPenjualan.removeE(bayar)+"__" +ActivityPenjualan.removeE(kembali)+"__" +lunas+"__" +ActivityPenjualan.removeE(kurang)+"__" +diskonOrder;
                arrayList.add(campur);

                double tot=0.0;
                Cursor cur = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + faktur.substring(3));
                double sum=0.0;
                if (cur.moveToFirst()){
                    sum = cur.getDouble(0);
                }
                tot = tot+sum;

                jum += tot - Double.parseDouble(diskonOrder);
            }
            ActivityTampil.setText(v, R.id.tValue1, "Total Penjualan :");
            ActivityTampil.setText(v, R.id.tValue2, "Rp. " + ActivityPenjualan.removeE(jum));
            ActivityTampil.setText(v, R.id.tValue3, "");
            ActivityTampil.setText(v, R.id.tjumlah, "Jumlah Data : " + String.valueOf(c.getCount()));
            ActivityTampil.setText(v, R.id.tjumlah2, "");
        } else {
            ActivityTampil.setText(v, R.id.tValue1, "Total Penjualan :");
            ActivityTampil.setText(v, R.id.tValue2, "Rp. 0");
            ActivityTampil.setText(v, R.id.tValue3, "");
            ActivityTampil.setText(v, R.id.tjumlah, "Jumlah Data : 0");
            ActivityTampil.setText(v, R.id.tjumlah2, "");
        }
        adapter.notifyDataSetChanged();
    }

    public void loadList4(String cari, String isi){
        String q = "";

        if(cari != "" && isi.equals("")){
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND pelanggan LIKE '%"+cari+"%' ORDER BY tglorder DESC";
        } else if(cari.equals("") && isi != "") {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 ORDER BY tglorder DESC";
        } else {
            q = "SELECT * FROM qorder WHERE kirim=1 AND bayar>0 AND pelanggan LIKE '%"+cari+"%' AND tglorder BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglorder DESC";
        }
        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recItem) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterPendapatanList(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q) ;
        if(c.getCount() > 0){
            double totale = 0;
            double bayare = 0;
            double kembalie = 0;
            double kurange = 0;
            while(c.moveToNext()){
                String faktur = ActivityTampil.getString(c, "faktur");
                String tglorder = ActivityTampil.getString(c, "tglorder");
                String tglselesai = ActivityTampil.getString(c, "tglselesai");
                String nama = ActivityTampil.getString(c, "pelanggan");
                String total = ActivityTampil.getString(c, "total");
                String bayar = ActivityTampil.getString(c, "bayar");
                String kembali = ActivityTampil.getString(c, "kembali");
                String kurang = ActivityTampil.getString(c, "kurang");
                String lunas = ActivityTampil.getString(c, "lunas");
                String diskonOrder = ActivityTampil.getString(c, "diskonorder");

                String campur = faktur+"__"+ActivityTampil.dateToNormal(tglorder)+"__"+ActivityTampil.dateToNormal(tglselesai)+"__"+nama+"__"+ActivityPenjualan.removeE(total)+"__"+ActivityPenjualan.removeE(bayar)+"__"+ActivityPenjualan.removeE(kembali)+"__"+lunas+"__"+ActivityPenjualan.removeE(kurang)+"__"+diskonOrder;
                arrayList.add(campur);

                double tot=0.0;
                Cursor cur = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + faktur.substring(3));
                double sum=0.0;
                if (cur.moveToFirst()){
                    sum = cur.getDouble(0);
                }
                tot = tot+sum;

                totale += tot - Double.parseDouble(diskonOrder);
                bayare += Double.parseDouble(bayar);
                kembalie += Double.parseDouble(kembali);
                kurange += Double.parseDouble(kurang);
            }
            ActivityTampil.setText(v, R.id.tValue1, "Pendapatan : Rp. " + ActivityPenjualan.removeE(totale));
            ActivityTampil.setText(v, R.id.tValue2, "Pembayaran : Rp.  " + ActivityPenjualan.removeE(bayare));
            ActivityTampil.setText(v, R.id.tValue3, "Kembali : Rp.  " + ActivityPenjualan.removeE(kembalie));
            ActivityTampil.setText(v, R.id.tjumlah, "Jumlah Data : " + String.valueOf(c.getCount()));
            ActivityTampil.setText(v, R.id.tjumlah2, "Kekurangan : Rp. "+ ActivityPenjualan.removeE(kurange));
        } else {
            ActivityTampil.setText(v, R.id.tValue1, "Pendapatan : Rp. 0");
            ActivityTampil.setText(v, R.id.tValue2, "Kembali    : Rp. 0");
            ActivityTampil.setText(v, R.id.tValue3, "Pembayaran : Rp. 0");
            ActivityTampil.setText(v, R.id.tjumlah, "Jumlah Data : 0");
            ActivityTampil.setText(v, R.id.tjumlah2, "Kekurangan : Rp. 0");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type.equals("status")) {
            loadList2("", "p");
        } else if (type.equals("metode")) {
            loadList3("", "p");
        } else if (type.equals("pendapatan")) {
            loadList4("", "p");
        }
    }

    public void dateDari(View view){
        setDate(1);
    }
    public void dateKe(View view){
        setDate(2);
    }

    public void filtertgl(){
        if (type.equals("status")) {
            loadList2("", "");
        } else if (type.equals("metode")) {
            loadList3("", "");
        } else if (type.equals("metode")) {
            loadList4("", "");
        }
    }

    //start date time picker
    public void setDate(int i) {
        showDialog(i);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if(id == 2){
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            setText(v, R.id.eDari, setDatePickerNormal(thn,bln+1,day)) ;
            dari = setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            setText(v, R.id.eKe, setDatePickerNormal(thn,bln+1,day)) ;
            ke = setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };
    //end date time picker

    public static String setDatePicker(int year , int month, int day) {
        String bln, hri ;
        if(month < 10){
            bln = "0"+ String.valueOf(month) ;
        } else {
            bln = String.valueOf(month) ;
        }

        if(day < 10){
            hri = "0"+ String.valueOf(day) ;
        } else {
            hri = String.valueOf(day) ;
        }

        return String.valueOf(year) + bln+hri;
    }

    public static String dateToNormal(String date){
        try {
            String b1 = date.substring(4) ;
            String b2 = b1.substring(2) ;

            String m = b1.substring(0,2) ;
            String d = b2.substring(0,2) ;
            String y = date.substring(0,4) ;
            return d+"/"+m+"/"+y ;
        }catch (Exception e){
            return "Belum Selesai" ;
        }
    }

    public static Boolean setText(View v, int id,String t){ //cara instal //FsetText(this.findViewById(android.R.id.content), R.id.jumlah,"ini String") ;
        TextView a = (TextView) v.findViewById(id) ;
        try {
            a.setText(t);
            return true ;
        }catch (Exception e){
            return false;
        }
    }

    public static String getSpinnerItem(View v,int idSpinner){
        try {
            Spinner s = (Spinner) v.findViewById(idSpinner) ;
            return s.getSelectedItem().toString() ;
        }catch (Exception e){
            return "" ;
        }
    }

    public static String getString(Cursor c, String name){
        return c.getString(c.getColumnIndex(name)) ;
    }
}


class AdapterItemList extends RecyclerView.Adapter<AdapterItemList.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterItemList(Context a, ArrayList<String> kota) {
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
        TextView nama, alamat, telp, opt;
        ConstraintLayout click;

        public ViewHolder(View view) {
            super(view);

            nama = (TextView) view.findViewById(R.id.tvPelanggan);
            alamat = (TextView) view.findViewById(R.id.tvAlamat);
            telp = (TextView) view.findViewById(R.id.tvTelp);
            opt = (TextView) view.findViewById(R.id.tvOpt);
            click = (ConstraintLayout) view.findViewById(R.id.cItem);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final String[] row = data.get(i).split("__");



        viewHolder.nama.setText(row[0]);
        viewHolder.alamat.setText("Nama : "+row[1]+"\nTanggal : "+row[5] + " - "+row[6]);
        viewHolder.telp.setText("Status : "+row[7]);
        viewHolder.click.setTag(row[0]);

        viewHolder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(c);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_text);

                String isiBarang="", isiJasa="", jenis="";
                Database db = new Database(c);
                Cursor cursor = db.sq("SELECT * FROM qorderdetail WHERE faktur = '"+row[0]+"' ");
                if (cursor.getCount()>0) {
                    while (cursor.moveToNext()) {
                        String diskon, setelahDsc;
                        double subTot = Double.parseDouble(ActivityTampil.getString(cursor, "jumlah")) * Double.parseDouble(ActivityTampil.getString(cursor, "harga"));
                        double tot = subTot - Double.parseDouble(ActivityTampil.getString(cursor, "diskonitem"));

                        if (ActivityTampil.getString(cursor, "diskonitem").equals("0")){
                            diskon = "";
                            setelahDsc = "";
                        } else {
                            diskon = "\n\nDiskon = " + ActivityPenjualan.removeE(ActivityTampil.getString(cursor, "diskonitem"));
                            setelahDsc = "\n\nSetelah Diskon = " + ActivityPenjualan.removeE(tot);
                        }

                        isiBarang +=
                                "\n\n-------------------------------------------------------"+
                                "\n\nBarang : "+ActivityTampil.getString(cursor, "barang")+
                                "\n\nVariasi : "+ActivityTampil.getString(cursor, "variasi")+
                                "\n\nUkuran : "+ActivityTampil.getString(cursor, "ukuran")+
                                diskon+
                                "\n\n"+ActivityTampil.getString(cursor, "jumlah")+" x "+ActivityPenjualan.removeE(ActivityTampil.getString(cursor, "harga"))+" = "+ActivityPenjualan.removeE(subTot)+
                                setelahDsc
                        ;

                        isiJasa +=
                                "\n\n-------------------------------------------------------"+
                                "\n\nBarang : "+ActivityTampil.getString(cursor, "barang")+
                                diskon+
                                "\n\n"+ActivityTampil.getString(cursor, "jumlah")+" x "+ActivityPenjualan.removeE(ActivityTampil.getString(cursor, "harga"))+" = "+ActivityPenjualan.removeE(subTot)+
                                setelahDsc
                        ;

                        jenis = ActivityTampil.getString(cursor, "jenis");
                    }
                }

                String kemkur = "";
                if (row[13].equals("1")){
                    kemkur = "Kekurangan : "+row[14];
                } else {
                    kemkur = "Kembali : " +row[12];
                }

                String ket = "";
                if (row[4].equals("")){
                    ket = "-";
                } else {
                    ket = row[5];
                }

                EditText etMulti = dialog.findViewById(R.id.etMulti);
                etMulti.setFocusable(false);

                cursor.moveToNext();
                double total=0.0;
                Cursor cur = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + row[0].substring(3));
                double sum=0.0;
                if (cur.moveToFirst()){
                    sum = cur.getDouble(0);
                }
                total = total+sum;
                double dsc = total - Double.parseDouble(row[15]) ;

                String dis = "";
                if (row[15].equals("0")){
                    dis = "";
                } else {
                    dis = "\n\nDiskon : "+removeE(row[15]);
                }

                final String header = "Faktur : "+row[0]+
                        "\n\nNama : "+row[1]+
                        "\n\nAlamat : "+row[2]+
                        "\n\nTelp : "+row[3]+
                        "\n\nTanggal Order : "+row[5]+
                        "\n\nTanggal Selesai : "+row[6]+
                        "\n\nKategori : "+row[8]+
                        "\n\nStatus : "+row[7]+
                        "\n\nMetode Pembayaran : "+row[9]+
                        "\n\nKeterangan : "+ket,

                        footer = "\n\n-------------------------------------------------------"+
                        dis+
                        "\n\nSub Total : "+ActivityPenjualan.removeE(total)+
                        "\n\nTotal : "+ActivityPenjualan.removeE(dsc)+
                        "\n\nBayar : "+row[11]+
                        "\n\n"+kemkur,

                        isiJenis = jenis;

                if (jenis.equals("Barang")){
                        etMulti.setText(
                                header+
                                isiBarang+
                                footer
                                );
                } else if (jenis.equals("Jasa")) {
                    etMulti.setText(
                            header +
                                    isiJasa +
                                    footer
                    );
                }

                Button btnCopy = dialog.findViewById(R.id.btnCopy);
                final String finalIsiBarang = isiBarang;
                final String finalIsiJasa = isiJasa;

                btnCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String isi = "";
                        if (isiJenis.equals("Barang")){
                            isi = finalIsiBarang;
                        } else if (isiJenis.equals("Jasa")){
                            isi = finalIsiJasa;
                        }
                        String text = header+isi+footer;

                        ClipData clipData = ClipData.newPlainText("text", text);
                        ClipboardManager clipboardManager = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setPrimaryClip(clipData);

                        Toast.makeText(c, "Berhasil Copy", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        });

        viewHolder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(c,viewHolder.opt);
                popupMenu.inflate(R.menu.option_item);
                Menu popup = popupMenu.getMenu();
                popup.findItem(R.id.menu_delete).setVisible(false);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_update:
                                String tgl = "";

                                if (row[6].equals("Belum Selesai")){
                                    tgl = "";
                                } else {
                                    tgl = row[6];
                                }

                                Intent intent = new Intent(c, ActivityPenjualanUpdate.class);
                                intent.putExtra("faktur", row[0]);
                                intent.putExtra("tglselesai", tgl);
                                intent.putExtra("status", row[7]);
                                c.startActivity(intent);
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
}

class AdapterPendapatanList extends RecyclerView.Adapter<AdapterPendapatanList.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterPendapatanList(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_penjualan, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView faktur, tanggal, nama, pendapatan;
        private ImageButton btn;

        public ViewHolder(View view) {
            super(view);

            faktur = (TextView) view.findViewById(R.id.tvCustom);
            tanggal = (TextView) view.findViewById(R.id.tvKat);
            nama = (TextView) view.findViewById(R.id.tvUkuran);
            pendapatan = (TextView) view.findViewById(R.id.tvHarga);
            btn = (ImageButton) view.findViewById(R.id.ibtnHapus);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final String[] row = data.get(i).split("__");

        Database db = new Database(c);

        double total=0.0;
        Cursor cur = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + row[0].substring(3));
        double sum=0.0;
        if (cur.moveToFirst()){
            sum = cur.getDouble(0);
        }
        total = total+sum;
        double dsc = total - Double.parseDouble(row[9]) ;

        String kemkur = "";
        if (row[7].equals("1")){
            kemkur = "Kekurangan : "+row[8];
        } else {
            kemkur = "Kembali : " +row[6];
        }

        final String isiKurang = kemkur, isiTotal = String.valueOf(total), isiDsc = String.valueOf(dsc);

        String dis = "";
        if (row[6].equals("0")){
            dis = "";
        } else {
            dis = "Diskon : "+row[6]+"\n";
        }

        viewHolder.faktur.setText(row[0]);
        viewHolder.tanggal.setText("Tanggal : "+row[1]+" - "+row[2]);
        viewHolder.nama.setText("Nama : "+row[3]);
        viewHolder.pendapatan.setText(dis+"Sub Total : "+ActivityPenjualan.removeE(total)+"\nTotal : "+ActivityPenjualan.removeE(dsc)+"\nBayar : "+row[5]+"\n"+kemkur);

        final String finalDis = dis;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = row[0] +"\n"+
                        "Tanggal : "+row[1]+" - "+row[2] +"\n"+
                        "Nama : "+row[3] +"\n"+
                        finalDis +
                        "Sub Total : "+ActivityPenjualan.removeE(isiTotal) +"\n"+
                        "Total : "+ActivityPenjualan.removeE(isiDsc) +"\n"+
                        "Bayar : "+row[5]+"\n"+isiKurang;

                ClipData clipData = ClipData.newPlainText("text", text);
                ClipboardManager clipboardManager = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(c, "Berhasil Copy", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.btn.setBackgroundResource(R.drawable.print);
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, ActivityCetak.class);
                intent.putExtra("faktur", row[0]);
                intent.putExtra("type", "laporan");
                c.startActivity(intent);
            }
        });
    }
}