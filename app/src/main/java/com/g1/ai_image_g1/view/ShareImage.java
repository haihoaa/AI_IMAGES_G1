package com.g1.ai_image_g1.view;

import android.graphics.Bitmap;

public interface ShareImage {
    void shareImage(Bitmap image);
    void shareImageUrl(String imageUrl);
    String getImageUrl();
}
