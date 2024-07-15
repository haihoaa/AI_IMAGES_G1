package com.g1.ai_image_g1.utils;

import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DecodeImage {
    public static File convertBase64ToFile(String base64Image) {
        try {
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            File file = File.createTempFile("image", null);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decodedBytes);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            return null;
        }
    }
}
