package com.alcorp.manata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityPenjualan extends AppCompatActivity {

    Toolbar appbar;
    Database db;
    View v;
    Config config;
    Calendar calendar;
    String faktur="00000000", tnPelanggan, tnKodeKategori, tnBarang, sStatus="", sKat="";
    int year, month, day, tJumlah=0, tIdpelanggan, tIdBarang, isikeranjang=0, tIdKategori;
    ImageButton btnTglOrder, btnCariPelanggan, btnCariBarang, btnAddJ, btnRemoveJ;
    TextInputEditText edtTgl, edtBarang, edtPelanggan, edtHarga, edtKeterangan;
    EditText edtJumlah;
    Spinner spStatus, spKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);
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
        tvTb.setText("Penjualan");

        db = new Database(this);
        v = this.findViewById(android.R.id.content);
        config = new Config(getSharedPreferences("config",this.MODE_PRIVATE));
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

//        ActivityTampil.setText(v,R.id.edtNomorFaktur, faktur);

        edtPelanggan = (TextInputEditText) findViewById(R.id.edtNamaPelanggan);
        edtTgl = (TextInputEditText) findViewById(R.id.edtTglOrder);
        edtBarang = (TextInputEditText) findViewById(R.id.edtNamaBarang);
        edtHarga = (TextInputEditText) findViewById(R.id.edtHarga);
        edtKeterangan = (TextInputEditText) findViewById(R.id.edtKeterangan);

        edtJumlah = (EditText) findViewById(R.id.edtJumlah);

        String date_n=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        edtTgl.setText(date_n);

        btnTglOrder = (ImageButton)findViewById(R.id.ibtnTglOrder);
        btnTglOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(1);
            }
        });

        spKategori = (Spinner) findViewById(R.id.spKat);
        spStatus = (Spinner) findViewById(R.id.spStat);

        setSpinner();
//        cek("000");
        btnCari();
