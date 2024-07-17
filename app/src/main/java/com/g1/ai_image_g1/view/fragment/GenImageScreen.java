package com.g1.ai_image_g1.view.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.g1.ai_image_g1.R;
import com.g1.ai_image_g1.api.GenerateImage;
import com.g1.ai_image_g1.model.ImageModel;
import com.g1.ai_image_g1.utils.ValidateInput;
import com.g1.ai_image_g1.view.ModelAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GenImageScreen extends Fragment {

    private EditText edPrompt;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private Button btnGenerate;
    private ModelAdapter modelAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gen_image_screen, container, false);
        bindingView(view);
        recyclerView = view.findViewById(R.id.recyclerView);
        setupRecyclerView();
        btnGenerate.setOnClickListener(v -> generateImage());
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_zoom) {
                navigateToExtendScreen();
                return true;
            }
            return false;
        });
        return view;
    }

    private void bindingView(View view) {
        edPrompt = view.findViewById(R.id.edtPrompt);
        radioGroup = view.findViewById(R.id.radioGroup);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnGenerate = view.findViewById(R.id.btnGenerate);
    }


    private void setupRecyclerView() {
        modelAdapter = new ModelAdapter(position -> modelAdapter.setSelectedPosition(position));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(modelAdapter);
    }


    @SuppressLint("SetTextI18n")
    private void generateImage() {
        String prompt = edPrompt.getText().toString();
        if (!ValidateInput.validateInput(prompt)) {
            edPrompt.setError("Hay nhap dung mo ta");
            return;
        }
        prompt = ValidateInput.modifyPrompt(prompt);

        String model = modelAdapter.getSelectedModel();
        if (model.equals("Korean Girl")) {
            prompt += ", <lora:koreanDollLikeness_v15:0.4>, ";
        } else {
            prompt += ", <lora:YA510:0.7>, ";
        }

        int width = 512, height = 512;
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.wh256) {
            width = height = 256;
        } else if (selectedId == R.id.w512h768) {
            height = 768;
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setPrompt(imageModel.getPreparePrompt() + prompt);
        imageModel.setSteps("20");
        imageModel.setWidth(String.valueOf(width));
        imageModel.setHeight(String.valueOf(height));

        GenerateImage generateImage = new GenerateImage();

        btnGenerate.setText("Đang tạo ảnh...");
        btnGenerate.setClickable(false);
        generateImage.generateImageApiCall(imageModel, new GenerateImage.GenerateImageCallback() {
            @Override
            public void generateSuccess(Bitmap generatedImage, String imageUrl) {
                btnGenerate.setClickable(true);
                btnGenerate.setText("Tạo ảnh...");
                Bundle bundle = new Bundle();
                bundle.putString("imageUrl", imageUrl);
                bundle.putParcelable("generatedImage", generatedImage);
                bundle.putString("imageWidth", imageModel.getWidth());
                bundle.putString("imageHeight", imageModel.getHeight());
                Navigation.findNavController(requireView()).navigate(R.id.action_home2_to_downloader, bundle);
            }

            @Override
            public void generateError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void navigateToExtendScreen() {
        Navigation.findNavController(requireView()).navigate(R.id.action_gen_to_extend);
    }
}