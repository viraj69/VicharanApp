package com.example.aksharSparsh.Activity;

import static com.example.aksharSparsh.Activity.CreateAccount.isEmailValid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aksharSparsh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileDetails extends AppCompatActivity {
    private FirebaseAuth auth;
    FirebaseFirestore db;
    TextInputLayout name, email, phone;
    Button btn_update;
    StorageReference storageReference;
    public static final int GALLERY_REQUEST_CODE = 105;
    int photos = 0;
    ImageView profile;
    FirebaseStorage storage;
    ArrayList contenturi = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        profile = findViewById(R.id.profile_image);
        name = findViewById(R.id.upadtename);
        email = findViewById(R.id.updateemail);
        phone = findViewById(R.id.updatephone);

        getUserData();

        btn_update = findViewById(R.id.btnUpdate);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        final String spEmail = sp.getString("USEREmailID", "");
        final String spPassword = sp.getString("USERPassword", "");
        Log.v("tagp", "EMAILID" + spEmail);
        Log.v("tagp", "PAssword" + spPassword);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = name.getEditText().getText().toString();
                String Phone = phone.getEditText().getText().toString();
                final String Email = email.getEditText().getText().toString();
                if (Name.isEmpty() || Phone.isEmpty() || Email.isEmpty()) {
                    Toast.makeText(ProfileDetails.this, "Please Fill The Form", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((Phone.length() < 10) || (Phone.length() > 10)) {
                    Toast.makeText(ProfileDetails.this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isEmailValid(Email)) {
                    Toast.makeText(ProfileDetails.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Map<String, Object> usermap = new HashMap<>();
                usermap.put("Name", Name);
                usermap.put("Phone", Phone);
                usermap.put("Email", Email);

                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String id = firebaseUser.getUid();
                Log.v("tagvv", " " + id);
                db = FirebaseFirestore.getInstance();
                Log.v("tagvv", " " + spEmail);
                Log.v("tagvv", " " + spPassword);

                if (!Email.equals(spEmail)) {
                    // Get auth credentials from the user for re-authentication
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(spEmail, spPassword); // Current Login Credentials \\
                    // Prompt the user to re-provide their sign-in credentials
                    firebaseUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("tagvv", "User re-authenticated.");
                                    //Now change your email address \\
                                    //----------------Code for Changing Email Address----------\\
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    firebaseUser.updateEmail(Email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("tagvv", "User email address updated.");
                                                        auth = FirebaseAuth.getInstance();
                                                        FirebaseUser firebaseUser = auth.getCurrentUser();
                                                        String id = firebaseUser.getUid();
                                                        db.collection("User").document(id).set(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(ProfileDetails.this, " Profile Updated ", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                String Error = e.getMessage();
                                                                Toast.makeText(ProfileDetails.this, " Error:" + Error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(ProfileDetails.this, " Email already exists", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });
                } else {
                    db.collection("User").document(id).set(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProfileDetails.this, " Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String Error = e.getMessage();
                            Toast.makeText(ProfileDetails.this, " Error:" + Error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel", "Delete"};
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileDetails.this);
        builder1.setTitle("Add Photo!");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {

                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gallery, ""), GALLERY_REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (options[item].equals("Delete")) {
                    deleteImage();
                }
            }
        });
        builder1.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                final Uri imageUri = data.getData();
                uploadImage(imageUri);

            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadImage(final Uri bitmap) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String USERid = firebaseUser.getUid();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference ref = storageReference.child("images").child("Profile").child(USERid + ".jpeg");
        ref.putFile(bitmap)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ProfileDetails.this, "Profile Picture is uploaded", Toast.LENGTH_SHORT).show();
                        profile.setImageURI(bitmap);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileDetails.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteImage() {
        auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        String photoRef = firebaseUser.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child("Profile").child(photoRef + ".jpeg");
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                profile.setImageResource(R.drawable.account);
                Toast.makeText(ProfileDetails.this, "Profile Picture is deleted", Toast.LENGTH_SHORT).show();
                Log.d("tagvv", "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d("tagvv", "onFailure: did not delete file");
            }
        });

    }

    private void getUserData() {
        auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        final String id = firebaseUser.getUid();
        Log.v("tagvv", " " + id);
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("User").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data2 = document.getData();
                        String Name = data2.get("Name").toString();
                        String Email = data2.get("Email").toString();
                        String Phnumber = data2.get("Phone").toString();
                        Log.d("tagvv", "DocumentSnapshot data: " + data2);
                        name.getEditText().setText(Name);
                        email.getEditText().setText(Email);
                        phone.getEditText().setText(Phnumber);
                        getProfileImage(id);

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    private void getProfileImage(String id) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/Profile/" + id + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.get().load(uri).fit().into(profile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}