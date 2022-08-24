package com.example.aksharSparsh.firebase.user;

import com.example.aksharSparsh.firebase.generic.DbCallbackListener;
import com.example.aksharSparsh.firebase.generic.DbInsertionListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class DbUser {
    private final static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String DbCollectionName = "User";

    public enum Fields {
        id("id"), name("Name"), email("Email"), phone("Phone");

        public String Name;

        Fields(String name) {
            Name = name;
        }
    }

    public static void insert(User user, DbInsertionListener dbInsertionListener) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(Fields.name.Name, user.getName());
        userMap.put(Fields.email.Name, user.getEmail());
        userMap.put(Fields.phone.Name, user.getPhone());

        db.collection(DbCollectionName).add(userMap)
                .addOnSuccessListener(documentReference -> dbInsertionListener.onSuccess(documentReference.getId()))
                .addOnFailureListener(e -> dbInsertionListener.onFailure(e));
    }

    public static void getById(String id, final DbCallbackListener<User> dbCallbackListener) {
        db.collection(DbCollectionName).document(id).get().addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                dbCallbackListener.onDbCallback(User.parseDb(task.getResult()));
            }
        });
    }
}
