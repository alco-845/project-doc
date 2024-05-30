package com.example.birdbsuser.ui.transaction;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsuser.R;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelBeli;
import com.example.birdbsuser.model.ModelBeliDetail;
import com.example.birdbsuser.model.ModelItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final Context context;
    private final List<ModelBeliDetail> listItem;

    private int tJumlah = 0;
    private String cartJumlah = "0";

    public CartAdapter(Context context, List<ModelBeliDetail> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bird, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ModelBeliDetail itemPosition = listItem.get(position);

        cartJumlah = itemPosition.getJumlah();

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> getItemId = service.getItemId(itemPosition.getIditem());
        getItemId.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                tJumlah = response.body().get(0).getJumlah();
                holder.tvName.setText(response.body().get(0).getNamaitem());
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvPrice.setText("Rp. " + itemPosition.getHarga());
        holder.tvValue.setText("Qty : " + String.valueOf(itemPosition.getJumlah()));

        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog
                    .setMessage("Delete this item?")
                    .setPositiveButton("Yes", (dialog12, which) -> {
                        Call<ModelBeliDetail> call = service.deleteBeliDetail(itemPosition.getIdbelidetail());
                        call.enqueue(new Callback<ModelBeliDetail>() {
                            @Override
                            public void onResponse(@NonNull Call<ModelBeliDetail> call, @NonNull Response<ModelBeliDetail> response) {
                                Call<ModelItem> updateItem = service.updateItem(itemPosition.getIditem(), Integer.valueOf(cartJumlah) + tJumlah);
                                updateItem.enqueue(new Callback<ModelItem>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ModelItem> call, @NonNull Response<ModelItem> response) {
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ModelItem> call, @NonNull Throwable t) {
                                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                                SharedPreferences sh = context.getSharedPreferences("UserApp", MODE_PRIVATE);
                                String total = sh.getString("total", "0");
                                int newTotal = Integer.parseInt(total) - Integer.parseInt(itemPosition.getHarga());
                                Call<ModelBeli> updateBeli = service.updateBeli(itemPosition.getIdbeli(), newTotal);
                                updateBeli.enqueue(new Callback<ModelBeli>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ModelBeli> call, @NonNull Response<ModelBeli> response) {
                                        SharedPreferences.Editor prefEdit = sh.edit();
                                        prefEdit.putString("total", response.body().getTotal());
                                        prefEdit.apply();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ModelBeli> call, @NonNull Throwable t) {
                                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                ((CartActivity)context).reload();
                                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ModelBeliDetail> call, @NonNull Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", (dialog1, id) -> dialog1.cancel());
            dialog.create();
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvValue;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvNameItem);
            tvValue = (TextView) itemView.findViewById(R.id.tvQtyItem);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPriceItem);
        }
    }
}
