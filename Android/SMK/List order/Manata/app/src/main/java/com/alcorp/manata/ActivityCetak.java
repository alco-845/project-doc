package com.alcorp.manata;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ActivityCetak extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    Toolbar appbar;
    Config config,temp ;
    Database db ;
    String device,faktur,hasil,type ;
    View v ;
    int flagready = 0 ;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak);
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
        tvTb.setText("Cetak");

        type = getIntent().getStringExtra("type");
        config = new Config(getSharedPreferences("config",this.MODE_PRIVATE));
        temp = new Config(getSharedPreferences("temp",this.MODE_PRIVATE));
        db = new Database(this) ;
        v = this.findViewById(android.R.id.content);

        device = config.getCustom("Printer","");
        faktur = getIntent().getStringExtra("faktur") ;

        if(TextUtils.isEmpty(faktur)){
            Intent i = new Intent(this, ActivityTransaksi.class) ;
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);
        }

        try {
            findBT();
            openBT();

            ConstraintLayout w = (ConstraintLayout) findViewById(R.id.wTeks) ;
            setPreview() ;
            w.setVisibility(View.VISIBLE);
        }catch (Exception e){
            Toast.makeText(this, "Bluetooth Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetConnection() {
        if (mmInputStream != null) {
            try {mmInputStream.close();} catch (Exception e) {}
            mmInputStream = null;
        }

        if (mmOutputStream != null) {
            try {mmOutputStream.close();} catch (Exception e) {}
            mmOutputStream = null;
        }

        if (mmSocket != null) {
            try {mmSocket.close();} catch (Exception e) {}
            mmSocket = null;
        }
    }

    public void copy(View view) {
        Cursor c = db.sq("SELECT * FROM qorder WHERE faktur = '"+faktur+"' ");
        c.moveToNext() ;
        String lunas = ActivityTampil.getString(c,"lunas");
        String dscOrder = ActivityTampil.getString(c,"diskonorder");
        String kurang, dsc;
        if(lunas.equals("1")){
            kurang = getText(v, R.id.tKekurangan);
        } else {
            kurang = getText(v, R.id.tKembali);
        }

        if (dscOrder.equals("0")){
            dsc = "";
        } else {
            dsc = getText(v, R.id.tDiskon)+"\n\n";
        }

        String text = dsc+getText(v, R.id.tbarang) + "\n\n" +
                getText(v, R.id.tValue1) + "\n\n" +
                getText(v, R.id.tTotal) + "\n\n" +
                getText(v, R.id.tBayar) + "\n\n" +
                kurang;

        ClipData clipData = ClipData.newPlainText("text", text);
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Berhasil Copy", Toast.LENGTH_SHORT).show();
    }

    public void setPreview(){
        Cursor identitas = db.sq("SELECT * FROM tblidentitas WHERE ididentitas = 1");
        identitas.moveToNext() ;
        Cursor bayar = db.sq("SELECT * FROM qorder WHERE faktur = '"+faktur+"' ");
        bayar.moveToNext() ;
        Cursor penj = db.sq("SELECT * FROM qorderdetail WHERE faktur = '"+faktur+"' ");

        String toko = ActivityTampil.getString(identitas,"namatoko") ;
        String alamat = ActivityTampil.getString(identitas,"alamattoko") ;
        String telp = ActivityTampil.getString(identitas,"telp") ; ActivityTampil.setText(v,R.id.tHeader,toko+"\n"+alamat+"\n"+telp) ;
        String tfaktur   = "Faktur : "+faktur ; ActivityTampil.setText(v,R.id.tFaktur,tfaktur) ;
        String tgl       = "Tanggal Order : "+ActivityTampil.dateToNormal(ActivityTampil.getString(bayar,"tglorder"));

        String tglselesai       = ActivityTampil.dateToNormal(ActivityTampil.getString(bayar,"tglselesai")) ;
        String lun = ActivityTampil.getString(bayar,"lunas");
        String tgljatuh = "";
        if (tglselesai.equals("Belum Selesai")){
            tgljatuh = "Tanggal Selesai : "+tglselesai;
        } else if (tglselesai != "" && lun.equals("2")){
            tgljatuh = "Tanggal Selesai : "+tglselesai;
        } else if (tglselesai != "" && lun.equals("1")){
            tgljatuh = "Tanggal Jatuh Tempo : "+tglselesai;
        }

        String kategori    = "Kategori : "+ActivityTampil.getString(bayar,"kategori") ;

        String keterangan    = ActivityTampil.getString(bayar,"keterangan") ;
        String ket = "";
        if (keterangan.equals("")){
            ket = "-";
        } else {
            ket = keterangan;
        }

        String pelanggan = "Pelanggan : "+ActivityTampil.getString(bayar,"pelanggan") ; ActivityTampil.setText(v,R.id.tPelanggan,pelanggan+"\nKeterangan : "+ket) ;
        ActivityTampil.setText(v,R.id.tTanggal,tgl+"\n"+tgljatuh+"\n"+kategori) ;

        ImageView ivLogo = findViewById(R.id.ivLogo);
        String logo = String.valueOf(Glide.with(this)
                .load(R.drawable.logo)
                .apply(RequestOptions.overrideOf(55, 55))
                .into(ivLogo));

        String header = logo+"\n"+
                setCenter(toko)+"\n"+
                setCenter(alamat)+"\n"+
                setCenter(telp)+"\n"+
                "\n"+
                tfaktur+"\n"+
                tgl+"\n"+
                tgljatuh+"\n"+
                kategori+"\n"+
                pelanggan+"\nKeterangan : "+
                ket+"\n"+
                getStrip();

        String body = "" ;
        String view = "" ;
        while(penj.moveToNext()){
            String barang = ActivityTampil.getString(penj,"barang") ;
            String jenis = ActivityTampil.getString(penj,"jenis") ;
            String ukuran = ActivityTampil.getString(penj,"ukuran") ;
            String warna = ActivityTampil.getString(penj,"warna") ;
            String variasi = ActivityTampil.getString(penj,"variasi") ;
            String jumlah = ActivityTampil.getString(penj,"jumlah") ;
            String harga = ActivityTampil.getString(penj,"harga") ;
            String diskonitem = ActivityTampil.getString(penj,"diskonitem") ;

            double subtotal = Double.parseDouble(jumlah)*Double.parseDouble(harga) ;
            double total = subtotal - Double.parseDouble(diskonitem) ;

            String kat = "", dscItem = "", dscHarga = "";

            if (jenis.equals("Jasa")){
                kat = "";
            } else {
                kat = "Variasi : "+variasi+"\n"+warna+"\nUkuran : "+ukuran+"\n";
            }

            if (diskonitem.equals("0")){
                dscHarga = "";
                dscItem = " = "+ActivityPenjualan.removeE(subtotal)+"\n";
            } else {
                dscHarga = "Diskon = "+ActivityPenjualan.removeE(diskonitem)+"\n";
                dscItem = " = "+ActivityPenjualan.removeE(subtotal)+"\n"+
                        "Setelah Diskon = "+ActivityPenjualan.removeE(total)+"\n";
            }

            body+=  "\n"+barang+"\n"+
                    kat+
                    dscHarga+
                    jumlah+" x "+ ActivityPenjualan.removeE(harga)+"\n"+
                    dscItem;
            view+=  "\n"+barang+"\n"+
                    kat+
                    dscHarga+
                    jumlah+" x "+ActivityPenjualan.removeE(harga)+
                    dscItem;
        }
        ActivityTampil.setText(v,R.id.tbarang,view) ;
        body+=getStrip() ;

        double total=0.0;
        Cursor cur = db.sq("SELECT SUM(harga*jumlah-diskonitem) FROM tblorderdetail WHERE idorder=" + faktur.substring(3));
        double sum=0.0;
        if (cur.moveToFirst()){
            sum = cur.getDouble(0);
        }
        total=total+sum;

        String dscOrder = ActivityTampil.getString(bayar,"diskonorder");
        double dsc = total - Double.parseDouble(dscOrder) ;
        String jumlahbayar = "", diskon = "";
        TextView tTotal = findViewById(R.id.tTotal);
        TextView tDiskon = findViewById(R.id.tDiskon);
        TextView tValue1 = findViewById(R.id.tValue1);

        if (dscOrder.equals("0")){
            jumlahbayar = "Total : " + ActivityPenjualan.removeE(total);
            diskon = "";
            tTotal.setVisibility(View.GONE);
            tDiskon.setVisibility(View.GONE);
            tValue1.setPadding(0, 16, 0, 0);
        } else {
            jumlahbayar = "Sub Total : " + ActivityPenjualan.removeE(total);
            diskon = "Total : " + ActivityPenjualan.removeE(dsc);
            tDiskon.setText("Diskon : " + ActivityPenjualan.removeE(dscOrder));
        }
        ActivityTampil.setText(v,R.id.tValue1,jumlahbayar);
        ActivityTampil.setText(v,R.id.tTotal,diskon) ;

        String dibayar = "Bayar : " + ActivityPenjualan.removeE(ActivityTampil.getString(bayar,"bayar")) ; ActivityTampil.setText(v,R.id.tBayar,dibayar) ;
        String caption =  ActivityTampil.getString(identitas,"cap1") ;
        String caption2 = ActivityTampil.getString(identitas,"cap2") ;
        String caption3 = ActivityTampil.getString(identitas,"cap3") ; ActivityTampil.setText(v,R.id.tCaption,caption+"\n"+caption2+"\n"+caption3) ;


        TextView tKembali, tKurang;
        tKembali = findViewById(R.id.tKembali);
        tKurang = findViewById(R.id.tKekurangan);
        String kembali = "", kurang = "";
        String lunas = ActivityTampil.getString(bayar,"lunas");
        if(lunas.equals("1")){
            kembali = "Kembali : -"+  ActivityPenjualan.removeE(ActivityTampil.getString(bayar,"kembali"));
            kurang = "Kekurangan : "+  ActivityPenjualan.removeE(ActivityTampil.getString(bayar,"kurang"));

            tKembali.setVisibility(View.GONE);
        } else {
            kembali = "Kembali : "+  ActivityPenjualan.removeE(ActivityTampil.getString(bayar,"kembali"));
            kurang = "Kekurangan : -"+  ActivityPenjualan.removeE(ActivityTampil.getString(bayar,"kurang"));

            tKurang.setVisibility(View.GONE);
        }

        ActivityTampil.setText(v,R.id.tKembali,kembali) ;
        ActivityTampil.setText(v,R.id.tKekurangan,kurang);

        String footer =  setRight(jumlahbayar)+"\n"+
                setRight(dibayar)+"\n"+
                setRight(kembali)+"\n"+
                setRight(kurang)+"\n\n"+
                setCenter(caption)+"\n"+
                setCenter(caption2)+"\n"+
                setCenter(caption3) ;


        hasil = header+body+footer ;
    }

    public static String setRight(String item){
        int leng = item.length() ;
        String hasil = "" ;
        for(int i=0 ; i<32-leng;i++){
            if((31-leng) == i){
                hasil += item ;
            } else {
                hasil += " " ;
            }
        }
        return hasil ;
    }

    public static String setRightTotal(String item){
        int leng = item.length() ;
        String hasil = "" ;
        for(int i=0 ; i<44-leng;i++){
            if((31-leng) == i){
                hasil += item ;
            } else {
                hasil += " " ;
            }
        }
        return hasil ;
    }

    public static String setRight(String item,int min){
        int leng = item.length() ;
        int total = 32-min ;
        String hasil = "" ;
        for(int i=0 ; i<total-leng;i++){
            if((total-leng) == i){
                hasil += item ;
            } else {
                hasil += " " ;
            }
        }
        return hasil ;
    }

    public static String getStrip(){
        String a = "" ;
        for(int i = 0 ; i < 32 ; i++){
            a+="-" ;
        }
        return a+"\n" ;
    }

    public static String setCenter(String item){
        int leng = item.length() ;
        String hasil = "" ;
        for(int i=0 ; i<32-leng;i++){
            if((32-leng)/2+1 == i){
                hasil += item ;
            } else {
                hasil += " " ;
            }
        }
        return hasil ;
    }

    public static String getText(View v, int id){
        TextView a = (TextView) v.findViewById(id) ;
        return a.getText().toString() ;
    }

    public void cari(View view){
        Intent i = new Intent(this,ActivityCetakCari.class) ;
        i.putExtra("faktur",faktur) ;
        startActivity(i);
    }

    public void cetak(View view) throws IOException {
        try {
            if(getText(v,R.id.ePrinter).equals("Tidak Ada Perangkat")){
                Toast.makeText(this, "Tidak ada Printer", Toast.LENGTH_SHORT).show();
            } else if (flagready == 1){
                try {
                    setPreview();
                }catch (Exception e){
                    Toast.makeText(this, "Preview Gagal", Toast.LENGTH_SHORT).show();
                }
                sendData(hasil);

            } else {
                Toast.makeText(this, "Printer belum siap", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Proses Cetak Gagal, Harap periksa Printer atau bluetooth anda", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (type.equals("bayar")){
            Intent i = new Intent(this, ActivityTransaksi.class) ;
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);
        } else if (type.equals("laporan")){
            Intent i = new Intent(this, ActivityLaporan.class) ;
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            startActivity(i);
        }

    }

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(this, "Tidak ada Bluetooth Adapter", Toast.LENGTH_SHORT).show();
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                ActivityTampil.setText(v,R.id.ePrinter,"Printer Belum Dipilih") ;
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(this.device)) {
                        mmDevice = device;
                        ActivityTampil.setText(v,R.id.ePrinter,this.device) ;
                        break;
                    }
                }
            } else {
                ActivityTampil.setText(v,R.id.ePrinter,"Tidak Ada Perangkat") ;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void openBT() throws IOException {
        try {
            resetConnection();
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            //00001101-0000-1000-8000-00805F9B34FB
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            ConstraintLayout c = (ConstraintLayout) findViewById(R.id.simbol) ;
            final int sdk = Build.VERSION.SDK_INT;
            if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
                c.setBackgroundDrawable( getResources().getDrawable(R.drawable.ovalgreen) );
            } else {
                c.setBackground( getResources().getDrawable(R.drawable.ovalgreen));
            }
            flagready = 1 ;
//            Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal tersambung dengan printer", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal tersambung dengan printer", Toast.LENGTH_SHORT).show();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // This is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted()
                            && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length);
                                        final String data = new String(
                                                encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            public void run() {
                                                Toast.makeText(ActivityCetak.this, data, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData(String hasil) throws IOException {
        try {
            hasil += "\n\n\n";
            mmOutputStream.write(hasil.getBytes());mBluetoothAdapter.cancelDiscovery() ; mmSocket.close();

            resetConnection();
            Toast.makeText(this, "Print Berhasil", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}