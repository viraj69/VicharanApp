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

public class theunitFragment extends Fragment {

    TextView sizeApartment, furnished, ac, smokingpermit;
    ImageView laundrytick, laundrybuildingtick, dishwashertick, fridgetick, yardtick, balconytick;
    FirebaseFirestore fstore;
    static String id;

    public static theunitFragment getInstance(String AptId) {
        id = AptId;
        theunitFragment TheunitFragment = new theunitFragment();
        return TheunitFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.row_theunit, container, false);

        sizeApartment = view.findViewById(R.id.sizeApartment);
        furnished = view.findViewById(R.id.furnished);
        laundrytick = view.findViewById(R.id.laundrytick);
        laundrybuildingtick = view.findViewById(R.id.laundrybuildingtick);
        dishwashertick = view.findViewById(R.id.dishwashertick);
        fridgetick = view.findViewById(R.id.fridgetick);
        ac = view.findViewById(R.id.ac);
        yardtick = view.findViewById(R.id.yardtick);
        balconytick = view.findViewById(R.id.balconytick);
        smokingpermit = view.findViewById(R.id.smokingpermit);

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

                        String szapt = data1.get("Size").toString();
                        String fur = data1.get("Furnished").toString();
                        String lt = data1.get("UnitLaundry").toString();
                        String ltb = data1.get("BuildingLaundry").toString();
                        String dw = data1.get("Dishwasher").toString();
                        String ft = data1.get("Fridge").toString();
                        String acd = data1.get("AirConditioning").toString();
                        String yt = data1.get("Yard").toString();
                        String bt = data1.get("Balcony").toString();
                        String sp = data1.get("SmokePermitted").toString();
                        sizeApartment.setText(szapt);
                        furnished.setText(fur);
                        ac.setText(acd);
                        smokingpermit.setText(sp);
                        if (lt.equals("Yes")) {
                            laundrytick.setImageResource(R.drawable.rightmark);
                        } else {
                            laundrytick.setImageResource(R.drawable.wrongmark);
                        }
                        if (ltb.equals("Yes")) {
                            laundrybuildingtick.setImageResource(R.drawable.rightmark);
                        } else {
                            laundrybuildingtick.setImageResource(R.drawable.wrongmark);
                        }
                        if (dw.equals("Yes")) {
                            dishwashertick.setImageResource(R.drawable.rightmark);
                        } else {
                            dishwashertick.setImageResource(R.drawable.wrongmark);
                        }
                        if (ft.equals("Yes")) {
                            fridgetick.setImageResource(R.drawable.rightmark);
                        } else {
                            fridgetick.setImageResource(R.drawable.wrongmark);
                        }
                        if (yt.equals("Yes")) {
                            yardtick.setImageResource(R.drawable.rightmark);
                        } else {
                            yardtick.setImageResource(R.drawable.wrongmark);
                        }
                        if (bt.equals("Yes")) {
                            balconytick.setImageResource(R.drawable.rightmark);
                        } else {
                            balconytick.setImageResource(R.drawable.wrongmark);
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
