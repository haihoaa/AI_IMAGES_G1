package com.g1.ai_image_g1.network;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface GenImageService {
    @Headers({
            "accept: application/json",
            "Content-Type: application/json"
    })
    @POST("/sdapi/v1/txt2img")
    Call<ResponseBody> generateImage(@Body RequestBody requestData);
}


