package com.example.orders.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServiceUpdateProductType {
    @POST("/updateProductType.php/")
    @FormUrlEncoded
    Call<ResponseBody> savePost(@Field("id") long id,
                                @Field("description") String type,
                                @Field("img") String img,
                                @Field("img_update") String img_update);
}
