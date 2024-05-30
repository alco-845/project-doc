package com.example.hi_kingadmin.ui.profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.hi_kingadmin.R;
import com.example.hi_kingadmin.koneksi.Api;
import com.example.hi_kingadmin.koneksi.ApiConfig;
import com.example.hi_kingadmin.model.ModelPemilik;
import com.example.hi_kingadmin.ui.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar appbar = view.findViewById(R.id.customToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(appbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tvTitle = view.findViewById(R.id.tvToolbar);
        tvTitle.setText("Profile");

        ImageView ivBack = view.findViewById(R.id.btnBack);
        ivBack.setVisibility(View.GONE);

        ImageView ivCart = view.findViewById(R.id.btnAdd);
        ivCart.setVisibility(View.GONE);

        setTextData();

        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_update_profile);

            EditText edtNama = (EditText) dialog.findViewById(R.id.edtNama);
            EditText edtAlamat = (EditText) dialog.findViewById(R.id.edtAlamat);
            EditText edtTelpon = (EditText) dialog.findViewById(R.id.edtTelpon);

            SharedPreferences sh = getContext().getSharedPreferences("AdminApp", MODE_PRIVATE);
            int id = sh.getInt("id", 0);

            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
            Call<List<ModelPemilik>> call = service.getPemilikId(id);
            call.enqueue(new Callback<List<ModelPemilik>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelPemilik>> call, @NonNull Response<List<ModelPemilik>> response) {
                    edtNama.setText(response.body().get(0).getNama());
                    edtAlamat.setText(response.body().get(0).getAlamat());
                    edtTelpon.setText(response.body().get(0).getTelpon());
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelPemilik>> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(v1 -> {
                String name = edtNama.getText().toString();
                String address = edtAlamat.getText().toString();
                String phone = edtTelpon.getText().toString();

                if (name.equals("") || address.equals("") || phone.equals("")) {
                    Toast.makeText(getContext(), "Isi data dengan benar", Toast.LENGTH_SHORT).show();
                } else {
                    Call<ModelPemilik> updatePemilik = service.updatePemilik(id, name, address, phone);
                    updatePemilik.enqueue(new Callback<ModelPemilik>() {
                        @Override
                        public void onResponse(@NonNull Call<ModelPemilik> call, @NonNull Response<ModelPemilik> response) {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            ((MainActivity)getContext()).reload();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ModelPemilik> call, @NonNull Throwable t) {
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            dialog.create();
            dialog.show();
        });

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog
                    .setMessage("Logout?")
                    .setPositiveButton("Yes", (dialog12, which) -> {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AdminApp", MODE_PRIVATE);
                        SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                        prefEdit.remove("id");
                        prefEdit.remove("username");
                        prefEdit.apply();
                        ((MainActivity)getContext()).reload();
                    })
                    .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
            dialog.create();
            dialog.show();
        });
    }

    private void setTextData() {
        SharedPreferences sh = getContext().getSharedPreferences("AdminApp", MODE_PRIVATE);
        int id = sh.getInt("id", 0);

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelPemilik>> call = service.getPemilikId(id);
        call.enqueue(new Callback<List<ModelPemilik>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelPemilik>> call, @NonNull Response<List<ModelPemilik>> response) {
                TextView tvName = (TextView) getView().findViewById(R.id.tvName);
                tvName.setText(response.body().get(0).getNama());

                TextView tvAddress = (TextView) getView().findViewById(R.id.tvAddress);
                tvAddress.setText(response.body().get(0).getAlamat());

                TextView tvPhone = (TextView) getView().findViewById(R.id.tvPhone);
                tvPhone.setText(response.body().get(0).getTelpon());
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelPemilik>> call, @NonNull Throwable t) {
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