package com.example.hi_kingadmin.ui.transaction;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_kingadmin.R;
import com.example.hi_kingadmin.helper.Helper;
import com.example.hi_kingadmin.koneksi.Api;
import com.example.hi_kingadmin.koneksi.ApiConfig;
import com.example.hi_kingadmin.model.ModelItem;
import com.example.hi_kingadmin.model.ModelPenyewa;
import com.example.hi_kingadmin.model.ModelSewa;
import com.example.hi_kingadmin.ui.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private final Context context;
    private final List<ModelSewa> listItem;

    private int jumlah = 0;

    public TransactionAdapter(Context context, List<ModelSewa> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_trans, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        ModelSewa itemPosition = listItem.get(position);

        holder.tvDate.setText(Helper.dateToNormal(itemPosition.getTglmulai()) + " - " + Helper.dateToNormal(itemPosition.getTglselesai()));

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.getItemId(itemPosition.getIditem());
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                holder.tvName.setText(response.body().get(0).getNamaitem() + "\n\n" + itemPosition.getPenyewa());
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.tvStatus.setText("Status : " + itemPosition.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_detail_transaction);

            TextView tvPenyewa = (TextView) dialog.findViewById(R.id.tvPenyewa);
            Call<List<ModelPenyewa>> getPenyewa = service.getPenyewaId(itemPosition.getIdpenyewa());
            getPenyewa.enqueue(new Callback<List<ModelPenyewa>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelPenyewa>> call, @NonNull Response<List<ModelPenyewa>> response) {
                    tvPenyewa.setText("Booked By :" + "\n" + itemPosition.getPenyewa() + "\n" + response.body().get(0).getAlamat() + "\n" + response.body().get(0).getTelpon());
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelPenyewa>> call, @NonNull Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            TextView tvTgl = (TextView) dialog.findViewById(R.id.tvTgl);
            tvTgl.setText(Helper.dateToNormal(itemPosition.getTglmulai()) + " - " + Helper.dateToNormal(itemPosition.getTglselesai()));

            TextView tvItemTrans = (TextView) dialog.findViewById(R.id.tvItemTrans);
            Call<List<ModelItem>> getItem = service.getItemId(itemPosition.getIditem());
            getItem.enqueue(new Callback<List<ModelItem>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                    tvItemTrans.setText(response.body().get(0).getNamaitem() + "\n\nJumlah dipinjam : " + itemPosition.getJumlah() + "\n\nHarga : Rp. " + response.body().get(0).getHarga());
                    jumlah = response.body().get(0).getJumlah();
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            TextView tvStatus = (TextView) dialog.findViewById(R.id.tvStatus);
            tvStatus.setText("Total : Rp. "+ itemPosition.getTotal() + "\n\nStatus : " + itemPosition.getStatus());

            Button btnStatus = (Button) dialog.findViewById(R.id.btnStatus);
            if (itemPosition.getStatus().equals("dikembalikan")) {
                btnStatus.setVisibility(View.INVISIBLE);
            } else {
                btnStatus.setOnClickListener(v1 -> {
                    Call<ModelSewa> updateSewa = service.updateSewa(itemPosition.getIdsewa(), "dikembalikan");
                    updateSewa.enqueue(new Callback<ModelSewa>() {
                        @Override
                        public void onResponse(@NonNull Call<ModelSewa> call1, @NonNull Response<ModelSewa> response) {
                            Call<ModelItem> updateItem = service.updateJumlahItem(itemPosition.getIdsewa(), jumlah + itemPosition.getJumlah());
                            updateItem.enqueue(new Callback<ModelItem>() {
                                @Override
                                public void onResponse(@NonNull Call<ModelItem> call1, @NonNull Response<ModelItem> response) {
                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                    ((MainActivity)context).reload();
                                }

                                @Override
                                public void onFailure(@NonNull Call<ModelItem> call1, @NonNull Throwable t) {
                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                            ((MainActivity)context).reload();
                        }

                        @Override
                        public void onFailure(@NonNull Call<ModelSewa> call1, @NonNull Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvStatus;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tvDateTrans);
            tvName = (TextView) itemView.findViewById(R.id.tvItemTrans);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatusTrans);
        }
    }
}
