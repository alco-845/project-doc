package com.example.gopartyadmin.koneksi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.0.106/Project/API/goparty_api/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