//        loadCart();
//        getTotal();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void setSpinner(){
        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                sStatus = parent.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final List<String> getIdKat = db.getIdKategori();
        final List<String> getKodeKat = db.getKodeKategori();
        getKategoriData();
        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                sKat = parent.getItemAtPosition(pos).toString();
                tIdKategori = Integer.valueOf(getIdKat.get(parent.getSelectedItemPosition()));
                tnKodeKategori = getKodeKat.get(parent.getSelectedItemPosition());
                cek(tnKodeKategori);
                loadCart();
                getTotal();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getKategoriData(){
        List<String> labels = db.getKategori();

        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(data);
    }

    public void cek(String tnKodeKategori){
        Cursor c = db.sq("SELECT * FROM tblorder WHERE bayar = 0");
        Cursor cur = db.sq("SELECT * FROM tblorder");
        if (c.getCount()>0 && !ActivityCetak.getText(v, R.id.edtNomorFaktur).substring(3).equals(cur.getCount())){
            c.moveToLast();
            String fakture = ActivityTampil.getString(c, "idorder");
            ActivityTampil.setText(v, R.id.edtNomorFaktur, tnKodeKategori+faktur.substring(0, faktur.length() - String.valueOf(fakture).length())+fakture);
            loadCart();
            getTotal();
        } else {
            getFaktur(tnKodeKategori);
        }
    }

    private void getFaktur(String tnKodeKategori){
        List<Integer> idorder = new ArrayList<Integer>();
        String q="SELECT idorder FROM tblorder";
        Cursor c = db.sq(q);
        if (c.moveToNext()){
            do {
                idorder.add(c.getInt(0));
            }while (c.moveToNext());
        }
        String tempFaktur = "";
        int IdFaktur=0;
        if (c.getCount()==0) {
            tempFaktur = faktur.substring(0, faktur.length() - String.valueOf(IdFaktur).length()) + "1";
        } else {
            IdFaktur = idorder.get(c.getCount()-1)+1;
            tempFaktur = faktur.substring(0,faktur.length() - String.valueOf(IdFaktur).length())+IdFaktur;
        }
        ActivityTampil.setText(v,R.id.edtNomorFaktur, tnKodeKategori + tempFaktur);
    }

    private void btnCari(){
        btnCariPelanggan = (ImageButton)findViewById(R.id.ibtnNamaPelanggan);
        btnCariPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ActivityPenjualan.this,ActivityPenjualanCari.class);
                i.putExtra("cari","pelanggan");
                startActivityForResult(i,1);
            }
        });

        btnCariBarang = (ImageButton)findViewById(R.id.ibtnNamaBarang);
        btnCariBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ActivityPenjualan.this, ActivityPenjualanCari.class);
                i.putExtra("cari","barang");
                startActivityForResult(i,2);
            }
        });

        btnAddJ = (ImageButton)findViewById(R.id.ibtnPlus);
        btnAddJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tJumlah=tJumlah+1;
                setJumlah(tJumlah);
            }
        });
        btnRemoveJ = (ImageButton)findViewById(R.id.ibtnMinus);
        btnRemoveJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tJumlah=tJumlah-1;
                setJumlah(tJumlah);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){
            tIdpelanggan = data.getIntExtra("idpelanggan",0);
            tnPelanggan = data.getStringExtra("pelanggan");
            getPelanggan(String.valueOf(tIdpelanggan));
        } else if (resultCode == 2){
            tIdBarang = data.getIntExtra("idbarang",0);
            tnBarang = data.getStringExtra("barang");
            getBarang(String.valueOf(tIdBarang));
        }
    }

    public void getPelanggan(String idpelanggan){
        String q = "SELECT * FROM tblpelanggan WHERE idpelanggan = '"+idpelanggan+"' ";
        Cursor c = db.sq(q) ;
        c.moveToNext() ;

        edtPelanggan.setText(ActivityTampil.getString(c, "pelanggan"));
    }

    public void getBarang(String idbarang){
        String q = "SELECT * FROM tblbarang WHERE idbarang = '"+idbarang+"' ";
        Cursor c = db.sq(q) ;
        c.moveToNext() ;

        edtBarang.setText(ActivityTampil.getString(c, "barang"));
    }

    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, dTerima, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dTerima = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ActivityTampil.setText(v, R.id.edtTglOrder, setDatePickerNormal(thn,bln+1,day)) ;
        }
    };

    private void setJumlah(Integer jumlah){
        ActivityTampil.setText(v,R.id.edtJumlah,String.valueOf(jumlah));

        btnAddJ.setVisibility(View.VISIBLE);

        if (jumlah<1){
            btnRemoveJ.setVisibility(View.INVISIBLE);
        }else {
            btnRemoveJ.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tJumlah=0;
        ActivityTampil.setText(v,R.id.edtJumlah,"0");
        ActivityTampil.setText(v,R.id.edtDiskonItem,"0");
        setJumlah(0);
    }

    public void insertTransaksi(View view) {
        String eFaktur = ActivityCetak.getText(v, R.id.edtNomorFaktur);
        String eTglP = edtTgl.getText().toString();
        String eNPelanggan = edtPelanggan.getText().toString();
        String eNBarang = edtBarang.getText().toString();
        String eNDiskon = ActivityCetak.getText(v, R.id.edtDiskonItem);
        String eHarga = edtHarga.getText().toString();
        String eJumlah = edtJumlah.getText().toString();
        String eKet = edtKeterangan.getText().toString();

        String stat = "";

        if (sStatus.equals("Terkirim")){
            stat = "2";
        } else {
            stat = "1";
        }

        double total=0.0;
        Cursor cursor = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + eFaktur.substring(3));
        double sum=0.0;
        if (cursor.moveToFirst()){
            sum = cursor.getDouble(0);
        }
        total = total + sum;

        if (eNPelanggan.equals("") || eNBarang.equals("") || eNDiskon.equals("") || eHarga.equals("")  || eJumlah.equals("0") || eJumlah.equals("")) {
            Toast.makeText(ActivityPenjualan.this, "Isi Data Dengan Benar", Toast.LENGTH_SHORT).show();
        } else if (Double.parseDouble(eHarga) < Double.parseDouble(eNDiskon)){
            Toast.makeText(ActivityPenjualan.this, "Diskon Tidak Boleh Lebih Dari Harga", Toast.LENGTH_SHORT).show();
        } else {
            String q = "SELECT * FROM tblorder WHERE idorder = '"+eFaktur.substring(3)+"' ";
            Cursor c = db.sq(q);
                if (c.getCount()==0){
                    // Start Insert
                    if (db.insertOrder(eFaktur, String.valueOf(tIdpelanggan), String.valueOf(tIdKategori), convertDate(eTglP), String.valueOf(total), eKet, stat, sStatus) && db.insertOrderDetail1(Integer.valueOf(eFaktur.substring(3)), String.valueOf(tIdBarang), eNDiskon, eHarga, eJumlah)) {
                        Toast.makeText(ActivityPenjualan.this, "Tambah Keranjang Berhasil", Toast.LENGTH_SHORT).show();
                        getTotal();
                        clearText();
                        loadCart();
                    } else {
                        Toast.makeText(ActivityPenjualan.this, "Tambah Keranjang Gagal", Toast.LENGTH_SHORT).show();
                    }
                    // End Insert
                } else {
                    // Start Update
                    if (db.updateOrder1(Integer.parseInt(eFaktur.substring(3)), eFaktur, String.valueOf(tIdpelanggan), String.valueOf(tIdKategori), convertDate(eTglP), eKet, stat, sStatus) && db.insertOrderDetail1(Integer.valueOf(eFaktur.substring(3)), String.valueOf(tIdBarang), eNDiskon, eHarga, eJumlah)) {
                        Toast.makeText(ActivityPenjualan.this, "Tambah Keranjang Berhasil", Toast.LENGTH_SHORT).show();
                        getTotal();
                        clearText();
                        loadCart();
                    } else {
                        Toast.makeText(ActivityPenjualan.this, "Tambah Keranjang Gagal", Toast.LENGTH_SHORT).show();
                    }
                    // End Update
                }
        }
    }

    public void getTotal(){
        double total=0.0;
        String idorder = ActivityCetak.getText(v, R.id.edtNomorFaktur);

        Cursor c = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM qorderdetail WHERE idorder=" + idorder.substring(3));
        double sum=0.0;
        if (c.moveToFirst()){
            sum = c.getDouble(0);
        }
        total=total+sum;
        ActivityTampil.setText(v,R.id.tvTotalBayar,"Rp. "+(removeE(total)));
    }

    public void loadCart(){
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recTransaksi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ArrayList arrayList = new ArrayList();
        RecyclerView.Adapter adapter=new AdapterPenjualan(this,arrayList);
        recyclerView.setAdapter(adapter);

        String tempFaktur = ActivityCetak.getText(v, R.id.edtNomorFaktur);

        String q = "SELECT * FROM qorderdetail WHERE idorder = '"+tempFaktur.substring(3)+"' ";
        Cursor c = db.sq(q);
        if (c.getCount()>0){
            while (c.moveToNext()){
                String campur = ActivityTampil.getString(c,"idorderdetail")+"__"+
                        ActivityTampil.getString(c, "barang") + "__" +
                        ActivityTampil.getString(c, "jenis") + "__" +
                        ActivityTampil.getString(c, "warna") + "__" +
                        ActivityTampil.getString(c, "variasi") + "__" +
                        ActivityTampil.getString(c, "ukuran") + "__" +
                        ActivityTampil.getString(c, "diskonitem") + "__" +
                        ActivityTampil.getString(c, "jumlah") + "__" +
                        ActivityTampil.getString(c, "harga");
                arrayList.add(campur);
            }
        }else{
        }
        adapter.notifyDataSetChanged();
    }

    private void clearText(){
        ActivityTampil.setText(v,R.id.edtJumlah,"0");
        ActivityTampil.setText(v,R.id.edtDiskonItem,"0");
        ActivityTampil.setText(v,R.id.edtNamaBarang,"");
        ActivityTampil.setText(v,R.id.edtHarga,"");
    }

    public void reset(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage("Anda Yakin Ingin Hapus Isi Keranjang?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String faktur = ActivityCetak.getText(v, R.id.edtNomorFaktur);
                String q = "DELETE FROM tblorderdetail WHERE idorder="+faktur.substring(3);
                try {
                    db.exc(q);
                    Toast.makeText(ActivityPenjualan.this, "Hapus Berhasil", Toast.LENGTH_SHORT).show();
                    getTotal();
                    loadCart();
                } catch (Exception e){
                    Toast.makeText(ActivityPenjualan.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    public void bayar(View view) {
        double total=0.0;
        String idorder = ActivityCetak.getText(v, R.id.edtNomorFaktur);

        Cursor cur = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + idorder.substring(3));
        double sum=0.0;
        if (cur.moveToFirst()){
            sum = cur.getDouble(0);
        }
        total=total+sum;

        String faktur = ActivityCetak.getText(v, R.id.edtNomorFaktur);
        Cursor c= db.sq("SELECT * FROM qorderdetail WHERE idorder='"+faktur.substring(3)+"'");
        c.moveToNext();
        isikeranjang=c.getCount();
        c.moveToLast();
        final Intent i = new Intent(this, ActivityPenjualanProses.class);
        i.putExtra("faktur", ActivityTampil.getString(c, "faktur"));
        i.putExtra("bayar", "penjualan");
        i.putExtra("harga", total);
        if (isikeranjang==0){
            Toast.makeText(this, "Masukkan Data Dengan Benar", Toast.LENGTH_SHORT).show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.create();
            builder.setMessage("Apakah Anda Yakin Ingin Menyimpan Pesanan Ini?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    }

    public String convertDate(String date){
        try {
            String[] a = date.split("/") ;
            return a[2]+a[1]+a[0];
        } catch (Exception e){
            return "";
        }
    }

    public static String setDatePickerNormal(int year , int month, int day) {
        String bln,hri ;
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

        return hri+"/"+bln+"/"+String.valueOf(year);
    }

    public static String removeE(double value){
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return numberFormat(df.format(value)) ;
    }

    public static String removeE(String value){
        double hasil = Double.parseDouble(value) ;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return numberFormat(df.format(hasil)) ;
    }

    public static String numberFormat(String number){ // Rp. 1,000,000.00
        try{
            String hasil = "";
            String[] b = number.split("\\.") ;

            if(b.length == 1){
                String[] a = number.split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
            } else {
                String[] a = b[0].split("") ;
                int c=0 ;
                for(int i=a.length-1;i>=0;i--){
                    if(c == 3 && !TextUtils.isEmpty(a[i])){
                        hasil = a[i] + "." + hasil ;
                        c=1;
                    } else {
                        hasil = a[i] + hasil ;
                        c++ ;
                    }
                }
                hasil+=","+b[1] ;
            }
            return  hasil ;
        }catch (Exception e){
            return  "" ;
        }
    }
}

class AdapterPenjualan extends RecyclerView.Adapter<AdapterPenjualan.PenjualanViewHolder> {
    private Context ctxAdapter;
    private ArrayList<String> data;

    public AdapterPenjualan(Context ctxAdapter, ArrayList<String> data) {
        this.ctxAdapter = ctxAdapter;
        this.data = data;
    }

    @NonNull
    @Override
    public PenjualanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_penjualan,viewGroup,false);
        return new PenjualanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenjualanViewHolder viewHolder, int i) {
        final String[] row=data.get(i).split("__");

        String diskon, setelahDsc;
        double subTot = Double.parseDouble(row[7]) * Double.parseDouble(row[8]);
        double tot = subTot - Double.parseDouble(row[6]);

        if (row[6].equals("0")){
            diskon = "";
            setelahDsc = "";
        } else {
            diskon = "Diskon = " + ActivityPenjualan.removeE(row[6]) + "\n";
            setelahDsc = "\nSetelah Diskon = " + ActivityPenjualan.removeE(tot);
        }

        if (row[2].equals("Barang")){
            viewHolder.ukuran.setText(row[3]+"\n"+row[4]+"\n"+"Ukuran : "+row[5]);
        } else {
            viewHolder.ukuran.setVisibility(View.GONE);
        }

        viewHolder.kategori.setText("Jenis : " + row[2]);
        viewHolder.custom.setText(row[1]);
        viewHolder.harga.setText(diskon + row[7]+" x "+ActivityPenjualan.removeE(row[8])+" = "+ActivityPenjualan.removeE(subTot) + setelahDsc);

        viewHolder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Database db = new Database(ctxAdapter);
                AlertDialog.Builder builder = new AlertDialog.Builder(ctxAdapter);
                builder.create();
                builder.setMessage("Apakah Anda Yakin Ingin Menghapusnya?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String q = "DELETE FROM tblorderdetail WHERE idorderdetail="+row[0];
                                if (db.exc(q)){
                                    Toast.makeText(ctxAdapter, "Berhasil", Toast.LENGTH_SHORT).show();
                                    ((ActivityPenjualan)ctxAdapter).getTotal();
                                    ((ActivityPenjualan)ctxAdapter).loadCart();
                                }else {
                                    Toast.makeText(ctxAdapter, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PenjualanViewHolder extends RecyclerView.ViewHolder {
        TextView custom, kategori, ukuran, harga;
        ImageButton hapus;

        public PenjualanViewHolder(@NonNull View itemView) {
            super(itemView);

            custom = (TextView) itemView.findViewById(R.id.tvCustom);
            kategori = (TextView) itemView.findViewById(R.id.tvKat);
            ukuran = (TextView) itemView.findViewById(R.id.tvUkuran);
            harga = (TextView) itemView.findViewById(R.id.tvHarga);
            hapus = (ImageButton) itemView.findViewById(R.id.ibtnHapus);
        }
    }
}
