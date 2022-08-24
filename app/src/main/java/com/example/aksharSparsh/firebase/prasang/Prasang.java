package com.example.aksharSparsh.firebase.prasang;

import com.example.aksharSparsh.firebase.location.DbLocation;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Prasang implements Serializable {
    private String id;
    private String locationId;
    private String userId;
    private GenericPrasang gujaratiVersion;
    private GenericPrasang englishVersion;
    private String date;
    private List<String> media;

    public static Prasang parseDb(DocumentSnapshot document) {
        try {
            Map<String, Object> data = document.getData();

            Prasang prasang = new Prasang();
            prasang.id = document.getId();
            prasang.locationId = (String) data.get(DbPrasang.Fields.locationId.Name);
            prasang.userId = (String) data.get(DbPrasang.Fields.userId.Name);
            prasang.date = (String) data.get(DbPrasang.Fields.date.Name);
            HashMap<String, String> imagesHashmap = data.get(DbPrasang.Fields.media.Name) == null ? null : (HashMap<String, String>) data.get(DbPrasang.Fields.media.Name);
            if (imagesHashmap != null) {
                prasang.media = new LinkedList<>(imagesHashmap.values());
            }

            if (document.contains(DbLocation.Fields.eng.Name)) {
                prasang.englishVersion = GenericPrasang.parseDb((HashMap<String, Object>) data.get(DbPrasang.Fields.eng.Name));
            } else {    // TODO: delete this - as it is just used for backward compatibility
                prasang.englishVersion = GenericPrasang.parseDb(data);
            }

            if (document.contains(DbLocation.Fields.guj.Name)) {
                prasang.gujaratiVersion = GenericPrasang.parseDb((HashMap<String, Object>) data.get(DbPrasang.Fields.guj.Name));
            } else {
                prasang.gujaratiVersion = prasang.englishVersion;
            }
            return prasang;
        } catch (Exception e) {
            throw e;
        }
    }

    public String getId() {
        return id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getMedia() {
        return media;
    }

    public void setMedia(List<String> media) {
        this.media = media;
    }

    public void addMedia(String media) {
        if (this.media == null) {
            this.media = new LinkedList<>();
        }
        this.media.add(media);
    }

    public GenericPrasang getGujaratiVersion() {
        return gujaratiVersion;
    }

    public void setGujaratiVersion(GenericPrasang gujaratiVersion) {
        this.gujaratiVersion = gujaratiVersion;
    }

    public GenericPrasang getEnglishVersion() {
        return englishVersion;
    }

    public void setEnglishVersion(GenericPrasang englishVersion) {
        this.englishVersion = englishVersion;
    }
}
