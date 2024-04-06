package com.example.qr_check_in;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;
import java.util.List;

public class ImageListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        recyclerView = view.findViewById(R.id.rv_images);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        imageAdapter = new ImageAdapter(new ArrayList<>(), this::onDeleteClick);
        recyclerView.setAdapter(imageAdapter);

        fetchImagesFromFirestore();

        return view;
    }

    // Method to fetch images from Firestore
    private void fetchImagesFromFirestore() {
        // Fetch profile images
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> fetchedUrls = new ArrayList<>(imageAdapter.getImageUrls());
                task.getResult().forEach(document -> {
                    String imageUrl = document.getString("profileImageUrl");
                    if (imageUrl != null) {
                        fetchedUrls.add(imageUrl);
                    }
                });
                imageAdapter.setImageUrls(fetchedUrls); // Update adapter with fetched URLs
            } else {
                // Handle errors
            }
        });

        // Fetch event posters
        db.collection("events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> fetchedUrls = new ArrayList<>(imageAdapter.getImageUrls());
                task.getResult().forEach(document -> {
                    String imageUrl = document.getString("posterUrl");
                    if (imageUrl != null) {
                        fetchedUrls.add(imageUrl);
                    }
                });
                imageAdapter.setImageUrls(fetchedUrls); // Update adapter with fetched URLs
            } else {
                // Handle errors
            }
        });
    }
    // Method called when the delete button is clicked
    private void onDeleteClick(String imageUrl, int position) {
        String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl).delete().addOnSuccessListener(aVoid -> {
            // Determine the collection to update based on the image path
            String collectionPath = imageUrl.contains("profile_images") ? "users" : "events";
            String documentField = imageUrl.contains("profile_images") ? "profileImageUrl" : "posterUrl";

            // Find the document with the matching image URL
            db.collection(collectionPath).whereEqualTo(documentField, imageUrl).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String docId = queryDocumentSnapshots.getDocuments().get(0).getId();
                            db.collection(collectionPath).document(docId)
                                    .update(documentField, null)
                                    .addOnSuccessListener(aVoid2 -> imageAdapter.removeAt(position))
                                    .addOnFailureListener(e -> {
                                        // Handle the error on updating Firestore
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error on finding the document
                    });
        }).addOnFailureListener(e -> {
            // Handle the error on deleting the image from Firebase Storage
        });
    }
}
