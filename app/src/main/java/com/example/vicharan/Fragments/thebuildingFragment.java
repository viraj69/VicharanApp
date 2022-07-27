package com.example.vicharan.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class thebuildingFragment extends Fragment {

    ImageView gymtick, pooltick, conciergetick, hourtick, bicycletick, storagetick, elevatortick;
    FirebaseFirestore fstore;
    static String id;

    public static thebuildingFragment getInstance(String Aptid) {
        id = Aptid;
        thebuildingFragment ThebuildingFragment = new thebuildingFragment();
        return ThebuildingFragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.row_thebuilding, container, false);

        gymtick = view.findViewById(R.id.gymtick);
        pooltick = view.findViewById(R.id.pooltick);
        conciergetick = view.findViewById(R.id.conciergetick);
        hourtick = view.findViewById(R.id.hourtick);
        bicycletick = view.findViewById(R.id.bicycletick);
        storagetick = view.findViewById(R.id.storagetick);
        elevatortick = view.findViewById(R.id.elevatortick);

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

                        String gt = data1.get("Gym").toString();
                        String pt = data1.get("Pool").toString();
                        String ct = data1.get("Concierge").toString();
                        String ht = data1.get("24_Hour_Security").toString();
                        String bt = data1.get("Bicycle_Parking").toString();
                        String st = data1.get("Storage_Space").toString();
                        String et = data1.get("Elevator").toString();
                        if (gt.equals("Yes")) {
                            gymtick.setImageResource(R.drawable.rightmark);
                        } else {
                            gymtick.setImageResource(R.drawable.wrongmark);
                        }
                        if (pt.equals("Yes")) {
                            pooltick.setImageResource(R.drawable.rightmark);
                        } else {
                            pooltick.setImageResource(R.drawable.wrongmark);
                        }
                        if (ct.equals("Yes")) {
                            conciergetick.setImageResource(R.drawable.rightmark);
                        } else {
                            conciergetick.setImageResource(R.drawable.wrongmark);
                        }
                        if (ht.equals("Yes")) {
                            hourtick.setImageResource(R.drawable.rightmark);
                        } else {
                            hourtick.setImageResource(R.drawable.wrongmark);
                        }
                        if (bt.equals("Yes")) {
                            bicycletick.setImageResource(R.drawable.rightmark);
                        } else {
                            bicycletick.setImageResource(R.drawable.wrongmark);
                        }
                        if (st.equals("Yes")) {
                            storagetick.setImageResource(R.drawable.rightmark);
                        } else {
                            storagetick.setImageResource(R.drawable.wrongmark);
                        }
                        if (et.equals("Yes")) {
                            elevatortick.setImageResource(R.drawable.rightmark);
                        } else {
                            elevatortick.setImageResource(R.drawable.wrongmark);
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
