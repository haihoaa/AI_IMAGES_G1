package com.g1.ai_image_g1.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g1.ai_image_g1.R;
import com.g1.ai_image_g1.presenter.GenerateImage;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements GenerateImageView, ShareImage {

    private EditText promptEditText;
    private ImageView generatedImageView;
    private GenerateImage presenter;
    private Bitmap generatedBitmap;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promptEditText = findViewById(R.id.inputPrompt);
        generatedImageView = findViewById(R.id.genImageView);
        Button generateButton = findViewById(R.id.genImage);
        Button shareButton = findViewById(R.id.shareImage);

        presenter = new GenerateImage(this);

        generateButton.setOnClickListener(v -> {
            String prompt = promptEditText.getText().toString().trim();
            presenter.generateImage(prompt);
        });

        shareButton.setOnClickListener(v -> {
            if (imageUrl != null && !imageUrl.isEmpty()) {
                shareImageUrl(imageUrl);
            } else {
                Toast.makeText(this, "No URL to share", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showGeneratedImage(Bitmap generatedImage) {
        generatedBitmap = generatedImage;
        generatedImageView.setImageBitmap(generatedImage);
        generatedImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void shareImage(Bitmap image) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/png");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), image, "Generated Image", null);
        Uri imageUri = Uri.parse(path);

        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(shareIntent, "Share Image"));
    }

    @Override
    public void shareImageUrl(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(shareIntent, "Share URL"));
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void updateImageUrl(String url) {
        this.imageUrl = url;
    }
}
