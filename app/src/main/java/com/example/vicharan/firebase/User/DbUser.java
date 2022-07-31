package com.example.vicharan.firebase.User;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vicharan.firebase.Apartment.Apartment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;


public class DbUser {
    private final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String DbCollectionName = "User";

    public enum Fields {
//        id("id"), name("country"), phone("place"),
//        email("latitude");

        id("id"), name("Name"), email("Email"), phone("Phone");

        public String Name;

        Fields(String name) {
            Name = name;
        }
    }

    public static void getById(String id, final com.example.vicharan.firebase.Apartment.DbApartment.OnDbApartmentCallbackListener onDbApartmentCallbackListener) {
        db.collection(DbCollectionName).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult() != null) {
                    onDbApartmentCallbackListener.onDbApartmentCallback(Apartment.parseDb(task.getResult()));
                }
            }
        });
    }

    public static void getByCountryName(String countryName, final com.example.vicharan.firebase.Apartment.DbApartment.OnDbApartmentListCallbackListener onDbApartmentListCallbackListener) {
        db.collection(DbCollectionName).whereEqualTo(com.example.vicharan.firebase.Apartment.DbApartment.Fields.country.Name, countryName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Apartment> list = new LinkedList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(Apartment.parseDb(document));
                    }
                    onDbApartmentListCallbackListener.onDbApartmentListCallback(list);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public interface OnDbApartmentCallbackListener {
        void onDbApartmentCallback(Apartment apartment);
    }

    public interface OnDbApartmentListCallbackListener {
        void onDbApartmentListCallback(List<Apartment> list);
    }
}
