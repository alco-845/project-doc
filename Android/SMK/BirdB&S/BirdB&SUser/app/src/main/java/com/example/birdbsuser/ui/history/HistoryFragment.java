package com.example.birdbsuser.ui.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsuser.R;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelBeli;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar appbar = view.findViewById(R.id.customToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(appbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = view.findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.title_history);

        ImageView ivBack = view.findViewById(R.id.btnBack);
        ivBack.setVisibility(View.GONE);

        ImageView ivCart = view.findViewById(R.id.btnCart);
        ivCart.setVisibility(View.GONE);

        setData();
    }

    public void setData() {
        SharedPreferences sh = getContext().getSharedPreferences("UserApp", Context.MODE_PRIVATE);
        int id = sh.getInt("id", 0);

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelBeli>> call = service.getBeliTwoParams(id, 2);
        call.enqueue(new Callback<List<ModelBeli>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelBeli>> call, @NonNull Response<List<ModelBeli>> response) {
                HistoryAdapter adapter = new HistoryAdapter(getContext(), response.body());
                RecyclerView recItem = (RecyclerView) getView().findViewById(R.id.recHistory);
                recItem.setLayoutManager(new LinearLayoutManager(getContext()));
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelBeli>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}