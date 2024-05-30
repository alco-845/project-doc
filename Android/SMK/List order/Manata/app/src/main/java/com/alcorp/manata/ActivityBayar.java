package com.alcorp.manata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static com.alcorp.manata.ActivityPenjualan.setDatePickerNormal;

public class ActivityBayar extends AppCompatActivity {

    Toolbar appbar;
    View v;
    Database db;
    ArrayList arrayList = new ArrayList();
    String dari, ke;
    Calendar calendar;
    int year, month, day;
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
        tvTb.setText("Bayar DP");

        v = this.findViewById(android.R.id.content);
        db = new Database(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        eCari = (EditText) findViewById(R.id.eCari);
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
                loadList(eCari.getText().toString(), "");
            }
        });

        setText();
        loadList("", "p");

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.wInfo);
        constraintLayout.setVisibility(View.GONE);

        Spinner spinner = (Spinner) findViewById(R.id.spItems);
        spinner.setVisibility(View.GONE);
    }

    public void setText() {
        dari = ActivityTampil.setDatePicker(year, month + 1, day);
        ke = ActivityTampil.setDatePicker(year, month + 1, day);
        String now = setDatePickerNormal(year, month + 1, day);
        ActivityTampil.setText(v, R.id.eKe, now);
        ActivityTampil.setText(v, R.id.eDari, now);
    }

    public void loadList(String cari, String isi) {
        String q;

        if(cari != "" && isi.equals("")){
            q = "SELECT * FROM qorder WHERE lunas = 1 AND (metode = 'Tunai DP' OR metode = 'Cicil') AND pelanggan LIKE '%"+cari+"%' ORDER BY tglselesai DESC";
        } else if(cari.equals("") && isi != "") {
            q = "SELECT * FROM qorder WHERE lunas = 1 AND (metode = 'Tunai DP' OR metode = 'Cicil') ORDER BY tglselesai DESC";
        } else {
            q = "SELECT * FROM qorder WHERE lunas = 1 AND (metode = 'Tunai DP' OR metode = 'Cicil') AND pelanggan LIKE '%"+cari+"%' AND tglselesai BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglselesai DESC";
        }

        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recItem) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterBayarList(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q) ;
        if(c.getCount() > 0){
            while(c.moveToNext()){
                String idorder = ActivityTampil.getString(c,"idorder") ;
                String faktur = ActivityTampil.getString(c,"faktur") ;
                String pelanggan = ActivityTampil.getString(c,"pelanggan") ;
                String tglorder = ActivityTampil.getString(c,"tglorder") ;
                String kurang = ActivityTampil.getString(c, "kurang");
                String tglselesai = ActivityTampil.getString(c,"tglselesai") ;

                String campur = idorder+"__"+faktur+"__"+pelanggan+"__"+ActivityTampil.dateToNormal(tglorder)+"__"+kurang+"__"+ActivityTampil.dateToNormal(tglselesai);
                arrayList.add(campur);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList("", "p");
    }

    public void dateDari(View view){
        setDate(1);
    }
    public void dateKe(View view){
        setDate(2);
    }

    public void filtertgl(){
        loadList("", "");
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
            ActivityTampil.setText(v, R.id.eDari, setDatePickerNormal(thn,bln+1,day)) ;
            dari = ActivityTampil.setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };

    private DatePickerDialog.OnDateSetListener edit2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int thn, int bln, int day) {
            ActivityTampil.setText(v, R.id.eKe, setDatePickerNormal(thn,bln+1,day)) ;
            ke = ActivityTampil.setDatePicker(thn,bln+1,day) ;
            filtertgl();
        }
    };
    //end date time picker
}

class AdapterBayarList extends RecyclerView.Adapter<AdapterBayarList.ViewHolder> {
    private ArrayList<String> data;
    Context c;

    public AdapterBayarList(Context a, ArrayList<String> kota) {
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

        viewHolder.nama.setText(row[1]);
        viewHolder.alamat.setText("Nama : "+row[2]+"\nTanggal Order : "+row[3]+"\nTanggal Jatuh Tempo : "+row[5]);
        viewHolder.telp.setText("Kekurangan : "+ ActivityPenjualan.removeE(row[4]));
        viewHolder.opt.setVisibility(View.GONE);
        viewHolder.click.setTag(row[0]);

        viewHolder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, ActivityPenjualanProses.class);
                intent.putExtra("faktur", row[1]);
                intent.putExtra("bayar", "hutang");
                intent.putExtra("tanggal", row[5]);
                c.startActivity(intent);
            }
        });
    }
}