package com.g1.ai_image_g1.view;

import android.graphics.Bitmap;

public interface GenerateImageView {
    void showGeneratedImage(Bitmap generatedImage);
    void showError(String message);
    void showSuccessMessage(String message);
    //void getImageUrl(String imageUrl);
}
