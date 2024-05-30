package com.example.hi_kinguser.koneksi;

import com.example.hi_kinguser.model.ModelItem;
import com.example.hi_kinguser.model.ModelPemilik;
import com.example.hi_kinguser.model.ModelPenyewa;
import com.example.hi_kinguser.model.ModelSewa;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiConfig {
    // Start of penyewa
    @GET("penyewa")
    Call<List<ModelPenyewa>> getPenyewa();

    @FormUrlEncoded
    @POST("penyewa")
    Call<ModelPenyewa> insertPenyewa(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("penyewa/{idpenyewa}")
    Call<ModelPenyewa> updatePenyewa(
            @Path("idpenyewa") Integer idpenyewa,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("penyewa")
    Call<List<ModelPenyewa>> deletePenyewa(@Field("idpenyewa") Integer idpenyewa);

    @GET("penyewa/get/{idpenyewa}")
    Call<List<ModelPenyewa>> getPenyewaId(@Path("idpenyewa") Integer idpenyewa);

    @GET("penyewa/{username}")
    Call<List<ModelPenyewa>> getByName(@Path("username") String username);
    // End of pemilik

    // Start of pemilik
    @GET("pemilik/get/{idpemilik}")
    Call<List<ModelPemilik>> getPemilikId(@Path("idpemilik") Integer idpemilik);
    // End of pemilik

    // Start of item
    @GET("item")
    Call<List<ModelItem>> getItem();

    @FormUrlEncoded
    @POST("item")
    Call<ModelItem> insertItem(
            @Field("idpemilik") Integer idpemilik,
            @Field("kategori") Integer kategori,
            @Field("namaitem") String namaitem,
            @Field("harga") String harga,
            @Field("jumlah") Integer jumlah
    );

    @FormUrlEncoded
    @POST("item/{iditem}")
    Call<ModelItem> updateItem(
            @Path("iditem") Integer iditem,
            @Field("idpemilik") Integer idpemilik,
            @Field("kategori") Integer kategori,
            @Field("namaitem") String namaitem,
            @Field("harga") String harga,
            @Field("jumlah") Integer jumlah
    );

    @FormUrlEncoded
    @POST("item/{iditem}")
    Call<ModelItem> updateJumlahItem(
            @Path("iditem") Integer iditem,
            @Field("jumlah") Integer jumlah
    );

    @DELETE("item/{iditem}")
    Call<ModelItem> deleteItem(@Path("iditem") Integer iditem);

    @GET("item/{kategori}")
    Call<List<ModelItem>> getItemStatus(@Path("kategori") Integer kategori);

    @GET("item/get/{iditem}")
    Call<List<ModelItem>> getItemId(@Path("iditem") Integer iditem);

    @GET("item/search/{search}")
    Call<List<ModelItem>> searchItem(@Path("search") String search);
    // End of item

    // Start of sewa
    @GET("sewa")
    Call<List<ModelSewa>> getSewa();

    @GET("sewa/penyewa/{idpenyewa}")
    Call<List<ModelSewa>> getSewaTwoParams(@Path("idpenyewa") Integer idpenyewa);

    @GET("sewa/{idpenyewa}/{status}")
    Call<List<ModelSewa>> getSewaTwoParamsStatus(@Path("idpenyewa") Integer idpenyewa, @Path("status") String status);

    @GET("sewa/get/{idsewa}")
    Call<List<ModelSewa>> getSewaById(@Path("idsewa") Integer idsewa);

    @GET("sewa/{status}")
    Call<List<ModelSewa>> filterSewa(@Path("status") String status);

    @GET("sewa/search/{search}")
    Call<List<ModelSewa>> searchSewa(@Path("search") String search);

//    @POST("tblsewa/filterDateSewa.php")
//    Call<List<ModelSewa>> filterDateSewa(@Field("datebefore") String datebefore, @Field("dateafter") String dateafter);

    @FormUrlEncoded
    @POST("sewa/{idsewa}")
    Call<ModelSewa> updateSewa(
            @Path("idsewa") Integer idsewa,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("sewa")
    Call<ModelSewa> insertSewa(
            @Field("iditem") Integer iditem,
            @Field("idpemilik") Integer idpemilik,
            @Field("idpenyewa") Integer idpenyewa,
            @Field("penyewa") String penyewa,
            @Field("tglmulai") String tglmulai,
            @Field("tglselesai") String tglselesai,
            @Field("jumlah") Integer jumlah,
            @Field("total") Integer total,
            @Field("status") String status
    );
    // End of sewa
}
