package com.cristiawan.goparty;

public class Item {

    private String nama;
    private String isi;
    private String harga;

    public Item(String nama, String harga, String isi) {
        this.nama = nama;
        this.isi = isi;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
