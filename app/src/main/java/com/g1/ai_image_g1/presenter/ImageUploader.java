package com.g1.ai_image_g1.presenter;

import com.g1.ai_image_g1.model.ImageModel;
import com.g1.ai_image_g1.utils.DecodeImage;

import androidx.annotation.NonNull;

import com.g1.ai_image_g1.network.RetrofitClient;
import com.g1.ai_image_g1.network.UploadService;

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
    public static void uploadImage(String base64Image, ImageModel imageProperty, UploadCallback callback) {
        String apiKey = "anh.moe_public_api";
        File imageFile = DecodeImage.convertBase64ToFile(base64Image);
        if (imageFile == null) {
            callback.onError("Failed to convert base64 to file");
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
                        imageProperty.setImageUrl(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    callback.onSuccess();
                } else {
                    callback.onError("Failed to upload image");
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onError("Network error while uploading image");
            }
        });
    }


    public interface UploadCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

}



