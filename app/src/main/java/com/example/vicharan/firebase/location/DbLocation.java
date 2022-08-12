package com.example.vicharan.firebase.location;

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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DbLocation {
    private final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String DbCollectionName = "Location";

    public enum Fields {
        id("id"), userId("userId"), googlePlaceId("googlePlaceId"),
        country("country"), city("city"), place("place"), address("address"), description("description"),
        latitude("latitude"), longitude("longitude"), inactive("inactive");

        public String Name;

        Fields(String name) {
            Name = name;
        }
    }

    public static void insert(Location location, DbInsertionListener dbInsertionListener) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(Fields.userId.Name, location.getUserId());
        userMap.put(Fields.googlePlaceId.Name, location.getGooglePlaceId());
        userMap.put(Fields.country.Name, location.getCountry());
        userMap.put(Fields.city.Name, location.getCity());
        userMap.put(Fields.place.Name, location.getPlace());
        userMap.put(Fields.address.Name, location.getAddress());
        userMap.put(Fields.description.Name, location.getDescription());
        userMap.put(Fields.latitude.Name, location.getLocation().latitude);
        userMap.put(Fields.longitude.Name, location.getLocation().longitude);

        db.collection(DbCollectionName).add(userMap)
                .addOnSuccessListener(documentReference -> dbInsertionListener.onSuccess(documentReference.getId()))
                .addOnFailureListener(e -> dbInsertionListener.onFailure(e));
    }

    /*
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
CollectionReference complaintsRef = rootRef.collection("complaints");
complaintsRef.whereEqualTo("id", 5).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Map<Object, String> map = new HashMap<>();
                map.put("complainant_Name", "ABC");
                complaintsRef.document(document.getId()).set(map, SetOptions.merge());
            }
        }
    }
});
    * */

    public static void update(Location location, OnSuccessListener<Void> onSuccessListener) {
        if (location.getId() == null) {
            throw new RuntimeException("location id is null");
        }
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(Fields.userId.Name, location.getUserId());
        userMap.put(Fields.googlePlaceId.Name, location.getGooglePlaceId());
        userMap.put(Fields.country.Name, location.getCountry());
        userMap.put(Fields.city.Name, location.getCity());
        userMap.put(Fields.place.Name, location.getPlace());
        userMap.put(Fields.address.Name, location.getAddress());
        userMap.put(Fields.description.Name, location.getDescription());
        userMap.put(Fields.latitude.Name, location.getLocation().latitude);
        userMap.put(Fields.longitude.Name, location.getLocation().longitude);

        db.collection(DbCollectionName).document(location.getId()).set(userMap).addOnSuccessListener(onSuccessListener);
    }

    public static void getById(String id, final DbCallbackListener<Location> dbCallbackListener) {
        db.collection(DbCollectionName).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult() != null) {
                    dbCallbackListener.onDbCallback(Location.parseDb(task.getResult()));
                }
            }
        });
    }

    public static void getByGooglePlaceId(String googlePlaceId, final DbCallbackListener<Location> dbCallbackListener) {
        db.collection(DbCollectionName).whereEqualTo(Fields.googlePlaceId.Name, googlePlaceId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() > 0) {
                    dbCallbackListener.onDbCallback(Location.parseDb(task.getResult().getDocuments().get(0)));
                    return;
                }
            } else {
                Log.d("TAG", "Error getting documents by googlePlaceId: ", task.getException());
            }
            dbCallbackListener.onDbCallback(null);
        });
    }

    public static void getByCountryName(String countryName, final DbListCallbackListener<Location> dbListCallbackListener) {
        db.collection(DbCollectionName).whereEqualTo(Fields.country.Name, countryName)/*.whereNotEqualTo(Fields.inactive.Name, true)*/.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Location> list = new LinkedList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    list.add(Location.parseDb(document));
                }
                dbListCallbackListener.onDbListCallback(list);
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }
}
