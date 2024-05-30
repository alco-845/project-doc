package com.example.gopartyuser.ui.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gopartyuser.R;
import com.example.gopartyuser.koneksi.Api;
import com.example.gopartyuser.koneksi.ApiConfig;
import com.example.gopartyuser.model.ModelSewa;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    RecyclerView recItem;
    int id = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar appbar = view.findViewById(R.id.customToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(appbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = view.findViewById(R.id.tvToolbar);
        tvTitle.setText("History");

        ImageView ivBack = view.findViewById(R.id.btnBack);
        ivBack.setVisibility(View.GONE);

        recItem = (RecyclerView) getView().findViewById(R.id.recTrans);
        recItem.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sh = getContext().getSharedPreferences("UserApp", Context.MODE_PRIVATE);
        id = sh.getInt("id", 0);

        getData();

        Spinner spinner = (Spinner) getView().findViewById(R.id.spTrans) ;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getData();
                } else if (position != 0){
                    getFilteredData(parent.getItemAtPosition(position).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelSewa>> call = service.getSewaTwoParams(id);
        call.enqueue(new Callback<List<ModelSewa>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelSewa>> call, @NonNull Response<List<ModelSewa>> response) {
                HistoryAdapter adapter = new HistoryAdapter(getContext(), response.body());
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelSewa>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilteredData(String position) {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelSewa>> call = service.getSewaTwoParamsStatus(id, position);
        call.enqueue(new Callback<List<ModelSewa>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelSewa>> call, @NonNull Response<List<ModelSewa>> response) {
                HistoryAdapter adapter = new HistoryAdapter(getContext(), response.body());
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelSewa>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}