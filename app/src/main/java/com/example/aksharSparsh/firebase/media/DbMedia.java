package com.example.aksharSparsh.firebase.media;

import androidx.annotation.NonNull;

import com.example.aksharSparsh.firebase.generic.DbCallbackListener;
import com.example.aksharSparsh.firebase.generic.DbInsertionListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DbMedia {
    private final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String DbCollectionName = "Media";

    public enum Fields {
        id("id"), name("Name"), mimeType("mimeType");

        public String Name;

        Fields(String name) {
            Name = name;
        }
    }

    public static void insert(Media media, DbInsertionListener dbInsertionListener) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(Fields.name.Name, media.getName());
        userMap.put(Fields.mimeType.Name, media.getMimeType());

        db.collection(DbCollectionName).add(userMap)
                .addOnSuccessListener(documentReference -> dbInsertionListener.onSuccess(documentReference.getId()))
                .addOnFailureListener(e -> dbInsertionListener.onFailure(e));
    }

    public static void updateById(@NonNull Media media, OnSuccessListener<Void> onSuccessListener) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(Fields.name.Name, media.getName());
        userMap.put(Fields.mimeType.Name, media.getMimeType());

        db.collection(DbCollectionName).document(media.getId()).set(userMap, SetOptions.mergeFields()).addOnSuccessListener(onSuccessListener);
    }

    public static void getById(String id, final DbCallbackListener<Media> dbCallbackListener) {
        db.collection(DbCollectionName).document(id).get().addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                dbCallbackListener.onDbCallback(Media.parseDb(task.getResult()));
            }
        });
    }
}
