package com.example.vicharan.firebase.media;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class Media {

    private String id;
    private String name;
    private int mimeType; // 1: image , 2: video

    public static Media parseDb(DocumentSnapshot document) {
        try {
            Map<String, Object> data = document.getData();
            Media media = new Media();
            media.id = document.getId();
            media.name = (String) data.get(DbMedia.Fields.name.Name);
            media.mimeType = (int) data.get(DbMedia.Fields.mimeType.Name);

            return media;
        } catch (Exception e) {
            throw e;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMimeType() {
        return mimeType;
    }

    public void setMimeType(int mimeType) {
        this.mimeType = mimeType;
    }
}
