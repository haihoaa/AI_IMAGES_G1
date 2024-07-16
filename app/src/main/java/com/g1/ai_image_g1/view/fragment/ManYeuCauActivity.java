package com.g1.ai_image_g1.view.fragment;

import static com.g1.ai_image_g1.R.id.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.g1.ai_image_g1.R;
import com.g1.ai_image_g1.view.MainActivity;

public class ManYeuCauActivity extends AppCompatActivity {

    TextView addmotion1;
    TextView addmotion2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_yeu_cau);

        addmotion1 = findViewById(R.id.addmotion1);
        addmotion2 = findViewById(R.id.addmotion2);

        addmotion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManYeuCauActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        addmotion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManYeuCauActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}