package com.example.orders.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIServiceDeleteOrder {
    @GET("/api/post/deleteOrder.php?")
    Call<ResponseBody> deleteOrder(@Query("id") long id);
}
