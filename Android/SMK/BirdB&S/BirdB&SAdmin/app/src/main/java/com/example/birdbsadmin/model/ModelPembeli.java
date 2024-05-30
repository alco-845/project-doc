package com.example.birdbsadmin.model;

import com.google.gson.annotations.SerializedName;

public class ModelPembeli {
    @SerializedName("idpembeli")
    private Integer idpembeli;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("nama")
    private String nama;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("telpon")
    private String telpon;

    public Integer getIdpenjual() {
        return idpembeli;
    }

    public void setIdpenjual(Integer idpembeli) {
        this.idpembeli = idpembeli;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelpon() {
        return telpon;
    }

    public void setTelpon(String telpon) {
        this.telpon = telpon;
    }
}
