package com.g1.ai_image_g1.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.g1.ai_image_g1.model.ImageModel;
import com.g1.ai_image_g1.network.ExtendImageService;
import com.g1.ai_image_g1.network.RetrofitClient;
import com.g1.ai_image_g1.utils.DecodeImage;
import com.g1.ai_image_g1.utils.StreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtendImage {
    private final ExtendImageService service;
    public ExtendImage() {
        this.service = RetrofitClient.getGenerateClient().create(ExtendImageService.class);
    }

    public void extendImageApiCall(Bitmap image, int extendSize, ImageModel imageProperty, ExtendImageCallback callback) {
        String base64Image = DecodeImage.convertByteToBase64(image);
        RequestBody requestData = createRqData(base64Image, extendSize);
        Log.d("2", "requestData: " + requestData);
        Call<ResponseBody> callExtend = service.extendImage(requestData);
        callExtend.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (InputStream inputStream = response.body().byteStream()) {
                        String responseString = StreamReader.readStream(inputStream);
                        JSONObject responseBody = new JSONObject(responseString);
                        String base64Image = responseBody.getString("image");
                        byte[] decodedImage = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap generatedImage = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
                        handleUploadImage(decodedImage, generatedImage, imageProperty, callback);
                    } catch (IOException | JSONException e) {
                        callback.extendError("Error reading response");
                    }
                } else {
                    callback.extendError("Failed to generate image: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.extendError("Network error");
            }
        });
    }

    private RequestBody createRqData(String base64Image, int extendSize) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("upscaling_resize", extendSize);
            requestData.put("upscaler_1", "4x-UltraSharp");
            requestData.put("image", "data:image/png;base64,"+ base64Image);

        } catch (JSONException ignored) {
        }
        return RequestBody.create(requestData.toString(), MediaType.parse("application/json"));
    }

    private void handleUploadImage(byte[] decodedImage, Bitmap extendedImage, ImageModel image, ExtendImage.ExtendImageCallback callback) {
        ImageUploader.uploadImage(decodedImage, image, new ImageUploader.UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                callback.extendSuccess(extendedImage,image.getImageUrl());
            }

            @Override
            public void onError(String errorMessage) {
                callback.extendError(errorMessage);
            }
        });
    }

    public interface ExtendImageCallback {
        void extendSuccess(Bitmap extendedImage, String imageUrl);
        void extendError(String errorMessage);
//        void onUploadSuccess();
    }
}
