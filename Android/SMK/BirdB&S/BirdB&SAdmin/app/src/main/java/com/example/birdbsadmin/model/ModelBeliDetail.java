package com.example.birdbsadmin.model;

import com.google.gson.annotations.SerializedName;

public class ModelBeliDetail {
    @SerializedName("idbelidetail")
    private Integer idbelidetail;

    @SerializedName("idbeli")
    private Integer idbeli;

    @SerializedName("iditem")
    private Integer iditem;

    @SerializedName("jumlah")
    private String jumlah;

    @SerializedName("harga")
    private String harga;

    public Integer getIdbelidetail() {
        return idbelidetail;
    }

    public void setIdbelidetail(Integer idbelidetail) {
        this.idbelidetail = idbelidetail;
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
