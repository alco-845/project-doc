package com.example.birdbsadmin.model;

import com.google.gson.annotations.SerializedName;

public class ModelItem {
    @SerializedName("iditem")
    private Integer iditem;

    @SerializedName("idpenjual")
    private Integer idpenjual;

    @SerializedName("namaitem")
    private String namaitem;

    @SerializedName("harga")
    private String harga;

    @SerializedName("jumlah")
    private Integer jumlah;

    public Integer getIditem() {
        return iditem;
    }

    public void setIditem(Integer iditem) {
        this.iditem = iditem;
    }

    public Integer getIdpenjual() {
        return idpenjual;
    }

    public void setIdpenjual(Integer idpenjual) {
        this.idpenjual = idpenjual;
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
