package com.g1.ai_image_g1.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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
import com.g1.ai_image_g1.view.ModelAdapter;

public class GenImageScreen extends Fragment {

    private EditText edPrompt, edNegativePrompt;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private Button btnGenerate;
    private final String[] modelNames = {"Anime", "Girls", "Animal"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gen_image_screen, container, false);

        edPrompt = view.findViewById(R.id.edtPrompt);
        edNegativePrompt = view.findViewById(R.id.edtNegative);
        radioGroup = view.findViewById(R.id.radioGroup);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnGenerate = view.findViewById(R.id.btnGenerate);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ModelAdapter(position -> {
            // chua lam
        }));

        btnGenerate.setOnClickListener(v -> generateImage());

        return view;
    }

    private void generateImage() {
        String prompt = edPrompt.getText().toString();
        //String negativePrompt = edNegativePrompt.getText().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();
//        int width = 512, height = 512;
//
//        if (selectedId == R.id.w256h256) {
//            width = height = 256;
//        } else if (selectedId == R.id.w1024h1024) {
//            width = height = 1024;
//        }

        ImageModel imageModel = new ImageModel();
        imageModel.setPrompt(prompt);
        imageModel.getPrepareNegative();
        imageModel.setSteps("20");
        imageModel.getWidth();
        imageModel.getHeight();
        imageModel.setImageUrl("");

        GenerateImage generateImage = new GenerateImage();
        generateImage.generateImage(imageModel, new GenerateImage.GenerateImageCallback() {
            @Override
            public void generateSuccess(Bitmap generatedImage, String imageUrl) {
                Bundle bundle = new Bundle();
                Log.d("Home", "Home-IMGURL: " + imageUrl);
                bundle.putString("imageUrl", imageUrl);
                bundle.putParcelable("generatedImage", generatedImage);
                Navigation.findNavController(requireView()).navigate(R.id.action_home2_to_downloader, bundle);
            }

            @Override
            public void generateError(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }

//            @Override
//            public void onUploadSuccess() {
//                // chua lam
//            }
        });
    }
}