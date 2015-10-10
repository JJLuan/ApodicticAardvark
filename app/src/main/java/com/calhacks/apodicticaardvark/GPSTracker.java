package com.calhacks.apodicticaardvark;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Created by John on 10/10/2015.
 */
public class GPSTracker {
    private long REFRESH_TIME=10000, REFRESH_DISTANCE=10;
    LocationManager locationMgr;

    public GPSTracker(Context context, LocationListener l) throws SecurityException{
        locationMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, REFRESH_TIME,REFRESH_DISTANCE,l);
    }

    public Location getLocation() throws SecurityException{
        return locationMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
}
