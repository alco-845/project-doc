package com.example.birdbsuser.model;

public class ModelBeliDetail {
    private Integer idbelidetail;
    private Integer idpenjual;
    private Integer idbeli;
    private Integer iditem;
    private String jumlah;
    private String harga;

    public Integer getIdbelidetail() {
        return idbelidetail;
    }

    public void setIdbelidetail(Integer idbelidetail) {
        this.idbelidetail = idbelidetail;
    }

    public Integer getIdpenjual() {
        return idpenjual;
    }

    public void setIdpenjual(Integer idpenjual) {
        this.idpenjual = idpenjual;
    }

    public Integer getIdbeli() {
        return idbeli;
    }

    public void setIdbeli(Integer idbeli) {
        this.idbeli = idbeli;
    }

    public Integer getIditem() {
        return iditem;
    }

    public void setIditem(Integer iditem) {
        this.iditem = iditem;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
