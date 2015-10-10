package com.calhacks.apodicticaardvark;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;

public class MainActivity extends AppCompatActivity {
    private final int currentApiVersion = Build.VERSION.SDK_INT;
    //embedded map fragment
    private Map map = null;

    //map fragment embedded in this activity
    private MapFragment mapFrag = null;

    Context mContext;
    private double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);
                if (locationManager != null) {
                    try {

                        // Getting GPS status
                        boolean isGPSEnabled = locationManager
                                .isProviderEnabled(LocationManager.GPS_PROVIDER);

                        // Getting network status
                        boolean isNetworkEnabled = locationManager
                                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        Location location = null;

                        if (!isGPSEnabled && !isNetworkEnabled) {
                            // No network provider is enabled
                        } else {
                            if (isNetworkEnabled) {
                                locationManager.requestLocationUpdates(
                                        LocationManager.NETWORK_PROVIDER,
                                        50000,
                                        100, new LocationListener() {
                                            @Override
                                            public void onLocationChanged(Location location) {

                                            }

                                            @Override
                                            public void onStatusChanged(String provider, int status, Bundle extras) {

                                            }

                                            @Override
                                            public void onProviderEnabled(String provider) {

                                            }

                                            @Override
                                            public void onProviderDisabled(String provider) {

                                            }
                                        });
                                Log.d("Network", "Network");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    }
                                }
                            }
                            // If GPS enabled, get latitude/longitude using GPS Services
                            if (isGPSEnabled) {
                                if (location == null) {
                                    locationManager.requestLocationUpdates(
                                            LocationManager.GPS_PROVIDER,
                                            50000,
                                            100, new LocationListener() {
                                                @Override
                                                public void onLocationChanged(Location location) {

                                                }

                                                @Override
                                                public void onStatusChanged(String provider, int status, Bundle extras) {

                                                }

                                                @Override
                                                public void onProviderEnabled(String provider) {

                                                }

                                                @Override
                                                public void onProviderDisabled(String provider) {

                                                }
                                            });
                                    Log.d("GPS Enabled", "GPS Enabled");
                                    if (locationManager != null) {
                                        location = locationManager
                                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                        if (location != null) {
                                            latitude = location.getLatitude();
                                            longitude = location.getLongitude();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }


        });

        //search for map fragment setup by calling init()
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFrag.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    //retrieve a reference of the map from the map fragment
                    map = mapFrag.getMap();
                    //set the map to the berkeley region (no animation)
                    map.setCenter(new GeoCoordinate(37.8717, -122.2728), Map.Animation.NONE);
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                    System.out.println(map);
                } else {
                    System.out.println("Error: " + error.toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
