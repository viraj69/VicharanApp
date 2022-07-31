package com.example.vicharan.firebase.Apartment;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class Apartment {
    private String id;
    private LatLng location;
    private String place;

    public static Apartment parseDb(DocumentSnapshot document) {
        try {
            Map<String, Object> data = document.getData();
            Apartment apartment = new Apartment();

            apartment.id = document.getId();
            apartment.location = new LatLng((Double) data.get(DbApartment.Fields.latitude.Name), (Double) data.get(DbApartment.Fields.longitude.Name));
            apartment.place = (String) data.get(DbApartment.Fields.place.Name);

            return apartment;
        } catch (Exception e) {
            throw e;
        }
    }

    public String getId() {
        return id;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getPlace() {
        return place;
    }
}
