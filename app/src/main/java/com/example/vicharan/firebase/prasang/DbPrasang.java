package com.example.vicharan.firebase.prasang;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vicharan.firebase.generic.DbCallbackListener;
import com.example.vicharan.firebase.generic.DbInsertionListener;
import com.example.vicharan.firebase.generic.DbListCallbackListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DbPrasang {
    private final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String DbCollectionName = "Prasang";

    public enum Fields {
        id("id"), locationId("locationId"), userId("userId"), title("title"), sutra("sutra"),
        description("description"), notes("notes"), date("date"), media("media"), inactive("inactive");

        public String Name;

        Fields(String name) {
            Name = name;
        }
    }

    public static void insert(Prasang prasang, DbInsertionListener dbInsertionListener) {
        Map<String, Object> map = new HashMap<>();
        map.put(Fields.locationId.Name, prasang.getLocationId());
        map.put(Fields.userId.Name, prasang.getUserId());
        map.put(Fields.title.Name, prasang.getTitle());
        map.put(Fields.sutra.Name, prasang.getSutra());
        map.put(Fields.description.Name, prasang.getDescription());
        map.put(Fields.notes.Name, prasang.getNotes());
        map.put(Fields.date.Name, prasang.getDate());

        if (prasang.getMedia() != null) {
            Map<String, String> images = new HashMap<>();
            for (int i = 0; i < prasang.getMedia().size(); i++) {
                String img = prasang.getMedia().get(i);
                images.put(i + "", img);
            }
            map.put(Fields.media.Name, images);
        }

        db.collection(DbCollectionName).add(map)
                .addOnSuccessListener(documentReference -> dbInsertionListener.onSuccess(documentReference.getId()))
                .addOnFailureListener(e -> dbInsertionListener.onFailure(e));
    }

    public static void update(Prasang prasang, OnSuccessListener<Void> onSuccessListener) {
        if(prasang.getId() == null) {
            throw new RuntimeException("prasang id is null");
        }
        Map<String, Object> map = new HashMap<>();
        map.put(Fields.locationId.Name, prasang.getLocationId());
        map.put(Fields.userId.Name, prasang.getUserId());
        map.put(Fields.title.Name, prasang.getTitle());
        map.put(Fields.sutra.Name, prasang.getSutra());
        map.put(Fields.description.Name, prasang.getDescription());
        map.put(Fields.notes.Name, prasang.getNotes());
        map.put(Fields.date.Name, prasang.getDate());

        if (prasang.getMedia() != null) {
            Map<String, String> images = new HashMap<>();
            for (int i = 0; i < prasang.getMedia().size(); i++) {
                String img = prasang.getMedia().get(i);
                images.put(i + "", img);
            }
            map.put(Fields.media.Name, images);
        }

        db.collection(DbCollectionName).document(prasang.getId()).set(map).addOnSuccessListener(onSuccessListener);
    }

    public static void getById(String id, final DbCallbackListener<Prasang> dbCallbackListener) {
        db.collection(DbCollectionName).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult() != null) {
                    dbCallbackListener.onDbCallback(Prasang.parseDb(task.getResult()));
                }
            }
        });
    }

    public static void getByLocationId(String locationId, final DbListCallbackListener<Prasang> dbListCallbackListener) {
        db.collection(DbCollectionName).whereEqualTo(Fields.locationId.Name, locationId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Prasang> list = new LinkedList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    list.add(Prasang.parseDb(document));
                }
                dbListCallbackListener.onDbListCallback(list);
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    public static void getByUserId(String userId, final DbListCallbackListener<Prasang> dbListCallbackListener) {
        db.collection(DbCollectionName).whereEqualTo(Fields.userId.Name, userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Prasang> list = new LinkedList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    list.add(Prasang.parseDb(document));
                }
                dbListCallbackListener.onDbListCallback(list);
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }
}
