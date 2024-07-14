package com.g1.ai_image_g1.network;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class StreamConverterFactory extends Converter.Factory {

    public static StreamConverterFactory create() {
        return new StreamConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        return (Converter<ResponseBody, InputStream>) ResponseBody::byteStream;
    }
}

