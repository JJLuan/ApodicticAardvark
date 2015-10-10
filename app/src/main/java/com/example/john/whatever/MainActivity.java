package com.example.john.whatever;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;

public class MainActivity extends AppCompatActivity {
    //embedded map fragment
    private Map map = null;

    //map fragment embedded in this activity
    private MapFragment mapFrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //search for map fragment setup by calling init()
        mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFrag.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if(error == OnEngineInitListener.Error.NONE) {
                    //retrieve a reference of the map from the map fragment
                    map = mapFrag.getMap();
                    //set the map to the berkeley region (no animation)
                    map.setCenter(new GeoCoordinate(37.8717, -122.2728), Map.Animation.NONE);
                    map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                    System.out.println(map);
                }
                else{
                    System.out.println("Error: "+error.toString());
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
