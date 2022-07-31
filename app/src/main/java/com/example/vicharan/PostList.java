package com.example.vicharan;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicharan.Adapters.PostListAdapter;
import com.example.vicharan.Models.PostListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class  PostList extends AppCompatActivity {
    RecyclerView PostListRecycler;

    PostListAdapter postlistAdapter;
    FirebaseStorage storage;
    StorageReference storageReference;
    /**
     * variable declaration authenication object
     */
    FirebaseFirestore db;
    private FirebaseAuth auth;
    /**
     * variable declarationfor current user
     */
    private FirebaseUser curUser;
    String userId = null;
    ArrayList<PostListModel> postlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();

        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }
        getApartmetList();
    }

    private void getApartmetList() {
        db = FirebaseFirestore.getInstance();
        db.collection("Apartment")
                .whereEqualTo("UserID", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("PostList", document.getId() + " => " + document.getData());
                                String id = document.getId();
                                String title = (String) document.getData().get("title");
                                String place = (String) document.getData().get("place");
                                String sutra = (String) document.getData().get("sutra");
                                getImage(id, title, place, sutra);
                            }
                            setPostListRecycler();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void getImage(final String id, final String title, final String place, final String sutra) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + id + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                postlist.add(new PostListModel(id, title, place, sutra, uri));
                postlistAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }


    private void setPostListRecycler() {
        PostListRecycler = findViewById(R.id.postlist_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        PostListRecycler.setLayoutManager(layoutManager);
        postlistAdapter = new PostListAdapter(this, postlist);
        PostListRecycler.setAdapter(postlistAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        postlist.clear();
        postlistAdapter.notifyDataSetChanged();
        getApartmetList();
    }
}