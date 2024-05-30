package com.example.gopartyadmin.model;

public class ModelItem {
    private Integer iditem;
    private Integer idpemilik;
    private String kategori;
    private String namaitem;
    private String harga;
    private Integer jumlah;

    public Integer getIditem() {
        return iditem;
    }

    public void setIditem(Integer iditem) {
        this.iditem = iditem;
    }

    public Integer getIdpemilik() {
        return idpemilik;
    }

    public void setIdpemilik(Integer idpemilik) {
        this.idpemilik = idpemilik;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getNamaitem() {
        return namaitem;
    }

    public void setNamaitem(String namaitem) {
        this.namaitem = namaitem;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }
}
