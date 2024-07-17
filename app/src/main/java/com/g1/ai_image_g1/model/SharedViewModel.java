package com.g1.ai_image_g1.model;

import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private String imagePath;

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    public String getImagePath() {
        return imagePath;
    }
}
