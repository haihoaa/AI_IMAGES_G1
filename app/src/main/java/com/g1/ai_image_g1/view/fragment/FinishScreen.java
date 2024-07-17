package com.g1.ai_image_g1.view.fragment;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.g1.ai_image_g1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FinishScreen extends Fragment {
    private ImageView outputImage;
    private Button downloadButton;
    private ProgressBar progressBar;
    private TextView successText;
    CardView cardImage;
    private Bitmap generatedImage;
    private String imageWidth, imageHeight;
    private String imageUrl;

    public void bindingView(View view){
        outputImage = view.findViewById(R.id.outputImage);
        downloadButton = view.findViewById(R.id.download);
        progressBar = view.findViewById(R.id.progressBar);
        successText = view.findViewById(R.id.successText);
        cardImage = view.findViewById(R.id.CardImage);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish_screen, container, false);
        bindingView(view);
        progressBar.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            generatedImage = bundle.getParcelable("generatedImage", Bitmap.class);
            imageUrl = bundle.getString("imageUrl");
            imageHeight = bundle.getString("imageHeight");
            imageWidth = bundle.getString("imageWidth");
            outputImage.setImageBitmap(generatedImage);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) cardImage.getLayoutParams();
            if(Objects.equals(imageHeight, "768")){
                layoutParams.dimensionRatio = "H,2:3";
            }
            cardImage.setLayoutParams(layoutParams);
            bundle.clear();
        }
        downloadButton.setOnClickListener(v -> saveImage());
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_image) {
                navigateToImageScreen();
                return true;
            } else if (id == R.id.action_zoom) {
                navigateToZoomScreen();
                return true;

            }
            return false;
        });
        return view;
    }

    private void saveImage() {
        downloadButton.setClickable(false);
        downloadButton.setText("Đang lưu...");
        if (generatedImage == null) {
            Toast.makeText(getContext(), "Loi, ko co anh", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String filename = "image_" + System.currentTimeMillis() + ".png";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(directory, filename);

        new Thread(() -> {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                generatedImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();

                MediaScannerConnection.scanFile(getContext(), new String[]{file.getAbsolutePath()}, null,
                        (path, uri) -> new Handler(Looper.getMainLooper()).post(() -> {
                            progressBar.setVisibility(View.GONE);
                            successText.setVisibility(View.VISIBLE);
                            downloadButton.setClickable(true);
                            downloadButton.setText("Tải ảnh");
                            Toast.makeText(getContext(), "Da luu anh vao may", Toast.LENGTH_SHORT).show();
                        }));

            } catch (IOException e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Khong luu anh duoc", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void navigateToZoomScreen() {
        Navigation.findNavController(requireView()).navigate(R.id.action_downloader_to_extend);
    }

    private void navigateToImageScreen(){
        Navigation.findNavController(requireView()).navigate(R.id.action_downloader_to_gen);

    }
}