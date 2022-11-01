package com.example.picklocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float ZOOM_VALUE = 15f;
    private static final String TAG = "MapActivity";


    private Boolean permissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFuesedLocationProviderClinet;
    private TextView latitude_value;
    private TextView longitude_value;

    private String latitude;
    private String longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Check the permission
        getPermission();
    }

    // Get Permissions
    private void getPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Request Permissions if it not enabled
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            permissionGranted = false;
                            return;
                        }
                    }
                    permissionGranted = true;
                    // initilize the map
                    initMap();

                }
            }
        }
    }

    // Get the device current location
    private void getMyCurrentLocation() {
        Log.d(TAG, "getMyCurrentLocatio body");
        mFuesedLocationProviderClinet = LocationServices.getFusedLocationProviderClient(MapActivity.this);

        try {
            if (permissionGranted) {
                Task location = mFuesedLocationProviderClinet.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // get the current location
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), ZOOM_VALUE);
                            // style for the current location marker
                            mMap.addMarker( new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())).title(getResources().getString(R.string.marker_title)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));

                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException block" + e.getMessage());

        }

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

// set the geographic coordinates on the view
    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++");
        Log.d(TAG, "moveCamera body : Curent Location info is ->  longitude: " + latLng.longitude + "  latitude: " + latLng.latitude);
        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        latitude_value = (TextView) findViewById(R.id.location_latitude_value);
        longitude_value = (TextView) findViewById(R.id.location_longitude_value);

        latitude = String.valueOf(latLng.latitude);
        longitude = String.valueOf(latLng.longitude);

        latitude_value.setText(latitude);
        longitude_value.setText(longitude);
    }

    // permissions granted and map ready to be displayed
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Toast.makeText(MapActivity.this, "Map is Ready to display", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (permissionGranted) {
            getMyCurrentLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);


        }
    }

}