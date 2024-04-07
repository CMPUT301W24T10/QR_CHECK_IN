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
        fetchImages("users", "profileImageUrl");

        // Fetch event posters
        fetchImages("events", "posterUrl");
    }

    private void fetchImages(String collectionPath, String imageUrlField) {
        db.collection(collectionPath).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> fetchedUrls = new ArrayList<>();
                task.getResult().forEach(document -> {
                    String imageUrl = document.getString(imageUrlField);
                    if (imageUrl != null && !imageUrl.isEmpty()) { // Check for non-empty URLs
                        fetchedUrls.add(imageUrl);
                    }
                });
                imageAdapter.setImageUrls(fetchedUrls);
                imageAdapter.notifyDataSetChanged();
            } else {
                // Handle errors
            }
        });
    }

    // Method called when the delete button is clicked
    private void onDeleteClick(String imageUrl, int position) {
        FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl).delete().addOnSuccessListener(aVoid -> {
            // Determine the collection to update based on the image path
            String collectionPath = imageUrl.contains("profile_images") ? "users" : "events";
            String documentField = imageUrl.contains("profile_images") ? "profileImageUrl" : "posterUrl";

            // Find the document with the matching image URL and update it with an empty string
            db.collection(collectionPath).whereEqualTo(documentField, imageUrl).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            String docId = queryDocumentSnapshots.getDocuments().get(0).getId();
                            db.collection(collectionPath).document(docId)
                                    .update(documentField, "") // Set field to empty string
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
