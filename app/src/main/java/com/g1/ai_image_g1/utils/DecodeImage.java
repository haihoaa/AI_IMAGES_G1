package com.g1.ai_image_g1.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DecodeImage {
    public static File convertByteToFile(byte[] decodedImage) {
        try {
            File file = File.createTempFile("image", null);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decodedImage);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    public static String convertByteToBase64(Bitmap decodedImage){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        decodedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
