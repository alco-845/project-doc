package com.example.gopartyadmin.ui.item;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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

import com.example.gopartyadmin.R;
import com.example.gopartyadmin.koneksi.Api;
import com.example.gopartyadmin.koneksi.ApiConfig;
import com.example.gopartyadmin.model.ModelItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView recItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar appbar = view.findViewById(R.id.customToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(appbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = view.findViewById(R.id.tvToolbar);
        tvTitle.setText("Home");

        ImageView ivBack = view.findViewById(R.id.btnBack);
        ivBack.setVisibility(View.GONE);

        Intent intent = new Intent(getContext(), AddItemActivity.class);

        ImageView ivCart = view.findViewById(R.id.btnAdd);
        ivCart.setOnClickListener(v -> startActivity(intent));

        recItem = (RecyclerView) getView().findViewById(R.id.recHome);
        recItem.setLayoutManager(new LinearLayoutManager(getContext()));

        EditText edtSearch = (EditText) view.findViewById(R.id.edtSearchItem);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = edtSearch.getText().toString();
                if (keyword.equals("")) {
                    getData();
                } else {
                    getSearchData(keyword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Spinner spinner = (Spinner) getView().findViewById(R.id.spKatHome) ;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getData();
                } else if (position != 0){
                    getFilteredData(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.getItem();
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                ItemAdapter adapter = new ItemAdapter(getContext(), response.body());
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFilteredData(int position) {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.getItemStatus(position);
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                ItemAdapter adapter = new ItemAdapter(getContext(), response.body());
                recItem.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearchData(String keyword) {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.searchItem(keyword);
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                ItemAdapter adapter = new ItemAdapter(getContext(), response.body());
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
        getData();
    }
}