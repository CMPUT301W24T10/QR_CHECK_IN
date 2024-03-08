package com.example.qr_check_in;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qr_check_in.data.FirebaseImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ImageListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    public ImageListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        recyclerView = view.findViewById(R.id.rv_images);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // for a grid with 3 columns
        imageAdapter = new ImageAdapter(new ArrayList<>());
        recyclerView.setAdapter(imageAdapter);

        // Fetch and display images
        new FirebaseImageLoader().loadImageUrls("event_posters/", new FirebaseImageLoader.ImageUrlsFetchedListener() {
            @Override
            public void onImageUrlsFetched(List<String> imageUrls) {
                imageAdapter.setImageUrls(imageUrls);
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                // Handle any errors
            }
        });

        return view;
    }
}
