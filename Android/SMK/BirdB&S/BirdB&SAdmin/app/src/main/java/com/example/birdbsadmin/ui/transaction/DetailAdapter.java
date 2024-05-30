package com.example.birdbsadmin.ui.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsadmin.R;
import com.example.birdbsadmin.koneksi.Api;
import com.example.birdbsadmin.koneksi.ApiConfig;
import com.example.birdbsadmin.model.ModelBeliDetail;
import com.example.birdbsadmin.model.ModelItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {
    private final Context context;
    private final List<ModelBeliDetail> listItem;

    public DetailAdapter(Context context, List<ModelBeliDetail> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        ModelBeliDetail itemPosition = listItem.get(position);

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.getItemId(itemPosition.getIditem());
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                holder.tvDate.setText(response.body().get(0).getNamaitem());
                holder.tvTotal.setText("Price Per item : Rp. " + response.body().get(0).getHarga());
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvName.setText("Qty : " + itemPosition.getJumlah());
        holder.tvStatus.setText("Total : Rp. " + itemPosition.getHarga());
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvStatus, tvTotal;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tvDateItem);
            tvName = (TextView) itemView.findViewById(R.id.tvNameItem);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatusItem);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotalItem);
        }
    }
}
