package com.example.hi_kingadmin.ui.item;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_kingadmin.R;
import com.example.hi_kingadmin.koneksi.Api;
import com.example.hi_kingadmin.koneksi.ApiConfig;
import com.example.hi_kingadmin.model.ModelItem;
import com.example.hi_kingadmin.ui.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ModelItem itemPosition = listItem.get(position);
        holder.tvName.setText(itemPosition.getNamaitem());
        holder.tvPrice.setText("Rp. " + itemPosition.getHarga() + " / day");

        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog
                    .setMessage("Delete this item?")
                    .setPositiveButton("Yes", (dialog12, which) -> {
                        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
                        Call<ModelItem> call = service.deleteItem(itemPosition.getIditem());
                        call.enqueue(new Callback<ModelItem>() {
                            @Override
                            public void onResponse(@NonNull Call<ModelItem> call, @NonNull Response<ModelItem> response) {
                                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                                ((MainActivity)context).reload();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ModelItem> call, @NonNull Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
            dialog.create();
            dialog.show();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailItemActivity.class);
            intent.putExtra("iditem", itemPosition.getIditem());
            intent.putExtra("kategori", String.valueOf(itemPosition.getKategori()));
            intent.putExtra("name", itemPosition.getNamaitem());
            intent.putExtra("spek", itemPosition.getSpek());
            intent.putExtra("price", itemPosition.getHarga());
            intent.putExtra("value", String.valueOf(itemPosition.getJumlah()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView btnDelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvItem);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            btnDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
        }
    }
}
