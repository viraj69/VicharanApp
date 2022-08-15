package com.example.vicharan.firebase.prasang;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vicharan.firebase.generic.DbCallbackListener;
import com.example.vicharan.firebase.generic.DbInsertionListener;
import com.example.vicharan.firebase.generic.DbListCallbackListener;
import com.example.vicharan.firebase.location.DbLocation;
import com.example.vicharan.firebase.location.Location;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
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
                .addOnFailureListener(dbInsertionListener::onFailure);
    }

    public static void update(Prasang prasang, OnSuccessListener<Void> onSuccessListener) {
        if (prasang.getId() == null) {
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
                } else {
                    dbCallbackListener.onDbCallback(null);
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
                dbListCallbackListener.onDbListCallback(null);
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
                dbListCallbackListener.onDbListCallback(null);
            }
        });
    }

    public static void getAllWithLocation(final DbCallbackListener<List<LocationPrasangPair>> dbCallbackListener) {
        db.collection(DbCollectionName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Prasang> list = new LinkedList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    list.add(Prasang.parseDb(document));
                }
                getLocationsForPrasangs(list, dbCallbackListener);
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
                dbCallbackListener.onDbCallback(null);
            }
        });
    }

    public static void getPrasangsWithTheirLocationByUserId(String userId, final DbCallbackListener<List<LocationPrasangPair>> dbCallbackListener) {
        getByUserId(userId, (List<Prasang> prasangs) -> getLocationsForPrasangs(prasangs, dbCallbackListener));
    }

    private static void mergeDuplicateLocations(List<LocationPrasangPair> locationPrasangPairs) {
        final HashMap<String, Location> locationHashMap = new HashMap<>();
        for (LocationPrasangPair locationPrasangPair : locationPrasangPairs) {
            Location location = locationHashMap.get(locationPrasangPair.getLocation().getId());
            if (location == null) {
                locationHashMap.put(locationPrasangPair.getLocation().getId(), locationPrasangPair.getLocation());
            } else {
                locationPrasangPair.setLocation(location);
            }
        }
    }

    private static void getLocationsForPrasangs(List<Prasang> prasangs, final DbCallbackListener<List<LocationPrasangPair>> dbCallbackListener) {
        final HashMap<String, Location> locationHashMap = new HashMap<>();
        final List<LocationPrasangPair> locationPrasangPairs = new LinkedList<>();

        for (int i = 0; i < prasangs.size(); i++) {
            final int index = i;
            final Prasang prasang = prasangs.get(i);
            Location location = locationHashMap.get(prasang.getLocationId());
            if (location != null) {
                locationPrasangPairs.add(new LocationPrasangPair(location, prasang));
                if (i == prasangs.size() - 1) {
                    mergeDuplicateLocations(locationPrasangPairs);
                    dbCallbackListener.onDbCallback(locationPrasangPairs);
                }
            } else {
                DbLocation.getById(prasang.getLocationId(), (Location loc) -> {
                    if (loc == null) {
                        locationPrasangPairs.add(new LocationPrasangPair(null, prasang));
                    } else {
                        locationHashMap.put(prasang.getLocationId(), loc);
                        locationPrasangPairs.add(new LocationPrasangPair(loc, prasang));
                    }
                    if (index == prasangs.size() - 1) {
                        mergeDuplicateLocations(locationPrasangPairs);
                        dbCallbackListener.onDbCallback(locationPrasangPairs);
                    }
                });
            }
        }
    }

    public static class LocationPrasangPair implements Serializable {
        private Location location;
        private final Prasang prasang;

        public LocationPrasangPair(Location location, Prasang prasang) {
            this.location = location;
            this.prasang = prasang;
        }

        private void setLocation(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public Prasang getPrasang() {
            return prasang;
        }
    }
}
