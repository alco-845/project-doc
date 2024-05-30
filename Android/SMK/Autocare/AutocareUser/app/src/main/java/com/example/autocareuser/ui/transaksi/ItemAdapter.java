package com.example.autocareuser.ui.transaksi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocareuser.R;
import com.example.autocareuser.model.ModelItem;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final Context context;
    private final List<ModelItem> listItem;

    public ItemAdapter(Context context, List<ModelItem> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ModelItem itemPosition = listItem.get(position);
        holder.tvName.setText(itemPosition.getNamaitem());
        holder.tvPrice.setText("Rp. " + itemPosition.getHarga());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailItemActivity.class);
            intent.putExtra("iditem", itemPosition.getIditem());
            intent.putExtra("idadmin", itemPosition.getIdadmin());
            intent.putExtra("kategori", String.valueOf(itemPosition.getKategori()));
            intent.putExtra("name", itemPosition.getNamaitem());
            intent.putExtra("detail", itemPosition.getDetail());
            intent.putExtra("price", itemPosition.getHarga());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvItemTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvItemPrice);
        }
    }
}
