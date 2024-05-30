package com.example.autocareuser.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.autocareuser.koneksi.Api;
import com.example.autocareuser.koneksi.ApiConfig;
import com.example.autocareuser.model.ModelTransaksi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusViewModel extends ViewModel {

    private final MutableLiveData<String> listTransaksi = new MutableLiveData<>();

    public void setTrans() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<ModelTransaksi> call = service.getTransaksiLast();
        call.enqueue(new Callback<ModelTransaksi>() {
            @Override
            public void onResponse(@NonNull Call<ModelTransaksi> call, @NonNull Response<ModelTransaksi> response) {
                listTransaksi.setValue(response.body().getStatus());
            }

            @Override
            public void onFailure(@NonNull Call<ModelTransaksi> call, @NonNull Throwable t) {
                Log.d("StatusViewModel"," - > Error    "+ t.getMessage());
            }
        });
    }

    public LiveData<String> getTrans() {
        return listTransaksi;
    }
}
