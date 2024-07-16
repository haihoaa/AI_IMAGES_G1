package com.g1.ai_image_g1.view.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.g1.ai_image_g1.R;

import org.jetbrains.annotations.Nullable;

import java.io.File;

public class FinishScreen extends Fragment {
    private ImageView outputImage;
    private Button downloadButton;
    private ProgressBar progressBar;
    private String imageUrl;
    private TextView successText;
    CardView cardImage;

    public void bindingView(View view){
        outputImage = view.findViewById(R.id.outputImage);
        downloadButton = view.findViewById(R.id.download);
        progressBar = view.findViewById(R.id.progressBar);
        successText = view.findViewById(R.id.successText);
        cardImage = view.findViewById(R.id.CardImage);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish_screen, container, false);
        bindingView(view);
        progressBar.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Bitmap generatedImage = bundle.getParcelable("generatedImage", Bitmap.class);
            imageUrl = bundle.getString("imageUrl");
            outputImage.setImageBitmap(generatedImage);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) cardImage.getLayoutParams();
            if(bundle.getString("imageHeight") == "768"){
                layoutParams.dimensionRatio = "H,2:3";
            }
            cardImage.setLayoutParams(layoutParams);
            bundle.clear();
        }
        downloadButton.setOnClickListener(v -> downloadImageNew("1",imageUrl));
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(requireView()).navigateUp();
            }
        });
        return view;
    }
    private void downloadImageNew(String filename, String downloadUrlOfImage) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            DownloadManager dm = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/png")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + filename + ".png");

            dm.enqueue(request);

            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    progressBar.setVisibility(View.GONE);
                    successText.setVisibility(View.VISIBLE);

                    requireActivity().unregisterReceiver(this);
                }
            };

            requireActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED);

            Toast.makeText(getContext(), "Đang tải ảnh về máy...", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi kết nối: Không tải đc", Toast.LENGTH_SHORT).show();
        }
    }
}