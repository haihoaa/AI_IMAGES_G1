package com.g1.ai_image_g1.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.g1.ai_image_g1.R;
import com.g1.ai_image_g1.presenter.GenerateImage;

public class MainActivity extends AppCompatActivity implements GenerateImageView {

    private EditText promptEditText;
    private ImageView generatedImageView;
    private GenerateImage presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        promptEditText = findViewById(R.id.inputPrompt);
        generatedImageView = findViewById(R.id.genImageView);
        Button generateButton = findViewById(R.id.genImage);
        Button btnSaveImage = findViewById(R.id.btnSaveToPhone);

        presenter = new GenerateImage(this);
        btnSaveImage.setOnClickListener(v -> {
            // Check if an image has been generated
            if (generatedImageView.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) generatedImageView.getDrawable()).getBitmap();
//                if (saveImageToGallery(bitmap)) {
//                    Toast.makeText(MainActivity.this, "Image saved to gallery", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Failed to save image", Toast.LENGTH_SHORT).show();
//                }
            }
//            else {
//                Toast.makeText(MainActivity.this, "No image to save", Toast.LENGTH_SHORT).show();
//            }
        });
        generateButton.setOnClickListener(v -> {
            String prompt = promptEditText.getText().toString().trim();
            presenter.generateImage(prompt);
        });
    }


    @Override
    public void showGeneratedImage(Bitmap generatedImage) {
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

}
