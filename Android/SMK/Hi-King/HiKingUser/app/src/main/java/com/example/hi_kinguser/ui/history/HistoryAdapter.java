package com.example.hi_kinguser.ui.history;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hi_kinguser.R;
import com.example.hi_kinguser.helper.Helper;
import com.example.hi_kinguser.koneksi.Api;
import com.example.hi_kinguser.koneksi.ApiConfig;
import com.example.hi_kinguser.model.ModelItem;
import com.example.hi_kinguser.model.ModelSewa;
import com.example.hi_kinguser.ui.transaction.DetailTransactionActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final Context context;
    private final List<ModelSewa> listItem;

    private String kategori = "", nama = "", spek = "";

    public HistoryAdapter(Context context, List<ModelSewa> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ModelSewa itemPosition = listItem.get(position);

        holder.tvDate.setText(Helper.dateToNormal(itemPosition.getTglmulai()) + " - " + Helper.dateToNormal(itemPosition.getTglselesai()));

        ApiConfig service = Api.getRetrofit().create(ApiConfig.class);
        Call<List<ModelItem>> call = service.getItemId(itemPosition.getIditem());
        call.enqueue(new Callback<List<ModelItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ModelItem>> call, @NonNull Response<List<ModelItem>> response) {
                kategori = response.body().get(0).getKategori();
                nama = response.body().get(0).getNamaitem();
                spek = response.body().get(0).getSpek();
            }

            @Override
            public void onFailure(@NonNull Call<List<ModelItem>> call, @NonNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.tvStatus.setText("Status : " + itemPosition.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailTransactionActivity.class);
            intent.putExtra("iditem", itemPosition.getIditem());
            intent.putExtra("idpemilik", itemPosition.getIdpemilik());
            intent.putExtra("kategori", String.valueOf(kategori));
            intent.putExtra("name", nama);
            intent.putExtra("spek", spek);
            intent.putExtra("price", String.valueOf(itemPosition.getTotal()));
            intent.putExtra("value", String.valueOf(itemPosition.getJumlah()));
            intent.putExtra("ket", "history");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvStatus;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tvItem);
            tvStatus = (TextView) itemView.findViewById(R.id.tvPrice);
        }
    }
}
