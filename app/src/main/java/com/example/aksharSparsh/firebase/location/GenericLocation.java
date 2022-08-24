package com.example.aksharSparsh.firebase.location;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.Map;

public class GenericLocation implements Serializable {
    private String country;
    private String city;
    private String place;

    public static GenericLocation parseDb(DocumentSnapshot document) {
        Map<String, Object> data = document.getData();
        return parseDb(data);
    }

    public static GenericLocation parseDb(Map<String, Object> data) {
        try {
            GenericLocation location = new GenericLocation();

            location.country = (String) data.get(DbLocation.Fields.country.Name);
            location.city = (String) data.get(DbLocation.Fields.city.Name);
            location.place = (String) data.get(DbLocation.Fields.place.Name);

            return location;
        } catch (Exception e) {
            throw e;
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
