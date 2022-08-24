package com.example.aksharSparsh.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.aksharSparsh.Activity.ForgotPassword;
import com.example.aksharSparsh.Activity.MainActivity;
import com.example.aksharSparsh.Activity.savePrasang.SavePrasang;
import com.example.aksharSparsh.Activity.ProfileDetails;
import com.example.aksharSparsh.Activity.prasangList.PrasangList;
import com.example.aksharSparsh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


///**
// * A simple {@link Fragment} subclass.
// * Use the {@link MyAccountFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MyAccountFragment extends PreferenceFragmentCompat {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CircleImageView ivProfile;
    TextView tvPersonName;
    LinearLayout llProfile;
    FirebaseUser fUser;
    private FirebaseAuth auth;
    private FirebaseFirestore mFirebaseFirestore;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference profile = findPreference("profile");
        Preference security = findPreference("security");
        Preference newpost = findPreference("newpost");
        Preference manage = findPreference("manage");
        Preference logout = findPreference("logout");


        profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(getActivity(), ProfileDetails.class);
                startActivity(i);
                return true;
            }
        });
        security.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(getActivity(), ForgotPassword.class);
                startActivity(i);
                return true;
            }
        });
        newpost.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(getActivity(), SavePrasang.class);
                startActivity(i);
                return true;
            }
        });
        manage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(getActivity(), PrasangList.class);
                startActivity(i);
                return true;
            }
        });
        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                Toast.makeText(getActivity(), "Logout Successfully", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }
}