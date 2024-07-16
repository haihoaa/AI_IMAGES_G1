package com.g1.ai_image_g1.api;

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
        String apiKey = "anh.moe_public_api";
        File imageFile = DecodeImage.convertByteToFile(decodedImage);
        if (imageFile == null) {
            callback.onError("Failed to convert base64 to file");
            return;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("source", imageFile.getName(), requestFile);
        RequestBody formatPart = RequestBody.create(MediaType.parse("text/plain"), "txt");

        Call<ResponseBody> call = RetrofitClient.getUploadClient().create(UploadService.class).uploadImage(apiKey, imagePart, formatPart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String imageUrl = response.body().string();
                        imageProperty.setImageUrl(imageUrl);
                        callback.onSuccess(imageUrl);
                    } catch (IOException e) {
                        callback.onError("Failed to upload image");
                    }
                } else {
                    callback.onError("Failed to upload image");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError("Network error while uploading image");
            }
        });
    }

    public interface UploadCallback {
        void onSuccess(String imageUrl);
        void onError(String errorMessage);
    }
}