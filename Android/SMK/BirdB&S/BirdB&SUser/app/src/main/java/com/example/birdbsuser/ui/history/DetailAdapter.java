package com.example.birdbsuser.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsuser.R;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelBeliDetail;
import com.example.birdbsuser.model.ModelItem;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_bird, parent, false);
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
                holder.tvName.setText(response.body().get(0).getNamaitem());
                holder.tvPrice.setText("Price Per item : Rp. " + response.body().get(0).getHarga() + "\n\n" + "Total : Rp. " + itemPosition.getHarga());
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvQty.setText("Qty : " + itemPosition.getJumlah());
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvQty, tvName, tvPrice;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvNameItem);
            tvQty = (TextView) itemView.findViewById(R.id.tvQtyItem);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPriceItem);
        }
    }
}
