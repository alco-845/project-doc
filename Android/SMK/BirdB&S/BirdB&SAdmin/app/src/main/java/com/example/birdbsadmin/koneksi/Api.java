package com.example.birdbsadmin.koneksi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.1.8/Project/API/bird_api/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
