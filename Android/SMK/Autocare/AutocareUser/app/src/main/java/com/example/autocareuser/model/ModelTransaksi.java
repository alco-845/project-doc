package com.example.autocareuser.model;

public class ModelTransaksi {
    private Integer idtransaksi;
    private Integer idadmin;
    private Integer idpelanggan;
    private Integer iditem;
    private String tgltransaksi;
    private String status;

    public Integer getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(Integer idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public Integer getIdadmin() {
        return idadmin;
    }

    public void setIdadmin(Integer idadmin) {
        this.idadmin = idadmin;
    }

    public Integer getIdpelanggan() {
        return idpelanggan;
    }

    public void setIdpelanggan(Integer idpelanggan) {
        this.idpelanggan = idpelanggan;
    }

    public Integer getIditem() {
        return iditem;
    }

    public void setIditem(Integer iditem) {
        this.iditem = iditem;
    }

    public String getTgltransaksi() {
        return tgltransaksi;
    }

    public void setTgltransaksi(String tgltransaksi) {
        this.tgltransaksi = tgltransaksi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
