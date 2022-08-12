package com.example.vicharan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vicharan.Activity.prasangDetails.PrasangDetails;
import com.example.vicharan.R;
import com.example.vicharan.firebase.FirebaseUtils;
import com.example.vicharan.firebase.location.DbLocation;
import com.example.vicharan.firebase.location.Location;
import com.example.vicharan.firebase.media.DbMedia;
import com.example.vicharan.firebase.media.Media;
import com.example.vicharan.firebase.prasang.DbPrasang;
import com.example.vicharan.firebase.prasang.Prasang;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PrasangDialog {
    private final Activity activity;
    private final Dialog prasangDialog;

    public PrasangDialog(Activity activity) {
        this.activity = activity;
        prasangDialog = new Dialog(activity);
        prasangDialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        prasangDialog.setCancelable(true);
        prasangDialog.setContentView(R.layout.activity_apartment_dialog);
        Window window = prasangDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
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

    private void initPrasangDialoagNextClickListener(final Location location, final Prasang prasang, final Media media) {
        ImageView next = prasangDialog.findViewById(R.id.btn_dialog);
        next.setOnClickListener(v -> PrasangDetails.startActivity(activity, location, prasang, media));
    }

    private void loadLocation(Location location) {
        TextView place = prasangDialog.findViewById(R.id.place1);
        place.setText(location.getPlace());
    }

    private void loadPrasang(Prasang prasang) {
        TextView title = prasangDialog.findViewById(R.id.title);
        title.setText(prasang.getTitle());
        TextView sutra = prasangDialog.findViewById(R.id.sutra);
        sutra.setText(prasang.getSutra());
    }

    private void loadImage(Uri uri) {
        ImageView image = prasangDialog.findViewById(R.id.dialogimage);
        Picasso.get().load(uri).fit().into(image);
    }

    private void fetchImage(String prasangId, String mediaName) {
        FirebaseUtils.loadImage(prasangId, mediaName, this::loadImage, e -> {
        });
    }

    private void fetchMedia(final Location location, final Prasang prasang) {
        DbMedia.getById(prasang.getMedia().get(0), (Media media) -> {
            if (media == null) return;
            fetchImage(prasang.getId(), media.getName());
            initPrasangDialoagNextClickListener(location, prasang, media);
        });
    }

    private void fetchPrasang(final Location location) {
        DbPrasang.getByLocationId(location.getId(), (List<Prasang> prasangs) -> {
            if (prasangs == null || prasangs.isEmpty()) {
                System.out.println("no prasangs found");
                return;
            }
            Prasang prasang = prasangs.get(0);
            loadLocation(location);
            loadPrasang(prasang);
            fetchMedia(location, prasang);
        });
    }

    private void fetchLocationAndPrasang(String locationId) {
        DbLocation.getById(locationId, this::fetchPrasang);
    }
}
