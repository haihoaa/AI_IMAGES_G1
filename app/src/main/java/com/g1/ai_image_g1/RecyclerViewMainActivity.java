package com.g1.ai_image_g1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewMainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private List<String> mImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Thiết lập layout lưới 2 cột

        mImageUrls = new ArrayList<>(); // Danh sách URL ảnh từ API, bạn cần lấy dữ liệu từ API và thêm vào mImageUrls

        mAdapter = new ImageAdapter(this, mImageUrls);
        mRecyclerView.setAdapter(mAdapter);

        // Phương thức để lấy dữ liệu từ API và thêm vào mImageUrls
        fetchImageDataFromAPI();
    }

    private void fetchImageDataFromAPI() {
        // Thực hiện lấy dữ liệu từ API và cập nhật mImageUrls
        // Ví dụ:
        // JSONArray jsonArray = ... // Kết quả từ API
        // for (int i = 0; i < jsonArray.length(); i++) {
        //     JSONObject jsonObject = jsonArray.getJSONObject(i);
        //     String imageUrl = jsonObject.getString("image_url");
        //     mImageUrls.add(imageUrl);
        // }
        // Sau khi có dữ liệu, cập nhật Adapter
        mAdapter.notifyDataSetChanged();
    }
}
