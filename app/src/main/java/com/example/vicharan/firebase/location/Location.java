package com.example.vicharan.firebase.location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.Map;

public class Location implements Serializable {
    private String id;
    private LatLng location;
    private String userId;
    private String googlePlaceId;
    private String country;
    private String city;
    private String place;
    private String address;
    private String description;

    public static Location parseDb(DocumentSnapshot document) {
        try {
            Map<String, Object> data = document.getData();
            Location location = new Location();

            location.id = document.getId();
            location.location = new LatLng((Double) data.get(DbLocation.Fields.latitude.Name), (Double) data.get(DbLocation.Fields.longitude.Name));
            location.userId = (String) data.get(DbLocation.Fields.userId.Name);
            location.googlePlaceId = (String) data.get(DbLocation.Fields.googlePlaceId.Name);
            location.country = (String) data.get(DbLocation.Fields.country.Name);
            location.city = (String) data.get(DbLocation.Fields.city.Name);
            location.place = (String) data.get(DbLocation.Fields.place.Name);
            location.address = (String) data.get(DbLocation.Fields.address.Name);
            location.description = (String) data.get(DbLocation.Fields.description.Name);

            return location;
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

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
