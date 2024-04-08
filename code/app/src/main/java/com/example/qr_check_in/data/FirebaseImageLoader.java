package com.example.qr_check_in.data;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;
/**
 * Class responsible for loading images from Firebase Storage.
 */
public class FirebaseImageLoader {
    private final FirebaseFirestore db; // FirebaseFirestore instance for database operations
    private final FirebaseStorage storage; // FirebaseStorage instance
    /**
     * Constructor initializing the Firestore database and Firebase Storage instances.
     */
    public FirebaseImageLoader() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }
    /**
     * Loads images from multiple folders in Firebase Storage.
     *
     * @param paths    List of folder paths.
     * @param listener Listener to handle the fetched image URLs or errors.
     */
    public void loadImagesFromMultipleFolders(List<String> paths, final ImageUrlsFetchedListener listener) {
        final List<String> allImageUrls = new ArrayList<>();
        final int[] pathsProcessed = {0};

        for (String path : paths) { // List all items (images) in the folder
            StorageReference folderRef = storage.getReference().child(path);

            folderRef.listAll()
                    .addOnSuccessListener(listResult -> {
                        List<Task<Uri>> downloadUrlTasks = new ArrayList<>();
                        for (StorageReference itemRef : listResult.getItems()) {
                            // Get the download URL for each item
                            Task<Uri> downloadUrlTask = itemRef.getDownloadUrl();
                            downloadUrlTasks.add(downloadUrlTask);
                        }

                        // When all download URLs are fetched
                        Task<List<Uri>> allTasks = Tasks.whenAllSuccess(downloadUrlTasks);
                        allTasks.addOnSuccessListener(uris -> {
                            for (Uri uri : uris) {
                                allImageUrls.add(uri.toString());
                            }
                            pathsProcessed[0]++;
                            // If all paths are processed, invoke the callback
                            if (pathsProcessed[0] == paths.size()) {
                                listener.onImageUrlsFetched(allImageUrls);
                            }
                        });
                    })
                    .addOnFailureListener(e -> listener.onError("Error listing images from path: " + path));
        }
    }

    public interface ImageUrlsFetchedListener {
        void onImageUrlsFetched(List<String> imageUrls);
        void onError(String error);
    }
}
