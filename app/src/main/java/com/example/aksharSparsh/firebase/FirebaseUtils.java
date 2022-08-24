package com.example.aksharSparsh.firebase;

import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.annotation.Nullable;

public class FirebaseUtils {
    @Nullable
    public static String getLoggedInUserId() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser curUser = auth.getCurrentUser();
        String userId = null;
        if (curUser != null) {
            userId = curUser.getUid();
        }
        return userId;
    }

    public static void loadImage(String prasangId, String mediaName, ImageView imageView, OnFailureListener onFailureListener) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + prasangId + "/" + mediaName).getDownloadUrl()
                .addOnSuccessListener(imageView::setImageURI)
                .addOnFailureListener(onFailureListener);
    }

    public static void loadImage(String prasangId, String mediaName, OnSuccessListener<Uri> onSuccessListener, OnFailureListener onFailureListener) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + prasangId + "/" + mediaName).getDownloadUrl()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public static void saveImage(String prasangId, String mediaName, Uri contentUri,
                                 @Nullable OnSuccessListener<UploadTask.TaskSnapshot> taskSnapshotOnSuccessListener,
                                 @Nullable OnFailureListener failureListener) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference ref = storageReference.child("images").child(prasangId + "/" + mediaName);
        ref.putFile(contentUri)
                .addOnSuccessListener(taskSnapshot -> {
                    if (taskSnapshotOnSuccessListener != null)
                        taskSnapshotOnSuccessListener.onSuccess(taskSnapshot);
                })
                .addOnFailureListener(e -> {
                    if (failureListener != null) failureListener.onFailure(e);
                });
    }
}
