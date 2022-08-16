package com.example.vicharan.firebase.location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Location implements Serializable {
    private String id;
    private double lat;
    private double lng;
    private String userId;
    private String googlePlaceId;
    private String address;
    private String description;
    private GenericLocation englishVersion;
    private GenericLocation gujaratiVersion;

    public static Location parseDb(DocumentSnapshot document) {
        try {
            Map<String, Object> data = document.getData();
            Location location = new Location();

            location.id = document.getId();
            location.lat = (Double) data.get(DbLocation.Fields.latitude.Name);
            location.lng = (Double) data.get(DbLocation.Fields.longitude.Name);
            location.userId = (String) data.get(DbLocation.Fields.userId.Name);
            location.googlePlaceId = (String) data.get(DbLocation.Fields.googlePlaceId.Name);
            location.address = (String) data.get(DbLocation.Fields.address.Name);
            location.description = (String) data.get(DbLocation.Fields.description.Name);

            if (document.contains(DbLocation.Fields.eng.Name)) {
                location.englishVersion = GenericLocation.parseDb((HashMap<String, Object>) document.get(DbLocation.Fields.eng.Name));
            } else {    // TODO: delete this - as it is just used for backward compatibility
                location.englishVersion = GenericLocation.parseDb(data);
            }

            if (document.contains(DbLocation.Fields.guj.Name)) {
                location.gujaratiVersion = GenericLocation.parseDb((HashMap<String, Object>) document.get(DbLocation.Fields.guj.Name));
            } else {
                location.gujaratiVersion = location.englishVersion;
            }

            return location;
        } catch (Exception e) {
            throw e;
        }
    }

    public String getId() {
        return id;
    }

    public LatLng getLocation() {
        return new LatLng(lat, lng);
    }

    public void setLocation(LatLng location) {
        this.lat = location.latitude;
        this.lng = location.longitude;
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

    public GenericLocation getGujaratiVersion() {
        return gujaratiVersion;
    }

    public GenericLocation getEnglishVersion() {
        return englishVersion;
    }

    public void setEnglishVersion(GenericLocation englishVersion) {
        this.englishVersion = englishVersion;
    }

    public void setGujaratiVersion(GenericLocation gujaratiVersion) {
        this.gujaratiVersion = gujaratiVersion;
    }
}
