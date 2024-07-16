package com.g1.ai_image_g1.view.fragment;

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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.g1.ai_image_g1.R;
import com.g1.ai_image_g1.api.GenerateImage;
import com.g1.ai_image_g1.model.ImageModel;
import com.g1.ai_image_g1.utils.ValidateInput;
import com.g1.ai_image_g1.view.ModelAdapter;

public class GenImageScreen extends Fragment {

    private EditText edPrompt, edNegativePrompt;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private Button btnGenerate;
    private ModelAdapter modelAdapter;

    public void findView(View view) {
        edPrompt = view.findViewById(R.id.edtPrompt);
        edNegativePrompt = view.findViewById(R.id.edtNegative);
        radioGroup = view.findViewById(R.id.radioGroup);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnGenerate = view.findViewById(R.id.btnGenerate);

    }

    private void setAdapter(View view) {
        modelAdapter = new ModelAdapter(position -> {
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(modelAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gen_image_screen, container, false);
        findView(view);
        setAdapter(view);
        btnGenerate.setOnClickListener(v -> generateImage());
        return view;
    }

    private void generateImage() {
        String prompt = edPrompt.getText().toString();
        if (!ValidateInput.validateInput(prompt)) {
            edPrompt.setError("Chua nhap noi dung");
            return;
        }
        prompt = ValidateInput.modifyPrompt(prompt);
        //String negativePrompt = edNegativePrompt.getText().toString();
        int model = modelAdapter.getSelectedModel();
        model = (model == -1) ? 0 : 1;
        int selectedId = radioGroup.getCheckedRadioButtonId();
        int width = 512, height = 512;
        prompt += model == 1 ? ", <lora:YA510:0.7>, " : "";
        prompt += model == 0 ? ", <lora:koreanDollLikeness_v15:0.4>, " : "";
        if (selectedId == R.id.wh256) {
            width = height = 256;
        } else if (selectedId == R.id.w512h768) {
            width = 512;
            height = 768;
        }


        ImageModel imageModel = new ImageModel();
        imageModel.setPrompt(prompt + imageModel.getPreparePrompt());
        imageModel.getPrepareNegative();
        imageModel.setSteps("20");
        imageModel.setWidth(String.valueOf(width));
        imageModel.setHeight(String.valueOf(height));

        GenerateImage generateImage = new GenerateImage();
        generateImage.generateImageApiCall(imageModel, new GenerateImage.GenerateImageCallback() {
            @Override
            public void generateSuccess(Bitmap generatedImage, String imageUrl) {
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

//            @Override
//            public void onUploadSuccess() {
//                // chua lam
//            }
        });
    }

    public void inputError(String errorMessage) {


    }

}