package com.alcorp.manata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityLaporanKeuangan extends AppCompatActivity {

    Toolbar appbar;
    View v;
    Database db;
    ArrayList arrayList = new ArrayList();
    String dari, ke;
    Calendar calendar;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_keuangan);
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
        tvTb.setText("Laporan Keuangan");

        v = this.findViewById(android.R.id.content);
        db = new Database(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        final EditText eCari = (EditText) findViewById(R.id.eCari);
        eCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrayList.clear();
                String a = eCari.getText().toString();
                getKeuangan(a, "");
            }
        });
        setText();
        getKeuangan("", "p");

        Spinner spinner = (Spinner) findViewById(R.id.spKeuangan) ;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getKeuangan(ActivityCetak.getText(v, R.id.eCari), "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setText() {
        dari = ActivityTampil.setDatePicker(year, month + 1, day);
        ke = ActivityTampil.setDatePicker(year, month + 1, day);
        String now = ActivityPenjualan.setDatePickerNormal(year, month + 1, day);
        ActivityTampil.setText(v, R.id.eKe, now);
        ActivityTampil.setText(v, R.id.eDari, now);
    }

    public void clearText() {
        String saldo;
        Cursor c = db.sq("SELECT * FROM tbltransaksi");
        c.moveToNext();
        ActivityTampil.setText(v, R.id.teJumlahData, "Jumlah Data : " + String.valueOf(c.getCount()));

        Cursor cur = db.sq("SELECT * FROM tbltransaksi");
        cur.moveToLast();

        if (c.getCount()==0){
            ActivityTampil.setText(v, R.id.teValue, "Rp. 0");
        } else {
            saldo = ActivityTampil.getString(cur, "saldo");
            String j = ActivityPenjualan.removeE(saldo);
            ActivityTampil.setText(v, R.id.teValue, "Rp. " + j);
        }
    }

    public void getKeuangan(String cari, String isi){
        arrayList.clear();
        String item = ActivityTampil.getSpinnerItem(v,R.id.spKeuangan) ;
        String q="";
        if (cari != "" && isi.equals("") && item.equals("Semua")) {
            q = "SELECT * FROM tbltransaksi WHERE fakturtransaksi LIKE '%" + cari + "%' ORDER BY idtransaksi ASC";
        } else if (cari.equals("") && isi != "" && item.equals("Semua")) {
            q = "SELECT * FROM tbltransaksi ORDER BY idtransaksi ASC";
        } else if (cari.equals("") && isi.equals("") && item.equals("Semua")) {
            q = "SELECT * FROM tbltransaksi WHERE fakturtransaksi LIKE '%" + cari + "%' AND tgltransaksi BETWEEN '" + dari + "' AND '" + ke + "' ORDER BY idtransaksi ASC";

        } else if (cari != "" && isi.equals("") && item.equals("Pemasukan")){
            q = "SELECT * FROM tbltransaksi WHERE status=0 AND fakturtransaksi LIKE '%" + cari + "%' ORDER BY idtransaksi ASC";
        } else if (cari.equals("") && isi != "" && item.equals("Pemasukan")){
            q = "SELECT * FROM tbltransaksi WHERE status=0 ORDER BY  ASC";
        } else if (cari.equals("") && isi.equals("") && item.equals("Pemasukan")){
            q = "SELECT * FROM tbltransaksi WHERE status=0 AND fakturtransaksi LIKE '%" + cari + "%' AND tgltransaksi BETWEEN '" + dari + "' AND '" + ke + "' ORDER BY idtransaksi ASC";

        } else if (cari != "" && isi.equals("") && item.equals("Pengeluaran")) {
            q = "SELECT * FROM tbltransaksi WHERE status=1 AND fakturtransaksi LIKE '%" + cari + "%' ORDER BY idtransaksi ASC";
        } else if (cari.equals("") && isi != "" && item.equals("Pengeluaran")) {
            q = "SELECT * FROM tbltransaksi WHERE status=1 ORDER BY idtransaksi ASC";
        } else if (cari.equals("") && isi.equals("") && item.equals("Pengeluaran")) {
            q = "SELECT * FROM tbltransaksi WHERE status=1 AND fakturtransaksi LIKE '%" + cari + "%' AND tgltransaksi BETWEEN '" + dari + "' AND '" + ke + "' ORDER BY idtransaksi ASC";
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recKeuangan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterKeuangan(this, arrayList);
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q);
        if (c.getCount() > 0){
            ActivityTampil.setText(v,R.id.teJumlahData,"Jumlah Data : "+String.valueOf(c.getCount())) ;
            while (c.moveToNext()) {
                String tgl = ActivityTampil.getString(c, "tgltransaksi");
                String notrans = ActivityTampil.getString(c, "notransaksi");
                String faktur = ActivityTampil.getString(c, "fakturtransaksi");
                String ket = ActivityTampil.getString(c, "keterangantransaksi");
                String hmasuk = ActivityTampil.getString(c, "masuk");
                String hkeluar = ActivityTampil.getString(c, "keluar");
                String stat = ActivityTampil.getString(c, "status");

                String campur = notrans+"__"+ActivityTampil.dateToNormal(tgl)+"__"+faktur +"__"+"Harga Masuk : "+ActivityPenjualan.removeE(hmasuk)+"__"+"Harga Keluar : "+ActivityPenjualan.removeE(hkeluar)+"__"+stat+"__"+ket;
                arrayList.add(campur);
            }
            Cursor cur = db.sq("SELECT * FROM tbltransaksi");
            cur.moveToLast();
            String saldo = ActivityTampil.getString(cur, "saldo");
            String j = ActivityPenjualan.removeE(saldo);
            ActivityTampil.setText(v, R.id.teValue, "Rp. "+j);
        } else {
            ActivityTampil.setText(v, R.id.teValue, "Rp. 0");
            ActivityTampil.setText(v,R.id.teJumlahData, "Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
    }

    public void dateDari(View view) {
        setDate(1);
    }

    public void dateKe(View view) {
        setDate(2);
    }

    public void filtertgl() {
        getKeuangan("", "");
    }

    //start date time picker
    public void setDate(int i) {
        showDialog(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this, edit1, year, month, day);
        } else if (id == 2) {
            return new DatePickerDialog(this, edit2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener edit1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ActivityTampil.setText(v, R.id.eDari, ActivityPenjualan.setDatePickerNormal(thn, bln + 1, day));
            dari = ActivityTampil.setDatePicker(thn, bln + 1, day);
            filtertgl();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ActivityTampil.setText(v, R.id.eKe, ActivityPenjualan.setDatePickerNormal(thn, bln + 1, day));
            ke = ActivityTampil.setDatePicker(thn, bln + 1, day);
            filtertgl();
        }
    };
    //end date time picker
}

class AdapterKeuangan extends RecyclerView.Adapter<AdapterKeuangan.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterKeuangan(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_laporan_keuangan, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tanggal,harga,notrans, faktur, ket;
        ImageView hapus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            harga = (TextView) itemView.findViewById(R.id.teHarga);
            tanggal = (TextView) itemView.findViewById(R.id.teTanggal);
            notrans = (TextView) itemView.findViewById(R.id.teNoTrans);
            faktur = (TextView) itemView.findViewById(R.id.teFaktur);
            ket = (TextView) itemView.findViewById(R.id.teKet);
            hapus = (ImageView) itemView.findViewById(R.id.ivDelete);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final String[] row = data.get(i).split("__");
        final Database db = new Database(c);
        String stat;

        viewHolder.notrans.setText("No. Transaksi : "+row[0]);
        viewHolder.tanggal.setText(row[1]);
        viewHolder.faktur.setText(row[2]);
        if (row[5].equals("0")){
            stat = row[3];
        } else {
            stat = row[4];
        }
        viewHolder.harga.setText(stat);
        viewHolder.ket.setText("Keterangan : "+row[6]);
        Cursor cur = db.sq("SELECT * FROM tbltransaksi");
        if (cur.getCount()>0) {
            cur.moveToLast();
            String last = ActivityTampil.getString(cur, "notransaksi");
            if (row[0].equals(last)){
                viewHolder.hapus.setVisibility(View.VISIBLE);
            }else {
                viewHolder.hapus.setVisibility(View.GONE);
            }
        } else {
            viewHolder.hapus.setVisibility(View.VISIBLE);
        }
        viewHolder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.create();
                builder.setMessage("Apakah Anda Yakin Ingin Menghapusnya?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String q = "DELETE FROM tbltransaksi WHERE notransaksi=" + row[0];
                                if (db.exc(q)) {
                                    Toast.makeText(c, "Berhasil", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                    ((ActivityLaporanKeuangan) c).clearText();
                                    data.remove(i);
                                } else {
                                    Toast.makeText(c, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }
}