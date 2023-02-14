package com.rajendra.foodapp.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rajendra.foodapp.MainActivity;
import com.rajendra.foodapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity2 extends AppCompatActivity  implements OnMapReadyCallback{
    AlertDialog.Builder builder;
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(mLocationPermissionsGranted){
            getDeviceLocation();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }
    FirebaseFirestore mFirestore;
    public static String LocAddress;
    EditText mSeachText;
     String TAG = "MapActivity2";
     String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQEST_CODE=1234;
      GoogleMap mMap;
     ImageView mGps;
     Button cnfmButton;
    float  DEFAULT_ZOOM = 15f;
     FusedLocationProviderClient mFusedLocationProviderClient;
    public static boolean locChecker;

    private  boolean mLocationPermissionsGranted = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSeachText =(EditText) findViewById(R.id.input_search);
        mGps =(ImageView) findViewById(R.id.id_gps);
        cnfmButton = (Button)findViewById(R.id.confirm_button);
        getLocationPermission();
        Places.initialize(getApplicationContext(),"AIzaSyDUsC2e47TT0mhfTZ8OkUfFabyfnLKhu20");
        mSeachText.setFocusable(false);
        mSeachText.setText(null);
        mSeachText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS
                        ,Place.Field.LAT_LNG,Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        ,fieldList).build(MapActivity2.this);

                startActivityForResult(intent,100);



            }
        });



        cnfmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if(locChecker==false){
                    new AlertDialog.Builder(MapActivity2. this )
                            .setMessage( "Address is not Selected ⚠️" )
                            .show();

                }
                if (locChecker==true){
                    new AlertDialog.Builder(MapActivity2. this )
                            .setMessage( "Location confirmed successfully ✔" )
                            .show();
                    LocAddress = mSeachText.getText().toString();


                    Bill_info.billItm.put("Location",LocAddress);


                }
            }
        });

        mFirestore = FirebaseFirestore.getInstance();



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode ==  RESULT_OK){

            Place place =  Autocomplete.getPlaceFromIntent(data);
            mSeachText.setText(place.getName()+" "+place.getAddress());
            locChecker=true;
            geoLocate();

        }

        else  if (requestCode== AutocompleteActivity.RESULT_ERROR){
            Status status  = Autocomplete.getStatusFromIntent(data);

            Toast.makeText(getApplicationContext(),status.getStatusMessage()
                    ,Toast.LENGTH_SHORT).show();
        }
    }

    public void  init(){
        Log.d(TAG, "init: initializing");
        mSeachText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH || actionId==EditorInfo.IME_ACTION_DONE)
                    {
                     //execute our methods for searching
                        geoLocate();
                    }


                return false;
            }
        });
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

    }
    public void  geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");
        String searachString = mSeachText.getText().toString();
        Geocoder geocoder = new Geocoder(MapActivity2.this);
       List<Address> list = new ArrayList<>();
       try {

           list = geocoder.getFromLocationName(searachString,1);

       }catch (IOException e){

           Log.e(TAG, "geoLocate: IOException"+e.getMessage() );
       }
if(list.size()>0){
    Address address =list.get(0);
    Log.d(TAG, "geoLocate: found a location:"+ address.toString());
   // Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

    moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,address.getAddressLine(0));
}



    }

    public void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current locations");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
    if (mLocationPermissionsGranted){
        Task  location = mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
           if(task.isSuccessful()){
               Log.d(TAG, "onComplete: found loacation!");
               Location curreLocation = (Location) task.getResult();
               moveCamera(new LatLng(curreLocation.getLatitude(), curreLocation.getLongitude()),DEFAULT_ZOOM,"My location");

           }
           else {
               Log.d(TAG, "onComplete: current location is unavailbe");
               Toast.makeText(MapActivity2.this, "Unable to ger current locaton", Toast.LENGTH_SHORT).show();
           }
            }
        });
    }

        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation:SecurityException"+e.getMessage());
        }
    }



    public void moveCamera(LatLng latLng, float zoom, String title){

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My location"))
        {

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }


    }
    public void initMap() {
        Log.d(TAG, "initMap: inizilazing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity2.this);
    }

    public void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting loacation permission");
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted=true;
                initMap();
            }else
            {
                ActivityCompat.requestPermissions(this,
                        permission,
                        LOCATION_PERMISSION_REQEST_CODE);
            }
        }else
        {

            ActivityCompat.requestPermissions(this,
                    permission,
                    LOCATION_PERMISSION_REQEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
       mLocationPermissionsGranted = false;
       switch (requestCode){
           case LOCATION_PERMISSION_REQEST_CODE: {
               if(grantResults.length > 0){
                   for (int i =0; i <grantResults.length; i++){
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                                 mLocationPermissionsGranted = false;
                                Log.d(TAG, "onRequestPermissionsResult: permission failed");
                                return;
                            }
                   }
                   Log.d(TAG, "onRequestPermissionsResult: permission granted");
                   mLocationPermissionsGranted = true;
                   //initialize our map
                   initMap();
               }

           }
       }
    }



}
