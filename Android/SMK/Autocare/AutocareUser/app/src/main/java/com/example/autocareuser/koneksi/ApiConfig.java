package com.example.autocareuser.koneksi;

import com.example.autocareuser.model.ModelAdmin;
import com.example.autocareuser.model.ModelItem;
import com.example.autocareuser.model.ModelPelanggan;
import com.example.autocareuser.model.ModelTransaksi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiConfig {
    // Start of pelanggan
    @GET("pelanggan")
    Call<List<ModelPelanggan>> getPelanggan();

    @FormUrlEncoded
    @POST("pelanggan")
    Call<ModelPelanggan> insertPelanggan(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("pelanggan/{idpelanggan}")
    Call<ModelPelanggan> updatePelanggan(
            @Path("idpelanggan") Integer idpelanggan,
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("pelanggan")
    Call<List<ModelPelanggan>> deletePelanggan(@Field("idpelanggan") Integer idpelanggan);

    @GET("pelanggan/get/{idpelanggan}")
    Call<List<ModelPelanggan>> getPelangganId(@Path("idpelanggan") Integer idpelanggan);

    @GET("pelanggan/{username}")
    Call<List<ModelPelanggan>> getByName(@Path("username") String username);
    // End of pelanggan

    // Start of admin
    @GET("admin/get/{idadmin}")
    Call<List<ModelAdmin>> getAdminId(@Path("idadmin") Integer idadmin);
    // End of admin

    // Start of item
    @GET("item")
    Call<List<ModelItem>> getItem();

    @FormUrlEncoded
    @POST("item")
    Call<ModelItem> insertItem(
            @Field("idadmin") Integer idadmin,
            @Field("kategori") Integer kategori,
            @Field("namaitem") String namaitem,
            @Field("detail") String detail,
            @Field("harga") String harga
    );

    @FormUrlEncoded
    @POST("item/{iditem}")
    Call<ModelItem> updateItem(
            @Path("iditem") Integer iditem,
            @Field("idadmin") Integer idadmin,
            @Field("kategori") Integer kategori,
            @Field("namaitem") String namaitem,
            @Field("detail") String detail,
            @Field("harga") String harga
    );

    @DELETE("item/{iditem}")
    Call<ModelItem> deleteItem(@Path("iditem") Integer iditem);

    @GET("item/{kategori}")
    Call<List<ModelItem>> getItemKategori(@Path("kategori") Integer kategori);

    @GET("item/get/{iditem}")
    Call<List<ModelItem>> getItemId(@Path("iditem") Integer iditem);

    @GET("item/search/{search}")
    Call<List<ModelItem>> searchItem(@Path("search") String search);
    // End of item

    // Start of transaksi
    @GET("transaksi")
    Call<List<ModelTransaksi>> getTransaksi();

    @GET("transaksi/last")
    Call<ModelTransaksi> getTransaksiLast();

    @GET("transaksi/pelanggan/{idpelanggan}")
    Call<List<ModelTransaksi>> getTransaksiTwoParams(@Path("idpelanggan") Integer idpelanggan);

    @GET("transaksi/{idpelanggan}/{status}")
    Call<List<ModelTransaksi>> getTransaksiTwoParamsStatus(@Path("idpelanggan") Integer idpelanggan, @Path("status") String status);

    @GET("transaksi/get/{idtransaksi}")
    Call<List<ModelTransaksi>> getTransaksiById(@Path("idtransaksi") Integer idtransaksi);

    @GET("transaksi/{status}")
    Call<List<ModelTransaksi>> filterTransaksi(@Path("status") String status);

    @GET("transaksi/search/{search}")
    Call<List<ModelTransaksi>> searchTransaksi(@Path("search") String search);

//    @POST("tbltransaksi/filterDateTransaksi.php")
//    Call<List<ModelTransaksi>> filterDateTransaksi(@Field("datebefore") String datebefore, @Field("dateafter") String dateafter);

    @FormUrlEncoded
    @POST("transaksi/{idtransaksi}")
    Call<ModelTransaksi> updateTransaksi(
            @Path("idtransaksi") Integer idtransaksi,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("transaksi")
    Call<ModelTransaksi> insertTransaksi(
            @Field("idadmin") Integer idadmin,
            @Field("idpelanggan") Integer idpelanggan,
            @Field("iditem") Integer iditem,
            @Field("tgltransaksi") String tgltransaksi,
            @Field("status") String status
    );
    // End of transaksi
}
