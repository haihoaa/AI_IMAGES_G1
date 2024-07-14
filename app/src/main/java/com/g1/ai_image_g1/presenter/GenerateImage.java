package com.g1.ai_image_g1.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.g1.ai_image_g1.network.GenImageService;
import com.g1.ai_image_g1.network.RetrofitClient;
import com.g1.ai_image_g1.view.GenerateImageView;
import com.g1.ai_image_g1.model.ImageModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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

    private final GenerateImageView view;
    private final GenImageService service;

    public GenerateImage(GenerateImageView view) {
        this.view = view;
        this.service = RetrofitClient.getGenerateClient().create(GenImageService.class);
    }

    public void generateImage(String prompt) {
        ImageModel imageProperty = new ImageModel();
        String steps = "20";
        imageProperty.setPrompt(imageProperty.getPreparePrompt() + prompt);
        imageProperty.setSteps(steps);
        RequestBody requestData = createRqData(imageProperty);
        Call<ResponseBody> callGenerate = service.generateImage(requestData);
        callGenerate.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try (InputStream inputStream = response.body().byteStream()) {
                            String responseString = readStream(inputStream);
                            JSONObject responseBody = new JSONObject(responseString);
                            handleResponse(responseBody, imageProperty);
                        } catch (IOException | JSONException e) {
                            view.showError("Error reading response");
                        }
                    } else {
                        view.showError("Empty response body");
                    }
                } else {
                    view.showError("Failed to generate image: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                view.showError("Network error");
            }
        });
    }

    private RequestBody createRqData(ImageModel imageProperty) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("prompt", imageProperty.getPrompt());
            requestData.put("negative_prompt", imageProperty.getPrepareNegative());
            requestData.put("steps", imageProperty.getSteps());
            requestData.put("sampler_index", imageProperty.getSamplerIndex());
            requestData.put("sampler_name", imageProperty.getSamplerName());
            requestData.put("height", imageProperty.getHeight());
            requestData.put("width", imageProperty.getWidth());
        } catch (JSONException ignored) {
        }
        return RequestBody.create(requestData.toString(),MediaType.parse("application/json"));
    }

    private void handleResponse(JSONObject responseBody, ImageModel imageProperty) throws JSONException {
        Log.d("Response", "handleResponse: " + responseBody);
        String base64Image = responseBody.getJSONArray("images").getString(0);
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        InputStream imageInputStream = new ByteArrayInputStream(decodedString);
        Bitmap generatedImage = BitmapFactory.decodeStream(imageInputStream);
        handleUploadImage(base64Image, imageProperty);
        view.showGeneratedImage(generatedImage);
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

    private void handleUploadImage(String base64Image, ImageModel imageProperty) {
        ImageUploader.uploadImage(base64Image, imageProperty, new ImageUploader.UploadCallback() {
            @Override
            public void onSuccess() {
                view.showSuccessMessage("Image uploaded successfully");
            }

            @Override
            public void onError(String errorMessage) {
                view.showError(errorMessage);
            }

        });
    }
}