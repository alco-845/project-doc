package com.example.hi_kinguser.koneksi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.43.109/Project/API/hiking_api/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
