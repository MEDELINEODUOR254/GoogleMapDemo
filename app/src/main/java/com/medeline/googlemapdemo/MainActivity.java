package com.medeline.googlemapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private GoogleMap showMap;
    public Switch locationSwitch;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fgm_maps_id);
        mapFragment.getMapAsync(this);
        locationSwitch=findViewById(R.id.sth_switch_id);
        locationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //requesting permission
                requestPermissions();
            }
        });

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
    }
    /*end of oncreate method*/

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        showMap = googleMap;
        showMap.getUiSettings().setMyLocationButtonEnabled(true);
        showMap.setMyLocationEnabled(true);

    }

    //for getting user permission
    private void requestPermissions() {

        if (locationSwitch.isChecked()){

            if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                //we will show the location
            }
            else
            {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                },7);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7) {
            if (grantResults.length >0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //showing the location
                showUserLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void showUserLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>()
        {
            @Override
            public void onComplete(@NonNull Task<Location> task)
            {
                if (task.isSuccessful())
                {
                    lastKnownLocation=task.getResult();
                    if (lastKnownLocation!=null);
                    LatLng latLng=new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
                    showMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,30));


                }

            }
        });
    }


}