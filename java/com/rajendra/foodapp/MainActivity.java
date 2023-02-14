package com.rajendra.foodapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context ;
import android.location.LocationManager ;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;
import com.rajendra.foodapp.adapter.AsiaFoodAdapter;
import com.rajendra.foodapp.adapter.MapActivity2;
import com.rajendra.foodapp.adapter.PopularFoodAdapter;
import com.rajendra.foodapp.model.AsiaFood;
import com.rajendra.foodapp.model.PopularFood;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ImageView imageView;
    RecyclerView popularRecycler, asiaRecycler;
    PopularFoodAdapter popularFoodAdapter;
    private Toolbar toolbar;
    TextView name;
    AsiaFoodAdapter asiaFoodAdapter;
    GoogleSignInClient mGoogleSignInClient;
    ImageButton btnnav,pinbtn;
    boolean gps_enabled = false;
    private static final String TAG = "MapActivitiy1";
    private static final int ERROR_DIALOG_REQEST = 9001;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        if(isServicesOk()){
            init();
        }
       toolbar= findViewById(R.id.bottomAppBar2);
        setSupportActionBar(toolbar);
        pinbtn = findViewById(R.id.pinBtn);
        btnnav =findViewById(R.id.NavBtn);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        btnnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setVisibility(View.VISIBLE);
            }
        });





        pinbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        checkGPS();


    }
});





        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        imageView =findViewById(R.id.imageView);
        name =findViewById(R.id.username);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            Uri personPhoto = acct.getPhotoUrl();
            String personName = acct.getDisplayName();

            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
            name.setText(personName);
        }


        List<PopularFood> popularFoodList = new ArrayList<>();

        popularFoodList.add(new PopularFood("Pizza Comba Deal", "700", R.drawable.ofer));
        popularFoodList.add(new PopularFood("Chicken Bacon", "170", R.drawable.piz1));
        popularFoodList.add(new PopularFood("Devilled Chicken", "2000", R.drawable.piz3));
        popularFoodList.add(new PopularFood("Tandoori Chicken", "750", R.drawable.piz5));
        popularFoodList.add(new PopularFood("Sausage Delight", "1700", R.drawable.piz6));
        popularFoodList.add(new PopularFood("Cheese Lovers", "2550", R.drawable.piza7));

        setPopularRecycler(popularFoodList);


        List<AsiaFood> asiaFoodList = new ArrayList<>();
        asiaFoodList.add(new AsiaFood("Popcorn Chicken Pizza", "200", R.drawable.hut1, "4.5", "Briand Restaurant"));
        asiaFoodList.add(new AsiaFood("Spicy Veggie with Paneer", "250", R.drawable.hut2, "4.2", "Friends Restaurant"));
        asiaFoodList.add(new AsiaFood("Black Chicken", "200", R.drawable.hut3, "4.5", "Briand Restaurant"));
        asiaFoodList.add(new AsiaFood("BBQ Chicken", "250", R.drawable.hut5, "4.2", "Friends Restaurant"));
        asiaFoodList.add(new AsiaFood("Chicken Hawaiian", "200", R.drawable.hut6, "4.5", "Briand Restaurant"));
        asiaFoodList.add(new AsiaFood("Double Chicken & Cheese Fiesta", "250", R.drawable.piz47, "4.2", "Friends Restaurant"));

        setAsiaRecycler(asiaFoodList);

    }

    public void checkGPS(){



        LocationManager lm = (LocationManager)
                getSystemService(Context. LOCATION_SERVICE ) ;
        boolean gps_enableds = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
        }
        catch (Exception e) {
            e.printStackTrace() ;
        }
        if (gps_enabled) {

            Intent intent = new Intent(MainActivity.this, MapActivity2.class);
            startActivity(intent);

        }
        else {
            new AlertDialog.Builder(MainActivity. this )
                    .setMessage( "GPS Enable" )
                    .setPositiveButton( "Settings"  , new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                    startActivity( new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS )) ;
                                }
                            })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

        }
    }

    private void  init(){



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.cart2:
                Intent intent = new Intent(MainActivity.this, CartFood.class);
                startActivity(intent);
            case R.id.heart:
                //nothing
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private boolean isServicesOk() {

        Log.d(TAG,"isServiceOk :checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            //Everthing is fine and the user can make map reqest
            Log.d(TAG, "isServicesOk: Google play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error but we can resolve it
            Log.d(TAG, "isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available, ERROR_DIALOG_REQEST);
            dialog.show();
        }else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }



    private void setPopularRecycler(List<PopularFood> popularFoodList) {

        popularRecycler = findViewById(R.id.popular_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        popularRecycler.setLayoutManager(layoutManager);
        popularFoodAdapter = new PopularFoodAdapter(this, popularFoodList);
        popularRecycler.setAdapter(popularFoodAdapter);

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this,"Signed out successfully",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
    private void setAsiaRecycler(List<AsiaFood> asiaFoodList) {

        asiaRecycler = findViewById(R.id.asia_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        asiaRecycler.setLayoutManager(layoutManager);
        asiaFoodAdapter = new AsiaFoodAdapter(this, asiaFoodList);
        asiaRecycler.setAdapter(asiaFoodAdapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch ((item.getItemId())){
            case  R.id.nav_logout:
                signOut();
                break;
            case  R.id.cart:
                Intent intent = new Intent(MainActivity.this, CartFood.class);
                startActivity(intent);
                break;

        }
        return true;
    }

// Hi all,
    // Today we are going to build a food app.
    // so the first things frist lets add font and image assets
    // so lets see the design
    // now we will setup recyclerview
    // first we make a model class then adapter class.
    // now we will create a recyclerview row item layout file
    // so our adapter class is ready
    // now we will bind data with recyclerview
    // so we have successfully setup popular recyclerview
    // now same setup we need to do for asia food
    // will make model class then adapter and setup recyclerview
    // so lets do it fast.
    // so asia food setup done.
    // Now we will setup Bottom app bar
    // bottom app bar setup done if you want you can increase menu item in menu file
    // now we will setup details activity and on click listener in recyclerview row item
    // so this tutorial has been completed if you have any
    // question and query please do comment
    // Like share and subscribe
    // Thankyou for watching
    // see you in the next tutorial
}
