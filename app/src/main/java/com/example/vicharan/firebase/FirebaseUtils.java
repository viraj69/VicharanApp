package com.example.vicharan.firebase;

import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtils {
    public static void loadImage(String locationId, String mediaName, ImageView imageView, OnFailureListener onFailureListener) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + locationId + "/" + mediaName).getDownloadUrl()
                .addOnSuccessListener(imageView::setImageURI)
                .addOnFailureListener(onFailureListener);
    }

    public static void loadImage(String locationId, String mediaName, OnSuccessListener<Uri> onSuccessListener, OnFailureListener onFailureListener) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + locationId + "/" + mediaName).getDownloadUrl()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
}
