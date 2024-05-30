package com.example.gopartyadmin.model;

public class ModelPenyewa {
    private Integer idpenyewa;
    private String username;
    private String password;
    private String nama;
    private String alamat;
    private String telpon;

    public Integer getIdpenyewa() {
        return idpenyewa;
    }

    public void setIdpenyewa(Integer idpenyewa) {
        this.idpenyewa = idpenyewa;
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
