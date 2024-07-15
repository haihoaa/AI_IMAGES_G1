package com.g1.ai_image_g1.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {

    @Multipart
    @POST("/api/1/upload")
    Call<ResponseBody> uploadImage(
            @Header("X-API-Key") String apiKey,
            @Part MultipartBody.Part source,
            @Part("format") RequestBody format
    );
}