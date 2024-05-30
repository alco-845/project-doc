package com.example.hi_kingadmin.ui.transaction;

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

import com.example.hi_kingadmin.R;
import com.example.hi_kingadmin.koneksi.Api;
import com.example.hi_kingadmin.koneksi.ApiConfig;
import com.example.hi_kingadmin.model.ModelSewa;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends Fragment {

    RecyclerView recTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar appbar = view.findViewById(R.id.customToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(appbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = view.findViewById(R.id.tvToolbar);
        tvTitle.setText("Transaction");

        ImageView ivBack = view.findViewById(R.id.btnBack);
        ivBack.setVisibility(View.GONE);

        ImageView ivCart = view.findViewById(R.id.btnAdd);
        ivCart.setVisibility(View.GONE);

        recTransaction = (RecyclerView) getView().findViewById(R.id.recTrans);
        recTransaction.setLayoutManager(new LinearLayoutManager(getContext()));

        EditText edtSearch = (EditText) view.findViewById(R.id.edtSearchTrans);
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

    private void getData() {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelSewa>> call = service.getSewa();
        call.enqueue(new Callback<List<ModelSewa>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelSewa>> call, @NonNull Response<List<ModelSewa>> response) {
                TransactionAdapter adapter = new TransactionAdapter(getContext(), response.body());
                recTransaction.setAdapter(adapter);
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
        Call<List<ModelSewa>> call = service.filterSewa(position);
        call.enqueue(new Callback<List<ModelSewa>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelSewa>> call, @NonNull Response<List<ModelSewa>> response) {
                TransactionAdapter adapter = new TransactionAdapter(getContext(), response.body());
                recTransaction.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelSewa>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSearchData(String keyword) {
        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelSewa>> call = service.searchSewa(keyword);
        call.enqueue(new Callback<List<ModelSewa>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelSewa>> call, @NonNull Response<List<ModelSewa>> response) {
                TransactionAdapter adapter = new TransactionAdapter(getContext(), response.body());
                recTransaction.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelSewa>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}