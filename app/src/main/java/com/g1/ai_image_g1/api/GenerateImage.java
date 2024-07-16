package com.g1.ai_image_g1.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.g1.ai_image_g1.model.ImageModel;
import com.g1.ai_image_g1.network.GenImageService;
import com.g1.ai_image_g1.network.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateImage {

    private final GenImageService service;

    public GenerateImage() {
        this.service = RetrofitClient.getGenerateClient().create(GenImageService.class);
    }

    public void generateImageApiCall(ImageModel image, GenerateImageCallback callback) {
        RequestBody requestData = createRqData(image);
        Call<ResponseBody> callGenerate = service.generateImage(requestData);
        callGenerate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try (InputStream inputStream = response.body().byteStream()) {
                        String responseString = readStream(inputStream);
                        JSONObject responseBody = new JSONObject(responseString);
                        String base64Image = responseBody.getJSONArray("images").getString(0);
                        byte[] decodedImage = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap generatedImage = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
                        handleUploadImage(decodedImage,generatedImage, image, callback);
                    } catch (IOException | JSONException e) {
                        callback.generateError("Error reading response");
                    }
                } else {
                    callback.generateError("Failed to generate image: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.generateError("Network error");
            }
        });
    }

    private RequestBody createRqData(ImageModel image) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("prompt", image.getPrompt());
            requestData.put("negative_prompt", image.getPrepareNegative());
            requestData.put("steps", image.getSteps());
            requestData.put("sampler_index", image.getSamplerIndex());
            requestData.put("sampler_name", image.getSamplerName());
            requestData.put("height", image.getHeight());
            requestData.put("width", image.getWidth());
        } catch (JSONException ignored) {
        }
        return RequestBody.create(MediaType.parse("application/json"), requestData.toString());
    }

    private void handleUploadImage(byte[] decodedImage, Bitmap generatedImage, ImageModel image, GenerateImageCallback callback) {
        ImageUploader.uploadImage(decodedImage, image, new ImageUploader.UploadCallback() {
            @Override
            public void onSuccess(String imageUrl) {
                callback.generateSuccess(generatedImage,image.getImageUrl());
            }

            @Override
            public void onError(String errorMessage) {
                callback.generateError(errorMessage);
            }
        });
    }

    private String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public interface GenerateImageCallback {
        void generateSuccess(Bitmap generatedImage, String imageUrl);
        void generateError(String errorMessage);
//        void onUploadSuccess();
    }
}

