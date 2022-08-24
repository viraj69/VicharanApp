package com.example.aksharSparsh.firebase.prasang;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.Map;

public class GenericPrasang implements Serializable {
    private String title;
    private String sutra;
    private String description;
    private String notes;

    public static GenericPrasang parseDb(DocumentSnapshot document) {
        Map<String, Object> data = document.getData();
        return parseDb(data);
    }

    public static GenericPrasang parseDb(Map<String, Object> data) {
        try {
            GenericPrasang prasang = new GenericPrasang();

            prasang.title = (String) data.get(DbPrasang.Fields.title.Name);
            prasang.sutra = (String) data.get(DbPrasang.Fields.sutra.Name);
            prasang.description = (String) data.get(DbPrasang.Fields.description.Name);
            prasang.notes = (String) data.get(DbPrasang.Fields.notes.Name);

            return prasang;
        } catch (Exception e) {
            throw e;
        }
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
}
