package com.example.vicharan.Fragments.map;

import android.location.Location;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.util.List;

public class LocationCallbackWrapper extends LocationCallback {
    private final LocationCallbackListener listener;

    public LocationCallbackWrapper(LocationCallbackListener listener) {
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationResult(LocationResult locationResult) {
        List<Location> locationList = locationResult.getLocations();
        if (locationList.size() > 0) {
            // get last location since the last location in the list is the newest
            listener.onLocationAvailable(locationResult.getLocations().get(locationList.size() - 1));
        }
    }

    interface LocationCallbackListener {
        void onLocationAvailable(Location location);
    }
}
