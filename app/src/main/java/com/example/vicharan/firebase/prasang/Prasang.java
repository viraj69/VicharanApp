package com.example.vicharan.firebase.prasang;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Prasang {
    private String id;
    private String locationId;
    private String userId;
    private String title;
    private String sutra;
    private String description;
    private String notes;
    private String date;
    private List<String> media;

    public static Prasang parseDb(DocumentSnapshot document) {
        try {
            Prasang prasang = new Prasang();
            prasang.id = document.getId();
            prasang.locationId = (String) document.get(DbPrasang.Fields.locationId.Name);
            prasang.userId = (String) document.get(DbPrasang.Fields.userId.Name);
            prasang.title = (String) document.get(DbPrasang.Fields.title.Name);
            prasang.sutra = (String) document.get(DbPrasang.Fields.sutra.Name);
            prasang.description = (String) document.get(DbPrasang.Fields.description.Name);
            prasang.notes = (String) document.get(DbPrasang.Fields.notes.Name);
            prasang.date = (String) document.get(DbPrasang.Fields.date.Name);
            HashMap<String, String> imagesHashmap = document.get(DbPrasang.Fields.media.Name) == null ? null : (HashMap<String, String>) document.get(DbPrasang.Fields.media.Name);
            if (imagesHashmap != null) {
                prasang.media = new LinkedList<>(imagesHashmap.values());
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSutra() {
        return sutra;
    }

    public void setSutra(String sutra) {
        this.sutra = sutra;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
}
