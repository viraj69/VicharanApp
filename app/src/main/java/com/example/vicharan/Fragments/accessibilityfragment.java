package com.example.vicharan.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vicharan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class accessibilityfragment extends Fragment {

    TextView barrier, visual, accessible;
    ImageView wheelchairtick, brailletick, audiotick;

    FirebaseFirestore fstore;
    static String id;

    public static accessibilityfragment getInstance(String Aptid) {
        id = Aptid;
        accessibilityfragment AccessibilityFragment = new accessibilityfragment();

        return AccessibilityFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.row_accessibility, container, false);

        wheelchairtick = view.findViewById(R.id.wheelchairtick);
        brailletick = view.findViewById(R.id.brailletick);
        audiotick = view.findViewById(R.id.audiotick);
        barrier = view.findViewById(R.id.barrier);
        visual = view.findViewById(R.id.visual);
        accessible = view.findViewById(R.id.accessible);

        getdata();

        return view;
    }

    private void getdata() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Apartment").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data1 = document.getData();

                        String wt = data1.get("Wheelchair_Accessible").toString();
                        String bt = data1.get("Braille_Labels").toString();
                        String at = data1.get("Audio_Prompts").toString();
                        String bar = data1.get("Barrier_free_Entrance_Ramps").toString();
                        String vis = data1.get("VisualAids").toString();
                        String acc = data1.get("Accessible_Washrooms_in_suite").toString();
                        barrier.setText(bar);
                        visual.setText(vis);
                        accessible.setText(acc);
                        if (wt.equals("Yes")) {
                            wheelchairtick.setImageResource(R.drawable.rightmark);
                        } else {
                            wheelchairtick.setImageResource(R.drawable.wrongmark);
                        }
                        if (bt.equals("Yes")) {
                            brailletick.setImageResource(R.drawable.rightmark);
                        } else {
                            brailletick.setImageResource(R.drawable.wrongmark);
                        }
                        if (at.equals("Yes")) {
                            audiotick.setImageResource(R.drawable.rightmark);
                        } else {
                            audiotick.setImageResource(R.drawable.wrongmark);
                        }

                    } else {
                        Log.d("tagvv", "No such document");
                    }
                } else {
                    Log.d("tagvv", "get failed with ", task.getException());
                }
            }
        });
    }


}
