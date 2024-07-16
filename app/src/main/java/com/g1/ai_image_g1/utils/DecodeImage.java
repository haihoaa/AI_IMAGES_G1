package com.g1.ai_image_g1.utils;

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
}
