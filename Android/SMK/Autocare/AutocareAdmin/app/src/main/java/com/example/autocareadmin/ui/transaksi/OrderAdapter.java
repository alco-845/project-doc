package com.example.autocareadmin.ui.transaksi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocareadmin.R;
import com.example.autocareadmin.koneksi.Api;
import com.example.autocareadmin.koneksi.ApiConfig;
import com.example.autocareadmin.model.ModelItem;
import com.example.autocareadmin.model.ModelPelanggan;
import com.example.autocareadmin.model.ModelTransaksi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final Context context;
    private final List<ModelTransaksi> listOrder;

    private String pelanggan;
    private String alamat;
    private String telpon;

    private String item;

    public OrderAdapter(Context context, List<ModelTransaksi> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        ModelTransaksi itemPosition = listOrder.get(position);

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelPelanggan>> callPelanggan = service.getPelangganId(itemPosition.getIdpelanggan());
        callPelanggan.enqueue(new Callback<List<ModelPelanggan>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelPelanggan>> call, @NonNull Response<List<ModelPelanggan>> response) {
                pelanggan = response.body().get(0).getNama();
                alamat = response.body().get(0).getAlamat();
                telpon = response.body().get(0).getTelpon();

                holder.tvPrice.setText(pelanggan);
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelPelanggan>> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<ModelItem>> callItem = service.getItemId(itemPosition.getIditem());
        callItem.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                item = response.body().get(0).getNamaitem();
                holder.tvName.setText(item);
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AcceptOrderActivity.class);
            intent.putExtra("idtransaksi", itemPosition.getIdtransaksi());
            intent.putExtra("item", item);
            intent.putExtra("name", pelanggan);
            intent.putExtra("address", alamat);
            intent.putExtra("phone", telpon);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvItemTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvItemPrice);
        }
    }
}
