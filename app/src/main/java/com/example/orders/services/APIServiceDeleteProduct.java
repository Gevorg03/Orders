package com.example.orders.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIServiceDeleteProduct {
    @POST("/api/post/deleteProduct.php")
    @FormUrlEncoded
    Call<ResponseBody> deletePost(@Field("id") long id);
}
