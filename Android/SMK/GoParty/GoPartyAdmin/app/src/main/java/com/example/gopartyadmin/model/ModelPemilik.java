package com.example.gopartyadmin.model;

public class ModelPemilik {
    private Integer idpemilik;
    private String username;
    private String password;
    private String nama;
    private String alamat;
    private String telpon;

    public Integer getIdpemilik() {
        return idpemilik;
    }

    public void setIdpemilik(Integer idpemilik) {
        this.idpemilik = idpemilik;
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
