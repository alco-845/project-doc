package com.example.birdbsuser.ui.history;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsuser.R;
import com.example.birdbsuser.helper.Helper;
import com.example.birdbsuser.model.ModelBeli;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final Context context;
    private final List<ModelBeli> listItem;

    public HistoryAdapter(Context context, List<ModelBeli> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bird, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ModelBeli itemPosition = listItem.get(position);

        holder.tvDate.setText(Helper.dateToNormal(itemPosition.getTglbeli()));

        String status;
        if (itemPosition.getStatus().equals("1")) {
            status = "Already paid";
        } else {
            status = "Not yet paid";
        }
        holder.tvTotal.setText("Rp. " + itemPosition.getTotal());
        holder.tvStatus.setText("Status : " + status);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailHistoryActivity.class);
            intent.putExtra("idbeli", String.valueOf(itemPosition.getIdbeli()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        CardView itemBody;
        TextView tvDate, tvStatus, tvTotal;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemBody = (CardView) itemView.findViewById(R.id.itemBody);

            tvDate = (TextView) itemView.findViewById(R.id.tvNameItem);
            tvTotal = (TextView) itemView.findViewById(R.id.tvQtyItem);
            tvStatus = (TextView) itemView.findViewById(R.id.tvPriceItem);
        }
    }
}
