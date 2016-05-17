package com.project.sakib.task;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker gps;
    DBhelper2 D;
    private String data="";
    LatLng mark;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gps = new GPSTracker(MapsActivity.this);
        D=new DBhelper2(getApplicationContext());
        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            String strDate = sdf.format(cal.getTime());

            Bundle getuserID = getIntent().getExtras();
            if (getuserID != null) {
                String s=getIntent().getStringExtra("phn");
                position a=new position(s,Double.toString(latitude),Double.toString(longitude),strDate);
                long val=D.InsertPosition(a);
                if(val>0) {
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Insertion Error!!", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String urlString = "https://apex.oracle.com/pls/apex/share/taftech/tempinfo/";
        new ProcessJSON().execute(urlString);

    }
    private class ProcessJSON extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            if(stream !=null){
                String id;
                String LAST_UPDATED;
                String lat;
                String lng;
                String destination;
                String stat;
                try {
                    data=stream;
                    JSONObject jsono = new JSONObject(data);
                    JSONArray jsonArray = jsono.getJSONArray("items");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        try {
                            id = object.getString("user_id");
                        }catch (JSONException e)
                        {
                            continue;
                        }
                        try {
                            LAST_UPDATED = object.getString("last_updated");
                        }catch (JSONException e)
                        {
                            continue;
                        }
                        try {
                            lat=object.getString("user_lat");
                        }catch (JSONException e)
                        {
                            continue;
                        }
                        try {
                            lng=object.getString("user_long");
                        }catch (JSONException e)
                        {
                            continue;
                        }
                        try {
                            destination=object.getString("destination");
                        }catch (JSONException e)
                        {
                            continue;
                        }
                        try {
                            stat = object.getString("location_status");
                        }catch (JSONException e)
                        {
                            continue;
                        }
                        if(!LAST_UPDATED.equals("never!")) {
                            mark = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                            if (stat.equals("Y")) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(mark)
                                        .title(id + " " + destination)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 15));

                                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                            } else if (stat.equals("O")) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(mark)
                                        .title(id + " " + destination)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));
                            }
                        }
                    }
                    mark = new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(mark)
                            .title("My Position"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mark, 15));

                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                } catch (JSONException e)
                {
                    Toast.makeText(getApplicationContext(), "error",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

            }
        }
    }
}

