package com.example.birdbsadmin.model;

import com.google.gson.annotations.SerializedName;

public class ModelBeli {
    @SerializedName("idbeli")
    private Integer idbeli;

    @SerializedName("idpembeli")
    private Integer idpembeli;

    @SerializedName("idpenjual")
    private Integer idpenjual;

    @SerializedName("tglbeli")
    private String tglbeli;

    @SerializedName("total")
    private String total;

    @SerializedName("bayar")
    private String bayar;

    @SerializedName("kembali")
    private String kembali;

    @SerializedName("status")
    private String status;

    public Integer getIdbeli() {
        return idbeli;
    }

    public void setIdbeli(Integer idbeli) {
        this.idbeli = idbeli;
    }

    public Integer getIdpembeli() {
        return idpembeli;
    }

    public void setIdpembeli(Integer idpembeli) {
        this.idpembeli = idpembeli;
    }

    public Integer getIdpenjual() {
        return idpenjual;
    }

    public void setIdpenjual(Integer idpenjual) {
        this.idpenjual = idpenjual;
    }

    public String getTglbeli() {
        return tglbeli;
    }

    public void setTglbeli(String tglbeli) {
        this.tglbeli = tglbeli;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBayar() {
        return bayar;
    }

    public void setBayar(String bayar) {
        this.bayar = bayar;
    }

    public String getKembali() {
        return kembali;
    }

    public void setKembali(String kembali) {
        this.kembali = kembali;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
