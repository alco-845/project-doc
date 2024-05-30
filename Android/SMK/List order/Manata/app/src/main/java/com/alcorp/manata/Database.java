package com.alcorp.manata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public static final String nama_database="db_data";
    public static final int versi_database=1;
    SQLiteDatabase db;
    Context a;

    public Database(Context context){
        super(context, nama_database, null, versi_database);
        db = this.getWritableDatabase();
        a = context;
        cektbl();
    }

    public boolean cektbl(){
        // create tabel order
        exc("CREATE TABLE IF NOT EXISTS `tblorder` (\n" +
                "\t`idorder`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`idpelanggan`\tINTEGER,\n" +
                "\t`idkategori`\tINTEGER,\n" +
                "\t`faktur`\tTEXT,\n" +
                "\t`tglorder`\tINTEGER,\n" +
                "\t`tglselesai`\tINTEGER DEFAULT 0,\n" +
                "\t`keterangan`\tTEXT,\n" +
                "\t`diskonorder`\tREAL DEFAULT 0,\n" +
                "\t`total`\tREAL,\n" +
                "\t`bayar`\tREAL DEFAULT 0,\n" +
                "\t`kembali`\tREAL,\n" +
                "\t`kurang`\tREAL DEFAULT 0,\n" +
                "\t`status`\tTEXT,\n" +
                "\t`kirim`\tTEXT,\n" +
                "\t`metode`\tTEXT,\n" +
                "\t`lunas`\tTEXT,\n" +
                "\t FOREIGN KEY(`idpelanggan`) REFERENCES `tblpelanggan`(`idpelanggan`) ON UPDATE CASCADE ON DELETE RESTRICT, \n" +
                "\t FOREIGN KEY(`idkategori`) REFERENCES `tblkategori`(`idkategori`) ON UPDATE CASCADE ON DELETE RESTRICT \n" +
                ");");

        // create tabel orderdetail
        exc("CREATE TABLE IF NOT EXISTS `tblorderdetail` (\n" +
                "\t`idorderdetail`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`idorder`\tINTEGER, \n" +
                "\t`idbarang`\tINTEGER, \n" +
                "\t`diskonitem`\tREAL DEFAULT 0,\n" +
                "\t`harga`\tREAL,\n" +
                "\t`jumlah`\tREAL,\n" +
                "\t FOREIGN KEY(`idorder`) REFERENCES `tblorder`(`idorder`) ON UPDATE CASCADE ON DELETE RESTRICT, \n" +
                "\t FOREIGN KEY(`idbarang`) REFERENCES `tblbarang`(`idbarang`) ON UPDATE CASCADE ON DELETE RESTRICT \n" +
                ");");

        // create tabel kategori
        exc("CREATE TABLE IF NOT EXISTS `tblkategori` (\n" +
                "\t`idkategori`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`kodekategori`\tTEXT,\n" +
                "\t`kategori`\tTEXT\n" +
                ");");

        // create tabel barang
        exc("CREATE TABLE IF NOT EXISTS `tblbarang` (\n" +
                "\t`idbarang`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`barang`\tTEXT,\n" +
                "\t`jenis`\tTEXT,\n" +
                "\t`warna`\tTEXT,\n" +
                "\t`variasi`\tTEXT,\n" +
                "\t`ukuran`\tTEXT\n" +
                ");");

        // create tabel pelanggan
        exc("CREATE TABLE IF NOT EXISTS `tblpelanggan` (\n" +
                "\t`idpelanggan`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`pelanggan`\tTEXT,\n" +
                "\t`alamat`\tTEXT,\n" +
                "\t`telp`\tTEXT\n" +
                ");");

        // create tabel request
        exc("CREATE TABLE IF NOT EXISTS `tblrequest` (\n" +
                "\t`idrequest`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`nama`\tTEXT,\n" +
                "\t`request`\tTEXT\n" +
                ");");

        //create tabel Identitas
        exc("CREATE TABLE IF NOT EXISTS `tblidentitas` (\n" +
                "\t`ididentitas` \tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`namatoko` \tTEXT,\n" +
                "\t`alamattoko` \tTEXT,\n" +
                "\t`telp` \tTEXT,\n" +
                "\t`cap1` \tTEXT,\n" +
                "\t`cap2` \tTEXT,\n" +
                "\t`cap3` \tTEXT\n" +
                ");");

        //create tabel transaksi
        exc("CREATE TABLE IF NOT EXISTS `tbltransaksi` (\n" +
                "\t`idtransaksi`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`tgltransaksi`\tINTEGER,\n" +
                "\t`notransaksi`\tINTEGER,\n" +
                "\t`fakturtransaksi`\tTEXT,\n" +
                "\t`keterangantransaksi`\tTEXT DEFAULT 0,\n" +
                "\t`masuk`\tREAL DEFAULT 0,\n" +
                "\t`keluar`\tREAL DEFAULT 0,\n" +
                "\t`saldo`\tREAL DEFAULT 0,\n" +
                "\t`status` \tTEXT\n" +
                ");");

        //create view
//        exc("CREATE VIEW qbarang as SELECT tblbarang.idbarang, tblbarang.idkategori, tblbarang.barang, tblbarang.jenis, tblbarang.warna, tblbarang.variasi, tblbarang.ukuran, tblkategori.kodekategori, tblkategori.kategori FROM tblbarang INNER JOIN tblkategori ON tblbarang.idkategori = tblkategori.idkategori");

        exc("CREATE VIEW qorder as SELECT tblorder.idorder, tblorder.idpelanggan, tblorder.idkategori, tblorder.faktur, tblorder.tglorder, tblorder.tglselesai, tblorder.keterangan, tblorder.diskonorder, tblorder.total, tblorder.bayar, tblorder.kembali, tblorder.kurang, tblorder.status, tblorder.kirim, tblorder.metode, tblorder.lunas, tblkategori.kodekategori, tblkategori.kategori, tblpelanggan.pelanggan, tblpelanggan.alamat, tblpelanggan.telp FROM (tblkategori INNER JOIN tblorder ON tblkategori.idkategori = tblorder.idkategori) INNER JOIN tblpelanggan ON tblorder.idpelanggan = tblpelanggan.idpelanggan");

        exc("CREATE VIEW qorderdetail as SELECT tblorderdetail.idorderdetail, tblorderdetail.idorder, tblorderdetail.idbarang, tblorderdetail.diskonitem, tblorderdetail.harga, tblorderdetail.jumlah, tblbarang.barang, tblbarang.jenis, tblbarang.warna, tblbarang.variasi, tblbarang.ukuran, tblorder.faktur, tblorder.tglorder, tblorder.tglselesai, tblorder.keterangan, tblorder.diskonorder, tblorder.total, tblorder.bayar, tblorder.kembali, tblorder.kurang, tblorder.status, tblorder.kirim, tblorder.metode, tblorder.lunas FROM (tblbarang INNER JOIN tblorderdetail ON tblbarang.idbarang = tblorderdetail.idbarang) INNER JOIN tblorder ON tblorderdetail.idorder = tblorder.idorder");

        exc("CREATE VIEW qlist as SELECT tblorder.idorder, tblorder.idpelanggan, tblorder.idkategori, tblorderdetail.idbarang, tblorder.faktur, tblorder.tglorder, tblorder.tglselesai, tblorder.keterangan, tblorder.diskonorder, tblorder.total, tblorder.bayar, tblorder.kembali, tblorder.kurang, tblorder.status, tblorder.kirim, tblorder.metode, tblorder.lunas, tblorderdetail.diskonitem, tblorderdetail.harga, tblorderdetail.jumlah, tblpelanggan.pelanggan, tblpelanggan.alamat, tblpelanggan.telp, tblkategori.kodekategori, tblkategori.kategori, tblbarang.barang, tblbarang.jenis, tblbarang.warna, tblbarang.variasi, tblbarang.ukuran FROM tblbarang, ((tblkategori INNER JOIN tblorder ON tblkategori.idkategori = tblorder.idkategori) INNER JOIN tblorderdetail ON tblorder.idorder = tblorderdetail.idorder) INNER JOIN tblpelanggan ON tblorder.idpelanggan = tblpelanggan.idpelanggan");

        //create trigger
        //create trigger kurang_total
        exc("CREATE TRIGGER kurang_total AFTER DELETE ON tblorderdetail FOR EACH ROW BEGIN UPDATE tblorder SET total= total - (OLD.harga * OLD.jumlah) WHERE idorder = OLD.idorder; END");

        //create trigger tambah_total
        exc("CREATE TRIGGER tambah_total AFTER INSERT ON tblorderdetail FOR EACH ROW BEGIN UPDATE tblorder SET total= total + (NEW.harga * NEW.jumlah) WHERE idorder = NEW.idorder; END");

        //insert Identitas
        exc("INSERT INTO tblidentitas VALUES (1, 'Mr_Custom.id','Sidoarjo','0823 3987 4581','Harap Simpan Struk Ini','Sebagai Bukti Order','Terima Kasih')");
        return true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +nama_database);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.disableWriteAheadLogging();
        }
    }

    public boolean exc(String query){
        try {
            db.execSQL(query);
            return true ;
        } catch (Exception e){
            return false ;
        }
    }

    public Cursor sq(String query){
        try {
            Cursor cursor = db.rawQuery(query, null);
            return cursor;
        } catch (Exception e){
            return null ;
        }
    }

    // Penjualan
    public boolean insertOrder(String faktur, String idpelanggan, String idkategori, String tgl, String total, String keterangan, String kirim, String status){
        ContentValues cv= new ContentValues();
        cv.put("faktur", faktur );
        cv.put("idpelanggan", idpelanggan );
        cv.put("idkategori", idkategori );
        cv.put("tglorder", tgl);
        cv.put("total", total );
        cv.put("keterangan", keterangan );
        cv.put("kirim", kirim );
        cv.put("status", status );
        long result= db.insert("tblorder", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public boolean insertOrderDetail1(Integer idorder, String idbarang, String diskonitem, String harga, String jumlah){
        ContentValues cv= new ContentValues();
        cv.put("idorder", idorder );
        cv.put("idbarang", idbarang );
        cv.put("diskonitem", diskonitem );
        cv.put("harga", harga );
        cv.put("jumlah", jumlah );
        long result= db.insert("tblorderdetail", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean deleteOr(Integer idorder){
        if (db.delete("tblorder","idorder= ?",new String[]{String.valueOf(idorder)})==-1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean updateOrder1(Integer idorder, String faktur, String idpelanggan, String idkategori, String tgl, String keterangan, String kirim, String status){
        ContentValues cv= new ContentValues();
        cv.put("faktur", faktur );
        cv.put("idpelanggan", idpelanggan );
        cv.put("idkategori", idkategori );
        cv.put("tglorder", tgl);
        cv.put("keterangan", keterangan );
        cv.put("kirim", kirim );
        cv.put("status", status );
        long result = db.update("tblorder", cv, "idorder=?", new String[]{String.valueOf(idorder)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateOrder2(Integer idorder, String tgl, String kirim, String status){
        ContentValues cv= new ContentValues();
        cv.put("tglselesai", tgl);
        cv.put("kirim", kirim);
        cv.put("status", status );
        long result = db.update("tblorder", cv, "idorder=?", new String[]{String.valueOf(idorder)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Request
    public boolean insertReq(String nama, String request){
        ContentValues cv= new ContentValues();
        cv.put("nama", nama );
        cv.put("request", request );
        long result= db.insert("tblrequest", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean deleteReq(Integer idrequest){
        if (db.delete("tblrequest","idrequest= ?",new String[]{String.valueOf(idrequest)})==-1){
            return false;
        }else{
            return true;
        }
    }

    // Pemasukan
    public boolean insertTransMasuk(String tgltransaksi, String notransaksi, String fakturtransaksi, String keterangantransaksi, String masuk, String saldo, String status){
        ContentValues cv= new ContentValues();
        cv.put("tgltransaksi", tgltransaksi );
        cv.put("notransaksi", notransaksi );
        cv.put("fakturtransaksi", fakturtransaksi );
        cv.put("keterangantransaksi", keterangantransaksi );
        cv.put("masuk", masuk );
        cv.put("saldo", saldo );
        cv.put("status", status );
        long result= db.insert("tbltransaksi", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    // Pengeluaran
    public boolean insertTransKeluar(String tgltransaksi, String notransaksi, String fakturtransaksi, String keterangantransaksi, String keluar, String saldo, String status){
        ContentValues cv= new ContentValues();
        cv.put("tgltransaksi", tgltransaksi );
        cv.put("notransaksi", notransaksi );
        cv.put("fakturtransaksi", fakturtransaksi );
        cv.put("keterangantransaksi", keterangantransaksi );
        cv.put("keluar", keluar );
        cv.put("saldo", saldo );
        cv.put("status", status );
        long result= db.insert("tbltransaksi", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    // Kategori
    public List<String> getIdKategori(){
        List<String> labels = new ArrayList<String>();
        String q = "SELECT * FROM tblkategori";
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(0));
            }while (c.moveToNext());
        }
        return labels;
    }

    public List<String> getKodeKategori(){
        List<String> labels = new ArrayList<String>();
        String q = "SELECT * FROM tblkategori";
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(1));
            }while (c.moveToNext());
        }
        return labels;
    }

    public List<String> getKategori(){
        List<String> labels = new ArrayList<String>();
        String q = "SELECT * FROM tblkategori";
        Cursor c = db.rawQuery(q,null);
        if (c.moveToNext()){
            do {
                labels.add(c.getString(2));
            }while (c.moveToNext());
        }
        return labels;
    }

    public boolean insertKategori(String kodekategori, String kategori){
        ContentValues cv= new ContentValues();
        cv.put("kodekategori", kodekategori );
        cv.put("kategori", kategori );
        long result= db.insert("tblkategori", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean deleteKategori(Integer idkategori){
        if (db.delete("tblkategori","idkategori= ?",new String[]{String.valueOf(idkategori)})==-1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean updateKategori(int idkategori, String kodekategori, String kategori){
        ContentValues cv = new ContentValues();
        cv.put("kodekategori", kodekategori );
        cv.put("kategori", kategori );
        long result = db.update("tblkategori", cv, "idkategori=?", new String[]{String.valueOf(idkategori)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Barang
    public boolean insertBarang(String barang, String jenis, String warna, String variasi, String ukuran){
        ContentValues cv= new ContentValues();
        cv.put("barang", barang);
        cv.put("jenis", jenis);
        cv.put("warna", warna);
        cv.put("variasi", variasi);
        cv.put("ukuran", ukuran);
        long result= db.insert("tblbarang", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean deleteBarang(Integer idbarang){
        if (db.delete("tblbarang","idbarang= ?",new String[]{String.valueOf(idbarang)})==-1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean updateBarang(int idbarang, String barang, String jenis, String warna, String variasi, String ukuran){
        ContentValues cv = new ContentValues();
        cv.put("barang", barang );
        cv.put("jenis", jenis );
        cv.put("warna", warna );
        cv.put("variasi", variasi );
        cv.put("ukuran", ukuran );
        long result = db.update("tblbarang", cv, "idbarang=?", new String[]{String.valueOf(idbarang)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Pelanggan
    public boolean insertPelanggan(String pelanggan, String alamat, String notelp){
        ContentValues cv= new ContentValues();
        cv.put("pelanggan", pelanggan );
        cv.put("alamat", alamat );
        cv.put("telp", notelp );
        long result= db.insert("tblpelanggan", null, cv);
        if (result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean deletePelanggan(Integer idpelanggan){
        if (db.delete("tblpelanggan","idpelanggan= ?",new String[]{String.valueOf(idpelanggan)})==-1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean updatePelanggan(int idpelanggan, String pelanggan, String alamat, String notelp){
        ContentValues cv = new ContentValues();
        cv.put("pelanggan", pelanggan );
        cv.put("alamat", alamat );
        cv.put("telp", notelp );
        long result = db.update("tblpelanggan", cv, "idpelanggan=?", new String[]{String.valueOf(idpelanggan)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}
