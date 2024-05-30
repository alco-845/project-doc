package com.example.birdbsuser.koneksi;

import com.example.birdbsuser.model.ModelBeli;
import com.example.birdbsuser.model.ModelBeliDetail;
import com.example.birdbsuser.model.ModelItem;
import com.example.birdbsuser.model.ModelPembeli;

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
    // Start of pembeli
    @FormUrlEncoded
    @POST("pembeli")
    Call<ModelPembeli> insertPembeli(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @FormUrlEncoded
    @POST("pembeli/{idpembeli}")
    Call<ModelPembeli> updatePembeli(
            @Path("idpembeli") Integer idpembeli,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telpon") String telpon
    );

    @GET("pembeli/get/{idpembeli}")
    Call<List<ModelPembeli>> getPembeliId(@Path("idpembeli") Integer idpembeli);

    @GET("pembeli/{username}")
    Call<List<ModelPembeli>> getByName(@Path("username") String username);
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

    @GET("beli/{idpembeli}/{status}")
    Call<List<ModelBeli>> getBeliTwoParams(@Path("idpembeli") Integer idpembeli, @Path("status") Integer status);

    @GET("beli/last")
    Call<ModelBeli> getBeliLast();

    @GET("beli/{status}")
    Call<List<ModelBeli>> filterBeli(@Path("status") Integer status);

//    @POST("tblbeli/filterDateBeli.php")
//    Call<List<ModelBeli>> filterDateBeli(@Field("datebefore") String datebefore, @Field("dateafter") String dateafter);

    @FormUrlEncoded
    @POST("beli")
    Call<ModelBeli> insertBeli(
            @Field("idbeli") Integer idbeli,
            @Field("idpembeli") Integer idpembeli,
            @Field("idpenjual") Integer idpenjual,
            @Field("tglbeli") String tglbeli,
            @Field("total") Integer total,
            @Field("bayar") String bayar,
            @Field("kembali") String kembali,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("beli/{idbeli}")
    Call<ModelBeli> updateBeli(
            @Path("idbeli") Integer idbeli,
            @Field("total") Integer total
    );

    @FormUrlEncoded
    @POST("beli/{idbeli}")
    Call<ModelBeli> updateBeliStatus(
            @Path("idbeli") Integer idbeli,
            @Field("status") Integer status
    );
    // End of beli

    // Start of beli detail
    @GET("belidetail/{idbeli}")
    Call<List<ModelBeliDetail>> getBeliDetail(@Path("idbeli") Integer idbeli);

    @GET("belidetail/get/{idbelidetail}")
    Call<List<ModelBeliDetail>> getBeliDetailById(@Path("idbelidetail") Integer idbelidetail);

    @GET("belidetail/{idbeli}/{iditem}")
    Call<List<ModelBeliDetail>> getBeliDetailTwoParams(@Path("idbeli") Integer idbeli, @Path("iditem") Integer iditem);

    @FormUrlEncoded
    @POST("belidetail")
    Call<ModelBeliDetail> insertBeliDetail(
            @Field("idbeli") Integer idbeli,
            @Field("iditem") Integer iditem,
            @Field("jumlah") Integer jumlah,
            @Field("harga") Integer harga
    );

    @FormUrlEncoded
    @POST("belidetail/{idbelidetail}")
    Call<ModelBeliDetail> updateBeliDetail(
            @Path("idbelidetail") Integer idbelidetail,
            @Field("jumlah") Integer jumlah,
            @Field("harga") Integer harga
    );

    @DELETE("belidetail/{idbelidetail}")
    Call<ModelBeliDetail> deleteBeliDetail(@Path("idbelidetail") Integer idbelidetail);
// End of beli detail
}