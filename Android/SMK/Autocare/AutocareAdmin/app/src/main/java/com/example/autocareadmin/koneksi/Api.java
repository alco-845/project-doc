package com.example.autocareadmin.koneksi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.43.109/Project/API/autocare_api/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
