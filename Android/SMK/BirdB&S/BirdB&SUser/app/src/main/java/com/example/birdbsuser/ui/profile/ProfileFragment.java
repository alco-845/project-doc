package com.example.birdbsuser.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.birdbsuser.R;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelPembeli;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar appbar = view.findViewById(R.id.customToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(appbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = view.findViewById(R.id.tvToolbar);
        tvTitle.setText(R.string.txt_profile);

        ImageView ivBack = view.findViewById(R.id.btnBack);
        ivBack.setVisibility(View.GONE);

        ImageView ivCart = view.findViewById(R.id.btnCart);
        ivCart.setVisibility(View.GONE);

        setTextData();

        Intent intent = new Intent(getContext(), UpdateProfileActivity.class);

        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> startActivity(intent));

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog
                    .setMessage("Logout?")
                    .setPositiveButton("Yes", (dialog12, which) -> {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserApp", MODE_PRIVATE);
                        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                        prefEdit.remove("id");
                        prefEdit.apply();
                    })
                    .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
            dialog.create();
            dialog.show();
        });
    }

    private void setTextData() {
        SharedPreferences sh = getContext().getSharedPreferences("UserApp", MODE_PRIVATE);
        int id = sh.getInt("id", 0);

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelPembeli>> call = service.getPembeliId(id);
        call.enqueue(new Callback<List<ModelPembeli>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelPembeli>> call, @NonNull Response<List<ModelPembeli>> response) {
                TextView tvName = (TextView) getView().findViewById(R.id.tvName);
                tvName.setText(response.body().get(0).getNama());

                TextView tvAddress = (TextView) getView().findViewById(R.id.tvAddress);
                tvAddress.setText(response.body().get(0).getAlamat());

                TextView tvPhone = (TextView) getView().findViewById(R.id.tvPhone);
                tvPhone.setText(response.body().get(0).getTelpon());
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelPembeli>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setTextData();
    }
}