package com.example.orders.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServiceAddProductType {
    @POST("/addProductsType.php/")
    @FormUrlEncoded
    Call<ResponseBody> savePost(@Field("description") String description,
                                @Field("img") String img,
                                @Field("img_update") String img_update);
}
