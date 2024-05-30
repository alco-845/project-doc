package com.example.storyapp.data.remote.retrofit

import com.example.storyapp.data.remote.response.DataResponse
import com.example.storyapp.data.remote.response.DetailStory
import com.example.storyapp.data.remote.response.ListStory
import com.example.storyapp.data.remote.response.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun regisUser (
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<DataResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser (
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<User>

    @GET("stories")
    fun getListStory(
        @Header("Authorization") token: String
    ): Call<ListStory>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): Call<DataResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStory>
}