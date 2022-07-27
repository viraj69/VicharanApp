package com.example.vicharan.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vicharan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class Contact extends AppCompatActivity {
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    private static final int PERMISSION_REQUEST_CODE = 1;
    FirebaseFirestore fstore;
    FirebaseStorage storage;
    StorageReference storageReference;
    String Uid, OwnerEmail;
    EditText username, userphone, eml;
    TextView phone;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent i = getIntent();
        Uid = i.getStringExtra("Uid");

        Button sendMsg = (Button) findViewById(R.id.btnsendsms);
        Button startBtn = (Button) findViewById(R.id.btnsendemail);
        final EditText msg = (EditText) findViewById(R.id.inputtextsms);
        username = (EditText) findViewById(R.id.txtname);
        userphone = (EditText) findViewById(R.id.userphone);
        ImageView imageCall = findViewById(R.id.imgcall);
        phone = (TextView) findViewById(R.id.txtphonenumber);
        eml = findViewById(R.id.txteml);
        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendEmail();
            }
        });
        sendMsg.setEnabled(true);
        //check if permission is granted or not
        if (checkPermission(Manifest.permission.SEND_SMS)) {

        }   //if permission is not granted then check if the user has denied the permission
        else {
            //a pop up will appear asking for required permission to send a sms
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        getnumber();
        getUserData();
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Yes", "No"};
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Contact.this);
                builder1.setTitle("Do you want to send message to the owner?!");
                builder1.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Yes")) {
                            String message = msg.getText().toString();
                            String Phonenumber = phone.getText().toString();
                            if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(Phonenumber))
                                if (checkPermission(Manifest.permission.SEND_SMS)) {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(Phonenumber, null, message, null, null);
                                    Toast.makeText(Contact.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Contact.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                }
                            else {
                                Toast.makeText(Contact.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            }

                        } else if (options[item].equals("NO")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder1.show();
            }
        });


        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }//end


    private boolean checkPermission(String Permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, Permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thanks for permitting ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Allow to send an sms!", Toast.LENGTH_LONG).show();
                }
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }//swtich end
    }//end

    private void makePhoneCall() {
        final TextView phone = (TextView) findViewById(R.id.txtphonenumber);
        String number = phone.getText().toString();
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(Contact.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Contact.this,
                        new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(Contact.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    private void getnumber() {

        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("User").document(Uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data3 = document.getData();
                        OwnerEmail = data3.get("Email").toString();
                        String Phnumber = data3.get("Phone").toString();
                        Log.d("tagvv", "DocumentSnapshot data: " + data3);
                        phone.setText(Phnumber);
                        Log.d("tagvv", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("tagvv", "No such document");
                    }
                } else {
                    Log.d("tagvv", "get failed with ", task.getException());
                }
            }
        });
    }


    private void getUserData() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String id = firebaseUser.getUid();
        Log.v("tagvv", " " + id);
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("User").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data3 = document.getData();
                        String Name = data3.get("Name").toString();
                        String Email = data3.get("Email").toString();
                        String Phnumber = data3.get("Phone").toString();
                        Log.d("tagvv", "DocumentSnapshot data: " + data3);
                        username.setText(Name);
                        eml.setText(Email);
                        userphone.setText(Phnumber);

                        Log.d("tagvv", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("tagvv", "No such document");
                    }
                } else {
                    Log.d("tagvv", "get failed with ", task.getException());
                }
            }
        });
    }


    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {OwnerEmail};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry for Apartment");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi,\n" + "\n" + "I'd like to schedule a visit to your rental property I found on Rentals." +
                " Please let me know when the best date and time would be.\n" + "\n" + "\n" + "Thank you!");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Contact.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
