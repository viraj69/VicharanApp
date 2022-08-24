package com.example.aksharSparsh.firebase.user;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class User {
    private String id;
    private String name;
    private String email;
    private String phone;

    public static User parseDb(DocumentSnapshot document) {
        try {
            Map<String, Object> data = document.getData();
            User user = new User();
            user.id = document.getId();
            user.name = (String) data.get(DbUser.Fields.name.Name);
            user.email = (String) data.get(DbUser.Fields.email.Name);
            user.phone = (String) data.get(DbUser.Fields.phone.Name);
            return user;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
