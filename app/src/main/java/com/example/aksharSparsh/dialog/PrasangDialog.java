package com.example.aksharSparsh.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aksharSparsh.Activity.prasangDetails.PrasangDetails;
import com.example.aksharSparsh.GlobalApplication;
import com.example.aksharSparsh.R;
import com.example.aksharSparsh.firebase.FirebaseUtils;
import com.example.aksharSparsh.firebase.location.DbLocation;
import com.example.aksharSparsh.firebase.location.GenericLocation;
import com.example.aksharSparsh.firebase.location.Location;
import com.example.aksharSparsh.firebase.media.DbMedia;
import com.example.aksharSparsh.firebase.media.Media;
import com.example.aksharSparsh.firebase.prasang.DbPrasang;
import com.example.aksharSparsh.firebase.prasang.GenericPrasang;
import com.example.aksharSparsh.firebase.prasang.Prasang;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.LinkedList;
import java.util.List;

public class PrasangDialog {
    private final Activity activity;
    private final Dialog prasangDialog;

    public PrasangDialog(Activity activity) {
        this.activity = activity;
        prasangDialog = new Dialog(activity);
        prasangDialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        prasangDialog.setCancelable(true);
        prasangDialog.setContentView(R.layout.c_prasang_overlay);
        prasangDialog.getWindow().setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.gradient_gray));
        Window window = prasangDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.y = 150;
        window.setAttributes(wlp);
    }

    private void showDialog() {
        prasangDialog.show();
    }

    public void loadDataAndShowDialog(String locationId) {
        fetchLocationAndPrasang(locationId);
        showDialog();
    }

    private void initPrasangDialoagNextClickListener(final Location location, final Prasang prasang, final Media media, LinkedList<DbPrasang.LocationPrasangPair> locationPrasangPairs) {
        View next = prasangDialog.findViewById(R.id.root);
        next.setOnClickListener(v -> PrasangDetails.startActivity(activity, locationPrasangPairs));
    }

    private void loadLocation(GenericLocation location) {
        TextView place = prasangDialog.findViewById(R.id.place1);
        place.setText(location.getPlace());
    }

    private void loadPrasang(GenericPrasang prasang) {
        TextView title = prasangDialog.findViewById(R.id.title);
        title.setText(prasang.getTitle());
        TextView sutra = prasangDialog.findViewById(R.id.sutra);
        sutra.setText(prasang.getSutra());
    }

    private void loadImage(Uri uri) {
        ImageView image = prasangDialog.findViewById(R.id.dialogimage);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(7)
                .oval(false)
                .build();

        Picasso.get()
                .load(uri)
                .fit()
                .transform(transformation)
                .into(image);
    }

    private void fetchImage(String prasangId, String mediaName) {
        FirebaseUtils.loadImage(prasangId, mediaName, this::loadImage, e -> {
        });
    }

    private void fetchMedia(final Location location, final Prasang prasang, LinkedList<DbPrasang.LocationPrasangPair> locationPrasangPairs) {
        DbMedia.getById(prasang.getMedia().get(0), (Media media) -> {
            if (media == null) return;
            fetchImage(prasang.getId(), media.getName());
            initPrasangDialoagNextClickListener(location, prasang, media, locationPrasangPairs);
        });
    }

    private void fetchPrasang(final Location location) {
        DbPrasang.getByLocationId(location.getId(), (List<Prasang> prasangs) -> {
            if (prasangs == null || prasangs.isEmpty()) {
                System.out.println("no prasangs found");
                return;
            }
            Prasang prasang = prasangs.get(0);

            boolean isGujaratiLanguageSelected = GlobalApplication.getInstance().isGujaratiLanguageSelected();
            if (isGujaratiLanguageSelected) {
                loadLocation(location.getGujaratiVersion());
                loadPrasang(prasang.getGujaratiVersion());
            } else {
                loadLocation(location.getEnglishVersion());
                loadPrasang(prasang.getEnglishVersion());
            }

            LinkedList<DbPrasang.LocationPrasangPair> locationPrasangPairs = new LinkedList<>();
            for (Prasang p : prasangs)
                locationPrasangPairs.add(new DbPrasang.LocationPrasangPair(location, p));
            fetchMedia(location, prasang, locationPrasangPairs);
        });
    }

    private void fetchLocationAndPrasang(String locationId) {
        DbLocation.getById(locationId, this::fetchPrasang);
    }
}
