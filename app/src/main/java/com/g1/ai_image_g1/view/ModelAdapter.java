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

    private final String[] modelNames = {"Korean Girl", "Beautiful Girl"};
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
        int drawableResId;
        String modelName = modelNames[position];
        switch (modelName) {
            case "Korean Girl":
                drawableResId = R.drawable.korean;
                break;
            case "Beautiful Girl":
                drawableResId = R.drawable.girl;
                break;
            default:
                drawableResId = R.drawable.girl;
                break;
        }
        holder.modelImage.setImageResource(drawableResId);

        holder.checkBox.setChecked(position == selectedPosition);

        holder.checkBox.setOnClickListener(v -> {
            setSelectedPosition(position);
            onItemClickListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return modelNames.length;
    }

    public String getSelectedModel() {
        if (selectedPosition != -1 && selectedPosition < modelNames.length) {
            return modelNames[selectedPosition];
        } else {
            return "";
        }
    }

    public void setSelectedPosition(int position) {
        int previousSelected = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousSelected);
        notifyItemChanged(selectedPosition);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        ImageView modelImage;
        TextView modelName;
        CheckBox checkBox;

        public ModelViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            modelImage = itemView.findViewById(R.id.modelImages);
            modelName = itemView.findViewById(R.id.modelName);
            checkBox = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            });
        }
    }
}