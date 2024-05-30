package com.example.birdbsadmin.ui.transaction;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsadmin.R;
import com.example.birdbsadmin.helper.Helper;
import com.example.birdbsadmin.koneksi.Api;
import com.example.birdbsadmin.koneksi.ApiConfig;
import com.example.birdbsadmin.model.ModelBeli;
import com.example.birdbsadmin.model.ModelPembeli;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private final Context context;
    private final List<ModelBeli> listItem;

    public TransactionAdapter(Context context, List<ModelBeli> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        ModelBeli itemPosition = listItem.get(position);

        if (itemPosition.getStatus().equals("3")) {
            holder.itemView.setVisibility(View.GONE);
        } else {
            holder.tvDate.setText(Helper.dateToNormal(itemPosition.getTglbeli()));

            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
            Call<List<ModelPembeli>> call = service.getPembeliId(itemPosition.getIdpembeli());
            call.enqueue(new Callback<List<ModelPembeli>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelPembeli>> call, @NonNull Response<List<ModelPembeli>> response) {
                    holder.tvName.setText(response.body().get(0).getNama());
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelPembeli>> call, @NonNull Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            String status;
            if (itemPosition.getStatus().equals("1")) {
                status = "Already paid";
            } else {
                status = "Not yet paid";
            }
            holder.tvTotal.setText("Rp. " + itemPosition.getTotal());
            holder.tvStatus.setText("Status : " + status);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailTransactionActivity.class);
                intent.putExtra("idbeli", String.valueOf(itemPosition.getIdbeli()));
                intent.putExtra("status", itemPosition.getStatus());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvStatus, tvTotal;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tvDateItem);
            tvName = (TextView) itemView.findViewById(R.id.tvNameItem);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatusItem);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotalItem);
        }
    }
}
