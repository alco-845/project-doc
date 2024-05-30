package com.example.birdbsuser.ui.transaction;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdbsuser.R;
import com.example.birdbsuser.helper.Helper;
import com.example.birdbsuser.koneksi.Api;
import com.example.birdbsuser.koneksi.ApiConfig;
import com.example.birdbsuser.model.ModelBeli;
import com.example.birdbsuser.model.ModelBeliDetail;
import com.example.birdbsuser.model.ModelItem;
import com.example.birdbsuser.ui.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private final Context context;
    private final List<ModelItem> listItem;

    private int newIdBeli = 0;
    private int tJumlah = 0;

    public TransactionAdapter(Context context, List<ModelItem> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bird, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        ModelItem itemPosition = listItem.get(position);
        holder.tvName.setText(itemPosition.getNamaitem());
        holder.tvPrice.setText("Rp. " + itemPosition.getHarga());
        holder.tvValue.setText("Stok : " + String.valueOf(itemPosition.getJumlah()));

        holder.itemView.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(context).inflate(R.layout.fragment_transaction, bottomSheetDialog.findViewById(R.id.bottomSheetContainer));

            TextView tvName = bottomSheetView.findViewById(R.id.tvDialogBirdName);
            tvName.setText(itemPosition.getNamaitem());

            TextView tvPrice = bottomSheetView.findViewById(R.id.tvDialogBirdPrice);
            tvPrice.setText("Rp. " + itemPosition.getHarga());

            TextView tvValue = bottomSheetView.findViewById(R.id.tvDialogBirdValue);
            tvValue.setText("Stok : " + String.valueOf(itemPosition.getJumlah()));

            EditText edtJumlah = bottomSheetView.findViewById(R.id.edtDialogBirdValue);
            ImageView btnAddJ = bottomSheetView.findViewById(R.id.btnPlus);
            ImageView btnRemoveJ = bottomSheetView.findViewById(R.id.btnMin);

            SharedPreferences sh = context.getSharedPreferences("UserApp", MODE_PRIVATE);
            int id = sh.getInt("id", 0);
            int idbeli = sh.getInt("idbeli", 0);
            String total = sh.getString("total", "0");

            ApiConfig service = Api.getRetrofit().create(ApiConfig.class);

            if (idbeli == 0) {
                Call<ModelBeli> call = service.getBeliLast();
                call.enqueue(new Callback<ModelBeli>() {
                    @Override
                    public void onResponse(@NonNull Call<ModelBeli> call, @NonNull Response<ModelBeli> response) {
                        newIdBeli = response.body().getIdbeli() + 1;
                        tJumlah = 0;
                    }

                    @Override
                    public void onFailure(@NonNull Call<ModelBeli> call, @NonNull Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            btnAddJ.setOnClickListener(v12 -> {
                tJumlah = tJumlah + 1;
                edtJumlah.setText(String.valueOf(tJumlah));

                if (tJumlah < 1){
                    btnRemoveJ.setVisibility(View.INVISIBLE);
                } else {
                    btnRemoveJ.setVisibility(View.VISIBLE);
                }

                if (tJumlah == itemPosition.getJumlah()) {
                    btnAddJ.setVisibility(View.INVISIBLE);
                } else {
                    btnAddJ.setVisibility(View.VISIBLE);
                }
            });

            btnRemoveJ.setOnClickListener(v13 -> {
                tJumlah = tJumlah - 1;
                edtJumlah.setText(String.valueOf(tJumlah));

                if (tJumlah < 1){
                    btnRemoveJ.setVisibility(View.INVISIBLE);
                } else {
                    btnRemoveJ.setVisibility(View.VISIBLE);
                }

                if (tJumlah == itemPosition.getJumlah()) {
                    btnAddJ.setVisibility(View.INVISIBLE);
                } else {
                    btnAddJ.setVisibility(View.VISIBLE);
                }
            });

            bottomSheetView.findViewById(R.id.btnSubmit).setOnClickListener(v1 -> {
                if (tJumlah == 0){
                    Toast.makeText(context, "Jumlah tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    String now = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                    int totalPrice = tJumlah * Integer.parseInt(itemPosition.getHarga());

                    if (idbeli == 0) {
                        // Insert
                        Call<ModelBeli> insertBeli = service.insertBeli(newIdBeli, id, itemPosition.getIdpenjual(), Helper.convertDate(now), totalPrice, "0", "0", "3");
                        insertBeli.enqueue(new Callback<ModelBeli>() {
                            @Override
                            public void onResponse(@NonNull Call<ModelBeli> call, @NonNull Response<ModelBeli> response) {
                                SharedPreferences.Editor prefEdit = sh.edit();
                                prefEdit.putInt("idbeli", response.body().getIdbeli());
                                prefEdit.putString("total", response.body().getTotal());
                                prefEdit.apply();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ModelBeli> call, @NonNull Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        Call<ModelBeliDetail> insertBeliDetail = service.insertBeliDetail(newIdBeli, itemPosition.getIditem(), tJumlah, totalPrice);
                        insertBeliDetail.enqueue(new Callback<ModelBeliDetail>() {
                            @Override
                            public void onResponse(@NonNull Call<ModelBeliDetail> call, @NonNull Response<ModelBeliDetail> response) {
                                SharedPreferences.Editor prefEdit = sh.edit();
                                prefEdit.putInt(String.valueOf(response.body().getIditem()), response.body().getIditem());
                                prefEdit.apply();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ModelBeliDetail> call, @NonNull Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        Call<ModelItem> updateItem = service.updateItem(itemPosition.getIditem(), itemPosition.getJumlah() - tJumlah);
                        updateItem.enqueue(new Callback<ModelItem>() {
                            @Override
                            public void onResponse(@NonNull Call<ModelItem> call, @NonNull Response<ModelItem> response) {
                            }

                            @Override
                            public void onFailure(@NonNull Call<ModelItem> call, @NonNull Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        ((MainActivity)context).reload();
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        // End of Insert
                    } else {
                        // Update
                        int newTotal = Integer.parseInt(total) + totalPrice;

                        Call<ModelBeli> updateBeli = service.updateBeli(idbeli, newTotal);
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

                        Call<ModelBeliDetail> insertBeliDetail = service.insertBeliDetail(idbeli, itemPosition.getIditem(), tJumlah, totalPrice);
                        insertBeliDetail.enqueue(new Callback<ModelBeliDetail>() {
                            @Override
                            public void onResponse(@NonNull Call<ModelBeliDetail> call, @NonNull Response<ModelBeliDetail> response) {
                                SharedPreferences.Editor prefEdit = sh.edit();
                                prefEdit.putInt(String.valueOf(response.body().getIditem()), response.body().getIditem());
                                prefEdit.apply();
                            }

                            @Override
                            public void onFailure(@NonNull Call<ModelBeliDetail> call, @NonNull Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        Call<ModelItem> updateItem = service.updateItem(itemPosition.getIditem(), itemPosition.getJumlah() - tJumlah);
                        updateItem.enqueue(new Callback<ModelItem>() {
                            @Override
                            public void onResponse(@NonNull Call<ModelItem> call, @NonNull Response<ModelItem> response) {
                            }

                            @Override
                            public void onFailure(@NonNull Call<ModelItem> call, @NonNull Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        ((MainActivity)context).reload();
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        // End of Update
                    }
                }
            });
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvValue;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvNameItem);
            tvValue = (TextView) itemView.findViewById(R.id.tvQtyItem);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPriceItem);
        }
    }
}
