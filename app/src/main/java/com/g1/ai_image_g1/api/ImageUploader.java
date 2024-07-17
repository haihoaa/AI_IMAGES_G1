package com.g1.ai_image_g1.api;

import androidx.annotation.NonNull;

import com.g1.ai_image_g1.model.ImageModel;
import com.g1.ai_image_g1.network.RetrofitClient;
import com.g1.ai_image_g1.network.UploadService;
import com.g1.ai_image_g1.utils.DecodeImage;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageUploader {
    public static void uploadImage(byte[] decodedImage, ImageModel imageProperty, UploadCallback callback) {
        String apiKey = "chv_c0Cp_b215c8781434cbb2d8287945b3124c05503738253bf9ad336fe9df27fc23b167e3b6ab29a5762fe80a1cd756511d80a4c5b6992e2301f833c4f3ca7ef68a41c2";
        File imageFile = DecodeImage.convertByteToFile(decodedImage);
        if (imageFile == null) {
            callback.onError("Khong chuyen doi base64 thanh anh duoc");
            return;
        }
        RequestBody requestFile = RequestBody.create(imageFile, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("source", imageFile.getName(), requestFile);
        RequestBody formatPart = RequestBody.create("txt", MediaType.parse("text/plain"));

        Call<ResponseBody> call = RetrofitClient.getUploadClient().create(UploadService.class).uploadImage(apiKey, imagePart, formatPart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String imageUrl = response.body().string();
                        imageProperty.setImageUrl(imageUrl);
                        callback.onSuccess(imageUrl);
                    } catch (IOException e) {
                        callback.onError("Loi khong upload duoc");
                    }
                } else {
                    callback.onError("Loi khong upload duoc");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onError("Loi ket noi");
            }
        });
    }

    public interface UploadCallback {
        void onSuccess(String imageUrl);

        void onError(String errorMessage);
    }
}