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
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.alcorp.manata.ActivityPenjualan.setDatePickerNormal;
import static com.alcorp.manata.ActivityTampil.dateToNormal;

public class ActivityKirim extends AppCompatActivity {

    Toolbar appbar;
    View v ;
    Database db ;
    ArrayList arrayList = new ArrayList();
    String dari, ke;
    Calendar calendar ;
    int year,month, day ;
    EditText eCari;
    TextView tvJumlah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim);
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
        tvTb.setText("Sudah Dikirim");

        v = this.findViewById(android.R.id.content);
        db = new Database(this) ;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        tvJumlah = (TextView) findViewById(R.id.tTotal) ;
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
                arrayList.clear();
                loadList(eCari.getText().toString(), "");
            }
        });

        setText();
        loadList("", "p");
    }

    public void setText(){
        dari = ActivityTampil.setDatePicker(year,month+1,day) ;
        ke = ActivityTampil.setDatePicker(year,month+1,day) ;
        String now = setDatePickerNormal(year,month+1,day) ;
        ActivityTampil.setText(v,R.id.eKe, now) ;
        ActivityTampil.setText(v,R.id.eDari, now) ;
    }

    public void loadList(String cari, String isi){
        String q;

        if(cari != "" && isi.equals("")){
            q = "SELECT * FROM qorder WHERE kirim=2 AND status = 'Terkirim' AND pelanggan LIKE '%"+cari+"%' ORDER BY tglselesai DESC";
        } else if(cari.equals("") && isi != "") {
            q = "SELECT * FROM qorder WHERE kirim=2 AND status = 'Terkirim' ORDER BY tglselesai DESC";
        } else {
            q = "SELECT * FROM qorder WHERE kirim=2 AND status = 'Terkirim' AND pelanggan LIKE '%"+cari+"%' AND tglselesai BETWEEN '"+dari+"' AND '"+ke+"' ORDER BY tglselesai DESC";
        }

        arrayList.clear();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recList) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.Adapter adapter = new AdapterKirimList(this,arrayList) ;
        recyclerView.setAdapter(adapter);
        Cursor c = db.sq(q) ;
        if(c.getCount() > 0){
            while(c.moveToNext()){
                String faktur = ActivityTampil.getString(c, "faktur");
                String pelanggan = ActivityTampil.getString(c,"pelanggan") ;
                String alamat = ActivityTampil.getString(c,"alamat") ;
                String telp = ActivityTampil.getString(c,"telp") ;
                String keterangan = ActivityTampil.getString(c,"keterangan") ;
                String tglorder = ActivityTampil.getString(c,"tglorder") ;
                String tglselesai = ActivityTampil.getString(c,"tglselesai") ;
                String stat = ActivityTampil.getString(c, "status");
                String metode = ActivityTampil.getString(c, "metode");
                String diskonOrder = ActivityTampil.getString(c, "diskonorder");
                String total = ActivityTampil.getString(c, "total");
                String bayar = ActivityTampil.getString(c, "bayar");
                String kembali = ActivityTampil.getString(c, "kembali");
                String kurang = ActivityTampil.getString(c, "kurang");
                String lunas = ActivityTampil.getString(c, "lunas");
                String kat = ActivityTampil.getString(c, "kategori");

                String campur = faktur+"__"+pelanggan+"__"+alamat+"__"+telp+"__"+keterangan+"__"+dateToNormal(tglorder)+"__" +dateToNormal(tglselesai)+"__" +stat+"__"+kat+"__"+metode+"__" +total+"__"+ActivityPenjualan.removeE(bayar)+"__" +ActivityPenjualan.removeE(kembali)+"__" +lunas+"__" +ActivityPenjualan.removeE(kurang)+"__" +diskonOrder;
                arrayList.add(campur);
            }
            tvJumlah.setText("Jumlah Data : "+String.valueOf(c.getCount()));
        } else {
            tvJumlah.setText("Jumlah Data : 0");
        }
        adapter.notifyDataSetChanged();
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

class AdapterKirimList extends RecyclerView.Adapter<AdapterKirimList.ViewHolder> {
    private ArrayList<String> data;
    Context c;
    Config config;

    public AdapterKirimList(Context a, ArrayList<String> kota) {
        this.data = kota;
        c = a;
        config = new Config(c.getSharedPreferences("config",c.MODE_PRIVATE));
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

        viewHolder.nama.setText("Nama : "+row[1]);
        viewHolder.alamat.setText("Tanggal : "+row[5] + " - "+row[6]);
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

        viewHolder.opt.setText("x");
        viewHolder.opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Database db = new Database(c);
                        if (db.deleteOr(Integer.valueOf(row[0]))){
                            data.remove(i);
                            notifyDataSetChanged();
                            ((ActivityKirim)c).loadList("", "p");
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
