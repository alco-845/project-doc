package com.example.birdbsuser.ui.transaction;

import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsuser.R;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar appbar = view.findViewById(R.id.customToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(appbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = view.findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.txt_home);

        ImageView ivBack = view.findViewById(R.id.btnBack);
        ivBack.setVisibility(View.GONE);

        Intent intent = new Intent(getContext(), CartActivity.class);

        ImageView ivCart = view.findViewById(R.id.btnCart);
        ivCart.setOnClickListener(v -> startActivity(intent));

        setData();
    }

    public void setData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.getItem();
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                TransactionAdapter adapter = new TransactionAdapter(getContext(), response.body());
                RecyclerView recItem = (RecyclerView) getView().findViewById(R.id.recTransaction);
                recItem.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        setData();
    }
}