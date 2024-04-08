package com.example.qr_check_in.data;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class User_name {

    private FirebaseFirestore db;
    /**
     * Class responsible for fetching user name from Firestore or creating a guest user if not exists.
     */
    public User_name() {
        db = FirebaseFirestore.getInstance();
    }
    /**
     * Constructor initializing the Firestore database instance.
     */
    public void getUserNameOrCreateGuest(String deviceId, final UserNameFetchListener listener) {
        final DocumentReference userDocRef = db.collection("users").document(deviceId);

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // User exists, retrieve the name
                        listener.onUserNameFetched(document.getString("Name"));
                    } else {
                        // User does not exist, create a new guest user
                        Map<String, Object> newUser = new HashMap<>();
                        newUser.put("Name", "guest");
                        userDocRef.set(newUser).addOnSuccessListener(aVoid -> {
                            // New guest user created
                            listener.onUserNameFetched("guest");
                        }).addOnFailureListener(e -> listener.onError(e));
                    }
                } else {
                    listener.onError(task.getException());
                }
            }
        });
    }

    public interface UserNameFetchListener {
        void onUserNameFetched(String userName);
        void onError(Exception e);
    }
}

