package com.g1.ai_image_g1.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.g1.ai_image_g1.R;
import com.g1.ai_image_g1.api.ExtendImage;
import com.g1.ai_image_g1.model.ImageModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class ExtendImageScreen extends Fragment {

    private RadioGroup radioGroup;
    private ImageView slImage;
    private Button extendBtn;
    private Uri selectedImageUri;

    public void bindingView(View view) {
        radioGroup = view.findViewById(R.id.radioGroup);
        slImage = view.findViewById(R.id.selectedImageView);
        extendBtn = view.findViewById(R.id.btnExtend);
    }

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    slImage.setImageURI(selectedImageUri);
                }
            }
    );

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extand_image_screen, container, false);
        bindingView(view);
        slImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });
        extendBtn.setOnClickListener(v -> {
            try {
                extendImage();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_image) {
                navigateToImageScreen();
                return true;
            }
            return false;
        });

        return view;
    }

    private void extendImage() throws FileNotFoundException {
        ExtendImage extendImage = new ExtendImage();
        ImageModel imageProperty = new ImageModel();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        int extendSize = 0;
        if (selectedId == R.id.doubleTimes) {
             extendSize = 2;
        }else{
            Toast.makeText(getContext(), "Chua chon kich thuoc", Toast.LENGTH_SHORT).show();
            return;
        }
        slImage.setClickable(false);
        extendBtn.setClickable(false);
        extendBtn.setText("Đang mở rộng ảnh...");
        InputStream imageStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
        Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
        extendImage.extendImageApiCall(imageBitmap, extendSize, imageProperty, new ExtendImage.ExtendImageCallback() {

            @Override
            public void extendSuccess(Bitmap extendedImage, String imageUrl) {
                slImage.setClickable(true);
                extendBtn.setClickable(true);
                extendBtn.setText("Mở rộng ảnh");
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl", imageUrl);
                bundle.putParcelable("generatedImage", extendedImage);
                bundle.putString("imageWidth", "512");
                bundle.putString("imageUrl", "512");
                Navigation.findNavController(requireView()).navigate(R.id.action_extend_to_downloader, bundle);
            }

            @Override
            public void extendError(String errorMessage) {
                Log.d("1", "extendError");
            }
        });
    }

    private void navigateToImageScreen() {
        Navigation.findNavController(requireView()).navigate(R.id.action_extend_to_gen);
    }
}