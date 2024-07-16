package com.g1.ai_image_g1.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.g1.ai_image_g1.R;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ModelViewHolder> {

    private final String[] modelNames = {"Korean Girl", "Beautiful Girl" , "Anime"};
    private int selectedPosition = -1;
    private final OnItemClickListener onItemClickListener;

    public ModelAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_models, parent, false);
        return new ModelViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        holder.modelName.setText(modelNames[position]);
        holder.checkBox.setChecked(position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return modelNames.length;
    }

    public  int getSelectedModel() {
            return selectedPosition;

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder {
        ImageView modelImage;
        TextView modelName;
        CheckBox checkBox;

        ModelViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            modelImage = itemView.findViewById(R.id.modelImages);
            modelName = itemView.findViewById(R.id.modelName);
            checkBox = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }

                notifyItemChanged(selectedPosition);
                selectedPosition = position;
                notifyItemChanged(selectedPosition);
                onItemClickListener.onItemClick(position);
            });
        }
    }
}