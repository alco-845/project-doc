package com.example.birdbsadmin.koneksi;

import com.example.birdbsadmin.model.ModelBeli;
import com.example.birdbsadmin.model.ModelBeliDetail;
import com.example.birdbsadmin.model.ModelItem;
import com.example.birdbsadmin.model.ModelPembeli;
import com.example.birdbsadmin.model.ModelPenjual;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiConfig {
    // Start of penjual
    @GET("penjual")
    Call<List<ModelPenjual>> getPenjual();

    @FormUrlEncoded
    @POST("penjual")
    Call<ModelPenjual> insertPenjual(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("penjual")
    Call<List<ModelPenjual>> updatePenjual(
            @Field("idpenjual") Integer idpenjual,
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("penjual")
    Call<List<ModelPenjual>> deletePenjual(@Field("idpenjual") Integer idpenjual);

    @GET("penjual/{username}")
    Call<List<ModelPenjual>> getByName(@Path("username") String username);
    // End of penjual

    // Start of pembeli
    @GET("pembeli/get/{idpembeli}")
    Call<List<ModelPembeli>> getPembeliId(@Path("idpembeli") Integer idpembeli);
    // End of pembeli

    // Start of item
    @GET("item")
    Call<List<ModelItem>> getItem();

    @FormUrlEncoded
    @POST("item")
    Call<ResponseBody> insertItem(
            @Field("idpenjual") Integer idpenjual,
            @Field("namaitem") String namaitem,
            @Field("harga") String harga,
            @Field("jumlah") Integer jumlah
    );

    @FormUrlEncoded
    @POST("item/{iditem}")
    Call<ModelItem> updateItem(
            @Path("iditem") Integer iditem,
            @Field("idpenjual") Integer idpenjual,
            @Field("namaitem") String namaitem,
            @Field("harga") String harga,
            @Field("jumlah") Integer jumlah
    );

    @DELETE("item/{iditem}")
    Call<ModelItem> deleteItem(@Path("iditem") Integer iditem);

    @GET("item/{iditem}")
    Call<List<ModelItem>> getItemId(@Path("iditem") Integer iditem);

    @GET("item/search/{search}")
    Call<List<ModelItem>> searchItem(@Path("search") String search);
    // End of item

    // Start of beli
    @GET("beli")
    Call<List<ModelBeli>> getBeli();

    @GET("beli/get/{idbeli}")
    Call<List<ModelBeli>> getBeliById(@Path("idbeli") String idbeli);

    @GET("beli/{status}")
    Call<List<ModelBeli>> filterBeli(@Path("status") Integer status);

//    @POST("tblbeli/filterDateBeli.php")
//    Call<List<ModelBeli>> filterDateBeli(@Field("datebefore") String datebefore, @Field("dateafter") String dateafter);

    @FormUrlEncoded
    @POST("beli/{idbeli}")
    Call<ModelBeli> updateBeli(
            @Path("idbeli") Integer idbeli,
            @Field("bayar") String bayar,
            @Field("kembali") String kembali,
            @Field("status") String status
    );
    // End of beli

    // Start of beli detail
    @GET("belidetail/{idbeli}")
    Call<List<ModelBeliDetail>> getBeliDetail(@Path("idbeli") String idbeli);

//    @GET("belidetail")
//    Call<List<ModelBeliDetail>> getListDetail(@Field("idbeli") Integer idbeli);
    // End of beli detail
}
