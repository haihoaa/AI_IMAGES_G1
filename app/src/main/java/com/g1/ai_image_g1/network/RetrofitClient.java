package com.g1.ai_image_g1.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String GENERATE_URL = "http://192.168.1.55:7890";
    private static final String UPLOAD_URL = "https://anh.moe/api/1/upload/";

    public static Retrofit getGenerateClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .baseUrl(GENERATE_URL)
                .client(okHttpClient)
                .addConverterFactory(StreamConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getUploadClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(3, TimeUnit.MINUTES);
        builder.readTimeout(1, TimeUnit.MINUTES);
        builder.writeTimeout(1, TimeUnit.MINUTES);
        OkHttpClient okHttpClient = builder
                .build();
        return new Retrofit.Builder()
                .baseUrl(UPLOAD_URL).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
