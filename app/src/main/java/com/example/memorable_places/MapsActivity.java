package com.example.memorable_places;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    List<Address> addresses = null;
    static SharedPreferences sharedPreferences;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                updateChanges(location, "this is your way");
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void updateChanges(Location location, String way) {

        LatLng locat = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(locat).title(way));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locat));
        try {
            sharedPreferences.edit().putString("places", ObjectSerializer.serialize(MainActivity.places)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void updateChanges1(LatLng latLng) {

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("previous location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));


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
        mMap.setOnMapLongClickListener(this);

        if (getIntent().getExtras().getInt("arg") == 0) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateChanges(location, "this is your way");


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
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                updateChanges(location, "this is your way");

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

        } else if (getIntent().getExtras().getInt("arg") == 1) {
            updateChanges1(MainActivity.longClicks.get(0));
        } else if (getIntent().getExtras().getInt("arg") == 2) {
            updateChanges1(MainActivity.longClicks.get(1));
        } else if (getIntent().getExtras().getInt("arg") == 3) {
            updateChanges1(MainActivity.longClicks.get(2));
        } else if (getIntent().getExtras().getInt("arg") == 4) {
            updateChanges1(MainActivity.longClicks.get(3));
        } else if (getIntent().getExtras().getInt("arg") == 5) {
            updateChanges1(MainActivity.longClicks.get(4));
        } else if (getIntent().getExtras().getInt("arg") == 6) {
            updateChanges1(MainActivity.longClicks.get(5));
        } else if (getIntent().getExtras().getInt("arg") == 7) {
            updateChanges1(MainActivity.longClicks.get(6));
        }


    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("way"));
        try {
            Geocoder land = new Geocoder(getApplicationContext(), Locale.ENGLISH);
            addresses = land.getFromLocation(latLng.latitude, latLng.longitude, 1);
            MainActivity.places.add(addresses.get(0).getAddressLine(0));
            MainActivity.savedPlaces.add(addresses.get(0).getCountryName());

            sharedPreferences.edit().putString("places", ObjectSerializer.serialize(MainActivity.places)).apply();

            Log.i("onclick is", MainActivity.places.toString());

            MainActivity.longClicks.add(latLng);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "location not saved", Toast.LENGTH_SHORT).show();

        }
        MainActivity.adapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "location saved", Toast.LENGTH_SHORT).show();
        Log.i("pressed", addresses.get(0).getAddressLine(0).toString());

    }
}
