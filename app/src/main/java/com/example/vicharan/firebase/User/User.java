package com.example.vicharan.firebase.User;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class User {
    private String id;
    private String Name;
    private String Email;
    private String Phone;

    public static User parseDb(DocumentSnapshot document) {
        try {
            Map<String, Object> data = document.getData();
            User user = new User();
            user.id = document.getId();
            user.Name = (String) data.get(DbUser.Fields.name.Name);
            user.Email = (String) data.get(DbUser.Fields.email.Name);
            user.Phone = (String) data.get(DbUser.Fields.phone.Name);
            return user;
        } catch (Exception e) {
            throw e;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return Name;
    }
}
