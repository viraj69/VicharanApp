package com.example.vicharan.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.vicharan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ApartmentDialog extends AppCompatActivity {
    FirebaseFirestore db;
    StorageReference storageReference;
    SharedPreferences sp;
    public TextInputLayout Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_dialog);

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
    }

    public void showDialog(final Activity activity, final String ApartmentId) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_apartment_dialog);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.y = 150;
        window.setAttributes(wlp);
        ImageView next = (ImageView) dialog.findViewById(R.id.btn_dialog);

        getApartmentData(dialog, ApartmentId);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ApartmentDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("AptId", ApartmentId);
                i.putExtras(bundle);
                activity.startActivity(i);
            }
        });
        dialog.show();


    }

    public void showLoginDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.fragment_profile);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.y = 150;
        window.setAttributes(wlp);
        final TextInputLayout Email, Password;
        Button create, login, forgot;
        ProgressDialog pd;
        Toolbar toolbar;
        final FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        Email = dialog.findViewById(R.id.email);
        Password = dialog.findViewById(R.id.password);
        create = dialog.findViewById(R.id.create);
        login = dialog.findViewById(R.id.login);
        forgot = dialog.findViewById(R.id.forgotpass);
        toolbar = dialog.findViewById(R.id.toolbar);

        create.setTextColor(0);
        forgot.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd;
                String email = Email.getEditText().getText().toString();
                String pwd = Password.getEditText().getText().toString();
                System.out.println(email + "" + pwd);
                if (email.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(activity, "Please Fill The Form", Toast.LENGTH_SHORT).show();
                    return;
                }
                sp = activity.getSharedPreferences("Userdata", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USEREmailID", email);
                editor.putString("USERPassword", pwd);
                editor.commit();
                pd = new ProgressDialog(activity);
                pd.setMessage("Loading...");
                pd.show();
                auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(activity.getApplicationContext(), "Login Success!", Toast.LENGTH_LONG).show();

                            activity.finish();
                            activity.overridePendingTransition(0, 0);
                            activity.startActivity(activity.getIntent());
                            activity.overridePendingTransition(0, 0);
                            dialog.dismiss();
                            pd.dismiss();

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(activity.getApplicationContext(), "Email not exist!", Toast.LENGTH_LONG).show();
                                Email.getEditText().getText().clear();
                                Password.getEditText().getText().clear();
                                Email.setError("Email not exist!");
                                Email.requestFocus();
                                pd.dismiss();
                                return;
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(activity.getApplicationContext(), "Wrong Credential!", Toast.LENGTH_LONG).show();
                                Password.getEditText().getText().clear();
                                Email.requestFocus();
                                pd.dismiss();
                                return;
                            } catch (Exception e) {
                                Toast.makeText(activity.getApplicationContext(), "Login Failed!", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            }
                        }

                    }
                });
            }
        });
        dialog.show();
    }

    private void getApartmentData(final Dialog dialog, final String apartmentId) {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Apartment").document(apartmentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        TextView title = (TextView) dialog.findViewById(R.id.title);
                        TextView sutra = (TextView) dialog.findViewById(R.id.sutra);
                        TextView place = (TextView) dialog.findViewById(R.id.place1);
                        title.setText("" + document.getData().get("title"));
                        place.setText("" + document.getData().get("place"));
                        sutra.setText("" + document.getData().get("sutra"));

                        getImage(dialog, apartmentId);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });


    }

    private void getImage(final Dialog dialog, String id) {

        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + id + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                ImageView image = (ImageView) dialog.findViewById(R.id.dialogimage);
                Picasso.get().load(uri).fit().into(image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}