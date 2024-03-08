package com.example.qr_check_in.data;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class FirebaseImageLoader {
    // Callback interface for when the image URLs are fetched
    public interface ImageUrlsFetchedListener {
        void onImageUrlsFetched(List<String> imageUrls);
        void onError(String error);
    }

    // Firebase storage reference
    private final StorageReference storageRef;

    public FirebaseImageLoader() {
        // Reference to your Firebase Storage root
        storageRef = FirebaseStorage.getInstance().getReference();
    }
    public void loadImageUrls(String path,ImageUrlsFetchedListener listener) {
        // Assuming that all images are stored in a folder named 'images'
        StorageReference imagesRef = storageRef.child("/event_posters");

        imagesRef.listAll()
                .addOnSuccessListener(listResult -> {
                    List<String> imageUrls = new ArrayList<>();
                    for (StorageReference itemRef : listResult.getItems()) {
                        // Get the download URL for each item
                        itemRef.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                            imageUrls.add(downloadUrl.toString());
                            if (imageUrls.size() == listResult.getItems().size()) {
                                listener.onImageUrlsFetched(imageUrls);
                            }
                        }).addOnFailureListener(e -> listener.onError("Error fetching download URL"));
                    }
                })
                .addOnFailureListener(e -> listener.onError("Error listing images"));
    }
}
