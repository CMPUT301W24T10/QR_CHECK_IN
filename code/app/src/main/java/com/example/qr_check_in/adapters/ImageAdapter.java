package com.example.qr_check_in.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qr_check_in.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> imageUrls;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(String imageUrl, int position);
    }

    public ImageAdapter(List<String> imageUrls, OnItemClickListener listener) {
        this.imageUrls = imageUrls;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public Button deleteButton;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageview);
            deleteButton = view.findViewById(R.id.delete_button); // Ensure you have a delete button in your item layout
        }

        public void bind(final String imageUrl, final int position, final OnItemClickListener listener) {
            // Load the image with Glide
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .into(imageView);

            // Set the delete button click listener
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(imageUrl, position));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        holder.bind(imageUrl, position, listener);
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    // Helper method to remove an item from the adapter
    public void removeAt(int position) {
        imageUrls.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, imageUrls.size());
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged(); // Notify the adapter to refresh the view
    }

    public void updateItemAt(int position, String imageUrl) {
        imageUrls.set(position, imageUrl);
        notifyItemChanged(position);
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

}
