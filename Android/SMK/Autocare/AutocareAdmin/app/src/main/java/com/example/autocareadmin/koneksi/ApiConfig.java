package com.example.autocareadmin.koneksi;

import com.example.autocareadmin.model.ModelAdmin;
import com.example.autocareadmin.model.ModelItem;
import com.example.autocareadmin.model.ModelPelanggan;
import com.example.autocareadmin.model.ModelTransaksi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiConfig {
    // Start of admin
    @GET("api/admin")
    Call<List<ModelAdmin>> getAdmin();

    @FormUrlEncoded
    @POST("api/admin")
    Call<ModelAdmin> insertAdmin(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("api/admin/{idadmin}")
    Call<ModelAdmin> updateAdmin(
            @Path("idadmin") Integer idadmin,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("api/admin")
    Call<List<ModelAdmin>> deleteAdmin(@Field("idadmin") Integer idadmin);

    @GET("api/admin/get/{idadmin}")
    Call<List<ModelAdmin>> getAdminId(@Path("idadmin") Integer idadmin);

    @GET("api/admin/{username}")
    Call<List<ModelAdmin>> getByName(@Path("username") String username);
    // End of admin

    // Start of pelanggan
    @GET("api/pelanggan/get/{idpelanggan}")
    Call<List<ModelPelanggan>> getPelangganId(@Path("idpelanggan") Integer idpelanggan);
    // End of pelanggan

    // Start of item
    @GET("api/item")
    Call<List<ModelItem>> getItem();

    @FormUrlEncoded
    @POST("api/item")
    Call<ModelItem> insertItem(
            @Field("idadmin") Integer idadmin,
            @Field("kategori") Integer kategori,
            @Field("namaitem") String namaitem,
            @Field("detail") String detail,
            @Field("harga") String harga
    );

    @FormUrlEncoded
    @POST("api/item/{iditem}")
    Call<ModelItem> updateItem(
            @Path("iditem") Integer iditem,
            @Field("idadmin") Integer idadmin,
            @Field("kategori") Integer kategori,
            @Field("namaitem") String namaitem,
            @Field("detail") String detail,
            @Field("harga") String harga
    );

    @DELETE("api/item/{iditem}")
    Call<ModelItem> deleteItem(@Path("iditem") Integer iditem);

    @GET("api/item/{kategori}")
    Call<List<ModelItem>> getItemKategori(@Path("kategori") Integer kategori);

    @GET("api/item/get/{iditem}")
    Call<List<ModelItem>> getItemId(@Path("iditem") Integer iditem);

    @GET("api/item/search/{search}")
    Call<List<ModelItem>> searchItem(@Path("search") String search);
    // End of item

    // Start of transaksi
    @GET("api/transaksi/{status}")
    Call<List<ModelTransaksi>> getTransaksiStatus(@Path("status") String status);

    @GET("api/transaksi/get/{idtransaksi}")
    Call<List<ModelTransaksi>> getTransaksiById(@Path("idtransaksi") Integer idtransaksi);

    @GET("api/transaksi/{status}")
    Call<List<ModelTransaksi>> filterTransaksi(@Path("status") String status);

//    @POST("api/tbltransaksi/filterDateTransaksi.php")
//    Call<List<ModelTransaksi>> filterDateTransaksi(@Field("datebefore") String datebefore, @Field("dateafter") String dateafter);

    @FormUrlEncoded
    @POST("api/transaksi/{idtransaksi}")
    Call<ModelTransaksi> updateTransaksi(
            @Path("idtransaksi") Integer idtransaksi,
            @Field("status") String status
    );
    // End of transaksi
}
