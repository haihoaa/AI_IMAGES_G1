package com.g1.ai_image_g1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private EditText promptEditText, stepsEditText;
    private ImageView generatedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        promptEditText = findViewById(R.id.inputPrompt);
        //stepsEditText = findViewById(R.id.steps_edittext);
        generatedImageView = findViewById(R.id.genImageView);
        Button generateButton = findViewById(R.id.genImage);
        generateButton.setOnClickListener(v -> generateImage());
        //setContentView(R.layout.generate_image);

        Button shareImage = findViewById(R.id.shareImage);
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) generatedImageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageAndText(bitmap);
            }
        });
    }

    private void shareImageAndText(Bitmap bitmap) {
        Uri uri = getImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Image Text");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Image Subject");
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            folder.mkdirs();
            File file = new File(folder, "image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(this, "com.g1.ai_image_g1", file);
        } catch (Exception ex) {
            ex.printStackTrace();
            //Toast.makeText(MainActivity.this,"", ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    private void generateImage() {
        //Log de debug
        Log.d("MainActivity", "generateImage() called");
        String prompt = promptEditText.getText().toString().trim();
        String steps = "20";
        String preparePromt = "(RAW photo, best quality), (realistic, photo-realistic:1.3), masterpiece, " +
                "an extremely delicate and beautiful, extremely detailed, CG, unity , 2k wallpaper, Amazing, " +
                "finely detail, extremely detailed CG unity 8k wallpaper, huge filesize, ultra-detailed, highres, " +
                "absurdres, soft light, beautiful detailed girl, detailed fingers, extremely detailed eyes and face, " +
                "beautiful detailed nose, detailed face, beautiful detailed eyes, light on face, looking at viewer, " +
                "1girl, cute, young, mature face, realistic face, realistic body, beautiful detailed thigh, " +
                "<lora:koreanDollLikeness:0.4>, ";
        String prepareNegative = "(nude:2), (nsfw:2), breast, (naked:2), paintings, sketches, (worst quality:2), (low quality:2), " +
                "(normal quality:2), lowres, ((monochrome)), ((grayscale)), skin spots, acnes, skin blemishes, age spot, " +
                "glans, extra fingers, fewer fingers, ((watermark:2)), (white letters:1), nipples, bad anatomy, " +
                "bad hands, text, error, missing fingers, missing arms, missing legs, extra digit, fewer digits, cropped, " +
                "worst quality, jpeg artifacts, signature, watermark, username, bad feet, {Multiple people}, blurry, " +
                "poorly drawn hands, poorly drawn face, mutation, deformed, extra limbs, extra arms, extra legs, " +
                "malformed limbs, fused fingers, too many fingers, long neck, cross-eyed, mutated hands, " +
                "polar lowres, bad body, bad proportions, gross proportions, wrong feet bottom render, " +
                "abdominal stretch, briefs, knickers, kecks, thong, {{fused fingers}}, {{bad body}}, " +
                "EasyNegative, bad proportion body to legs, wrong toes, extra toes, missing toes, weird toes, 2 body, pussy, 2 upper, 2 lower, 2 head, 3 hand, 3 feet, " +
                "extra long leg, super long leg, mirrored image, mirrored noise, aged up, old";
        String height = "768";
        String width = "512";
        if (prompt.isEmpty()) {
            Toast.makeText(this, "Please enter prompt", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("prompt", preparePromt + prompt);
            requestData.put("negative_prompt", prepareNegative);
            requestData.put("steps", steps);
            requestData.put("sampler_index", "DPM++ SDE Karras");
            requestData.put("sampler_name", "DPM++ SDE Karras");
            requestData.put("height", height);
            requestData.put("width", width);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new GenerateImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestData);
    }

    private class GenerateImageTask extends AsyncTask<JSONObject, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(JSONObject... jsonObjects) {
            Bitmap generatedImage = null;
            try {
                URL url = new URL("http://192.168.1.55:7860/sdapi/v1/txt2img");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                byte[] requestDataBytes = jsonObjects[0].toString().getBytes();
                connection.getOutputStream().write(requestDataBytes);

                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                byte[] responseDataBytes = outputStream.toByteArray();

                String responseJsonString = new String(responseDataBytes, StandardCharsets.UTF_8);
                Log.d("GenerateImageTask", "Server response: " + responseJsonString);

                JSONObject responseJson = new JSONObject(responseJsonString);

                String base64Image = responseJson.getJSONArray("images").getString(0);
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                InputStream imageInputStream = new ByteArrayInputStream(decodedString);
                generatedImage = BitmapFactory.decodeStream(imageInputStream);

                inputStream.close();
                outputStream.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return generatedImage;
        }
        public void uploadImage(Bitmap generatedImage){


        }

        @Override
        protected void onPostExecute(Bitmap generatedImage) {
            if (generatedImage != null) {
                generatedImageView.setImageBitmap(generatedImage);
                generatedImageView.setVisibility(View.VISIBLE);
            }
        }
    }
}
