package com.example.hi_kingadmin.model;

public class ModelSewa {
    private Integer idsewa;
    private Integer iditem;
    private Integer idpemilik;
    private Integer idpenyewa;
    private String penyewa;
    private String tglmulai;
    private String tglselesai;
    private Integer jumlah;
    private Integer total;
    private String status;

    public Integer getIdsewa() {
        return idsewa;
    }

    public void setIdsewa(Integer idsewa) {
        this.idsewa = idsewa;
    }

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

    public Integer getIdpenyewa() {
        return idpenyewa;
    }

    public void setIdpenyewa(Integer idpenyewa) {
        this.idpenyewa = idpenyewa;
    }

    public String getPenyewa() {
        return penyewa;
    }

    public void setPenyewa(String penyewa) {
        this.penyewa = penyewa;
    }

    public String getTglmulai() {
        return tglmulai;
    }

    public void setTglmulai(String tglmulai) {
        this.tglmulai = tglmulai;
    }

    public String getTglselesai() {
        return tglselesai;
    }

    public void setTglselesai(String tglselesai) {
        this.tglselesai = tglselesai;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
