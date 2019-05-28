package com.example.personalwellness;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.Manifest;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.List;

public class GriefReferralPage extends AppCompatActivity implements OnMapReadyCallback {
    ResourceDB resourceDB = ResourceDB.getResourceDB();
    List<Resource> resources = resourceDB.getResourceList();
    double lat;
    double lng;
    String resourceName;

    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final float ZOOM = 15f;
    public static final int REQUEST_CODE = 2323;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean permissionGranted = false;
    private LatLng userCoordinates;
    private double distance = -1;

    private GoogleMap mMap;

    private static final String TAG = GriefReferralPage.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grief_referral_page);

        String extra = getIntent().getStringExtra("buttonid");
        TextView textV = (TextView) findViewById(R.id.griefRef);
        TextView textAbout = (TextView) findViewById(R.id.resourceabout);
        TextView textAddress = (TextView) findViewById(R.id.address);
        initMap();
        checkLocationPermission();

        for (int i = 0; i < resources.size(); i++) {
            if (extra.equals((i+1)+"")) {
                resourceName = resources.get(i).getName();
                textV.setText(resourceName);
                textAbout.setText(resources.get(i).getSummary());
                lat = resources.get(i).getLat();
                lng = resources.get(i).getLng();
                if (!resources.get(i).getAddress().equals("")) {
                    textAddress.setText("Address:" + resources.get(i).getAddress());
                }

            }
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
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "-----------creating map");
        moveCam(new LatLng(lat, lng), resourceName);
        if (permissionGranted) {
            getUserLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

        }
    }

    private void calculateDistance() {
        if (userCoordinates != null) {
            double userLng = userCoordinates.longitude;
            double userLat = userCoordinates.latitude;
            double theta = userLng - lng;
            distance = Math.sin(deg2rad(userLat)) * Math.sin(deg2rad(lat)) + Math.cos(deg2rad(userLat)) *
                    Math.cos(deg2rad(lat)) * Math.cos(deg2rad(theta));
            distance = Math.acos(distance);
            distance = rad2deg(distance);
            distance = distance * 60 * 1.1515;
            TextView textDistance = (TextView) findViewById(R.id.distance_text);
            DecimalFormat df = new DecimalFormat("#.##");
            String d = df.format(distance);
            textDistance.setText("Distance from current location: " + d + " miles");
        } else {
            TextView textDistance = (TextView) findViewById(R.id.distance_text);
            textDistance.setText("Enable location services to see distance from your current location");
        }
    }


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    // initializes the map
    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void moveCam(LatLng coords, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 16f));
        if (!title.equals("My location")) {
            MarkerOptions options = new MarkerOptions().position(coords).title(title);
            mMap.addMarker(options);
        }
    }

    private void getUserLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (permissionGranted) {
                Task loc = mFusedLocationProviderClient.getLastLocation();
                loc.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found users location");
                            Location currLocation = (Location) task.getResult();
                            userCoordinates = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
                            calculateDistance();

                        } else {
                            Log.d(TAG, "onComplete: did not find users location");
                            Toast.makeText(GriefReferralPage.this, "couldn't get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException s) {
            Log.d(TAG, "-----------security exception: " + s.getMessage());
        }
    }

    // checks location permission
    public void checkLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        Log.d(TAG, "-----------checking permission");

        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }
    }

    // checks if the user gave permission to access their location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionGranted = false;
        switch(requestCode) {
            case REQUEST_CODE: {
                if(grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            permissionGranted = false;
                            Log.d(TAG, "-----------permission denied");
                            return;
                        }
                    }
                    Log.d(TAG, "-----------permission granted");
                    permissionGranted = true;
                    initMap();
                }
            }
        }
    }
}
