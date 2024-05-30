package com.example.hi_kingadmin.koneksi;

import com.example.hi_kingadmin.model.ModelItem;
import com.example.hi_kingadmin.model.ModelPemilik;
import com.example.hi_kingadmin.model.ModelPenyewa;
import com.example.hi_kingadmin.model.ModelSewa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiConfig {
    // Start of pemilik
    @GET("api/pemilik")
    Call<List<ModelPemilik>> getPemilik();

    @FormUrlEncoded
    @POST("api/pemilik")
    Call<ModelPemilik> insertPemilik(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("api/pemilik/{idpemilik}")
    Call<ModelPemilik> updatePemilik(
            @Path("idpemilik") Integer idpemilik,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("api/pemilik")
    Call<List<ModelPemilik>> deletePemilik(@Field("idpemilik") Integer idpemilik);

    @GET("api/pemilik/get/{idpemilik}")
    Call<List<ModelPemilik>> getPemilikId(@Path("idpemilik") Integer idpemilik);

    @GET("api/pemilik/{username}")
    Call<List<ModelPemilik>> getByName(@Path("username") String username);
    // End of pemilik

    // Start of penyewa
    @GET("api/penyewa/get/{idpenyewa}")
    Call<List<ModelPenyewa>> getPenyewaId(@Path("idpenyewa") Integer idpenyewa);
    // End of penyewa

    // Start of item
    @GET("api/item")
    Call<List<ModelItem>> getItem();

    @FormUrlEncoded
    @POST("api/item")
    Call<ModelItem> insertItem(
            @Field("idpemilik") Integer idpemilik,
            @Field("kategori") Integer kategori,
            @Field("namaitem") String namaitem,
            @Field("spek") String spek,
            @Field("harga") String harga,
            @Field("jumlah") Integer jumlah
    );

    @FormUrlEncoded
    @POST("api/item/{iditem}")
    Call<ModelItem> updateItem(
            @Path("iditem") Integer iditem,
            @Field("idpemilik") Integer idpemilik,
            @Field("kategori") Integer kategori,
            @Field("namaitem") String namaitem,
            @Field("spek") String spek,
            @Field("harga") String harga,
            @Field("jumlah") Integer jumlah
    );

    @FormUrlEncoded
    @POST("api/item/{iditem}")
    Call<ModelItem> updateJumlahItem(
            @Path("iditem") Integer iditem,
            @Field("jumlah") Integer jumlah
    );

    @DELETE("api/item/{iditem}")
    Call<ModelItem> deleteItem(@Path("iditem") Integer iditem);

    @GET("api/item/{kategori}")
    Call<List<ModelItem>> getItemStatus(@Path("kategori") Integer kategori);

    @GET("api/item/get/{iditem}")
    Call<List<ModelItem>> getItemId(@Path("iditem") Integer iditem);

    @GET("api/item/search/{search}")
    Call<List<ModelItem>> searchItem(@Path("search") String search);
    // End of item

    // Start of sewa
    @GET("api/sewa")
    Call<List<ModelSewa>> getSewa();

    @GET("api/sewa/get/{idsewa}")
    Call<List<ModelSewa>> getSewaById(@Path("idsewa") Integer idsewa);

    @GET("api/sewa/{status}")
    Call<List<ModelSewa>> filterSewa(@Path("status") String status);

    @GET("sewa/search/{search}")
    Call<List<ModelSewa>> searchSewa(@Path("search") String search);

//    @POST("api/tblsewa/filterDateSewa.php")
//    Call<List<ModelSewa>> filterDateSewa(@Field("datebefore") String datebefore, @Field("dateafter") String dateafter);

    @FormUrlEncoded
    @POST("api/sewa/{idsewa}")
    Call<ModelSewa> updateSewa(
            @Path("idsewa") Integer idsewa,
            @Field("status") String status
    );
    // End of sewa
}
