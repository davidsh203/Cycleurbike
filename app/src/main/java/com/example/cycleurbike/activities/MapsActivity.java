package com.example.cycleurbike.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cycleurbike.ClassHelpers.LocationHelper;
import com.example.cycleurbike.ClassHelpers.Route;
import com.example.cycleurbike.R;
import com.example.cycleurbike.fragments.UserHelperClass;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;

    private static final String TAG = "PERMISSION_STORAGE";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1333;
    private GoogleMap mMap;
    private DatabaseReference databaseReference;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;


    private FirebaseAuth mAuth;
    private FirebaseUser mfiFirebaseUser;
    private String getEmail, getUserEmail;


    private Chronometer chronometer;
    private boolean running = false;
    private long pauseOffset;
    private Date currentTime;
    private LatLng latLng1, latLng2, myLocation;
    private Marker marker, marker1, marker2;

    private Button startBtn, stopBtn, pauseBtn;
    private TextView distanceTextView, averageSpeedTextView;
    private ProgressBar progressBar;

    private long distance;
    private long averageSpeed = 0;
    private Location lastLocation;
    private Polyline gpsTrack, gpsShared;
    private File captureImageFile;
    private File imageFile;
    private String startRidingHour;
    private String timeRideToBeSave;
    Route route = new Route();

    Route savedRoute = null;

    UserHelperClass userHelperClass;
    boolean clickedOnce = false, isSharedSaved = false;


    public MapsActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar_activity_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        openDialogGps();
        Gson gson = new Gson();
        String json = getSharedPreferences("file", 0).getString("myRoute", "");
        savedRoute = gson.fromJson(json, Route.class);

        if (savedRoute != null) {
            isSharedSaved = true;
            //Toast.makeText(this, "NOT NULL", Toast.LENGTH_SHORT).show();
            Log.i("SAVED ROUTE", savedRoute.toString());
        } else Log.i("SAVED ROUTE", "FAILED");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        progressBar.setVisibility(View.VISIBLE);

        if (mAuth.getCurrentUser() != null) {

            FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        userHelperClass = snapshot.getValue(UserHelperClass.class);

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MapsActivity.this);

                        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);


                        //stoper:
                        chronometer = findViewById(R.id.stoper);
                        chronometer.setFormat("זמן: %s"); // ברגע לחיצה על התחל תופיע המילה זמן

                        startBtn = findViewById(R.id.startButton);
                        stopBtn = findViewById(R.id.stopButton);
                        pauseBtn = findViewById(R.id.pauseButton);

                        distanceTextView = findViewById(R.id.distance_text_view_activity_map);
                        averageSpeedTextView = findViewById(R.id.speed_avg_text_view_activity_map);

                        mAuth = FirebaseAuth.getInstance();
                        mfiFirebaseUser = mAuth.getCurrentUser();
                        getEmail = mfiFirebaseUser.getEmail();
                        int end = getEmail.indexOf("@");
                        getUserEmail = getEmail.substring(0, end);


                        FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("GET USER DETAILS FAILED", error.getMessage());
                    //Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
       // Toast.makeText(this, "המפה מוכנה,אתה רשאי להתחיל את המסלול", Toast.LENGTH_SHORT).show();
        resetPolylineTrack();


        if (savedRoute != null) {

            List<LatLng> latLngs = new ArrayList<>();
            Log.i("SAVED ROUTES", savedRoute.getLocationHelpers().size() + "");

            for (int i = 0; i < savedRoute.getLocationHelpers().size(); i++) {

                latLngs.add(new LatLng(savedRoute.getLocationHelpers().get(i).getLatitude(), savedRoute.getLocationHelpers().get(i).getLongitude()));

            }

            gpsTrack.setPoints(latLngs);


            if (savedRoute.getLocationHelpers().size() > 0) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(savedRoute.getLocationHelpers().get(0).getLatitude(), savedRoute.getLocationHelpers().get(0).getLongitude()), 18.5f));
            }

//            resetPolylineTrackShared();

        }

        setLocationListenerForNewRoute();

        progressBar.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            //Toast.makeText(this, "PERMISSION DID NOT GRANTED AND DOES NOT SHOWING", Toast.LENGTH_SHORT).show();

            return;
        } else //Toast.makeText(this, "PERMISSION GRANTED ALREADY", Toast.LENGTH_SHORT).show();


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lastLocation = location;
                            setLocationListenerForNewRoute();
                        }
                    }
                });

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (Exception e) {
            Log.i("ExeptionManager", e.getMessage());
            e.printStackTrace();
        }
    }


    private void openDialogGps() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(MapsActivity.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS", "Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.e("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("GPS", "checkLocationSettings -> onCanceled");
                    }
                });

    }


    private void openGpsEnableSetting() {
        Log.i("CALLED TO PEN GPS", "GPS");
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        startActivityForResult(intent, REQUEST_ENABLE_GPS);
        someActivityResultLauncher.launch(intent);
    }


    private void resetPolylineTrack() {

        Log.v("RESETPOLYLINE", "POLY LINE HERE");


        if (gpsTrack != null) {
            gpsTrack.remove();
        }

        PolylineOptions polylineOptions = new PolylineOptions();


        polylineOptions.color(Color.RED);

        clickedOnce = true;


        polylineOptions.width(8);

        gpsTrack = mMap.addPolyline(polylineOptions);
    }


    private void resetPolylineTrackShared() {

        Log.v("RESETPOLYSHARED", "POLY LINE HERE IS SHARED");

        PolylineOptions polylineOptions = new PolylineOptions();


        polylineOptions.color(Color.BLUE);


        polylineOptions.width(10);
        gpsTrack = mMap.addPolyline(polylineOptions);
    }


    //get last location by phone (current location of user)
    private Location getLastLocation() {

        Location location = null;
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);


        } else location = locationManager.getLastKnownLocation(provider);

        return location;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///WORKING ON ROUTE DETAILS///


    //startBtn
    public void startChronometer(View v) {

        if (!running) {
            currentTime = Calendar.getInstance().getTime();
            route = new Route();

            if (locationListener == null) {
                setLocationListenerForNewRoute();
            }

            isSharedSaved = false;

            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 18.0f));
                resetPolylineTrackShared();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm a");
            Date date = new Date();
            startRidingHour = dateFormat.format(date);

            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            startBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
            stopBtn.setVisibility(View.VISIBLE);


        }
    }


    //pauseBtn
    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            startBtn.setText("המשך");
            startBtn.setVisibility(View.VISIBLE);
            pauseBtn.setVisibility(View.GONE);
            stopBtn.setVisibility(View.VISIBLE);
            running = false;
        }
    }


    //stopBtn

    public void resetChronometer(View v) {

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 15.0f));

        timeRideToBeSave = chronometer.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                if (isStoragePermissionGranted()) {
                    snapShot();
                }

            }
        });
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //show message to save route
    private void showSharedMessage(final Route route, final GoogleMap googleMap) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);

        builder.setCancelable(true);
        builder.setTitle("האם תרצה לשמור מסלול זה?");
        final EditText input = new EditText(MapsActivity.this);
        input.setHint("הכנס שם למסלול");
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 if (input.getText().toString().isEmpty()) {
                    route.setRouteName("ללא שם");
                }
                else if (!input.getText().toString().isEmpty()) {
                    route.setRouteName(input.getText().toString());
                }

                FirebaseDatabase.getInstance().getReference("share routes").child(mAuth.getCurrentUser().getUid()).push().setValue(route);
                Toast.makeText(MapsActivity.this, "המסלול שותף בהצלחה!", Toast.LENGTH_SHORT).show();

            }
        });

        builder.show();
    }


    private void updateTrack() {

        List<LatLng> points = gpsTrack.getPoints();
        points.add(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
        Log.i("UPDATE TRACK", points.size() + "");

        for (int i = 0; i < points.size(); i++) {

            route.getLocationHelpers().add(new LocationHelper(points.get(i).longitude, points.get(i).latitude));

        }


        gpsTrack.setPoints(points);

    }


    private void updateTrackShared() {
        List<LatLng> points = gpsTrack.getPoints();
        points.add(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));
        Log.i("UPDATE TRACK SHARED", points.size() + "");

        for (int i = 0; i < points.size(); i++) {

            route.getLocationHelpers().add(new LocationHelper(points.get(i).longitude, points.get(i).latitude));

        }

        gpsTrack.setPoints(points);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (locationManager != null)
//            locationManager.removeUpdates(locationListener);
//

        SharedPreferences preferences = getSharedPreferences("file", 0);
        preferences.edit().remove("myRoute").commit();

    }


    //capturing image map and initialize it
    public void snapShot() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {

                bitmap = snapshot;

                try {

                    captureImageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "maps");

                    imageFile = new File(captureImageFile, System.currentTimeMillis() + ".jpeg");

                    if (!captureImageFile.exists())
                        captureImageFile.mkdirs();

                    if (isStoragePermissionGranted()) {

                        FileOutputStream fout = new FileOutputStream(imageFile);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                        fout.flush();
                        fout.close();
                        Toast.makeText(MapsActivity.this, "המסלול נשמר בהצלחה", Toast.LENGTH_SHORT).show();


                        saveRouteDetails();
                        updateUIMap();

                        mMap.clear();

                        resetPolylineTrack();

                    }

                } catch (Exception e) {
                    Log.i("EXEPTION IMAGE", e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "אירעה שגיאה,המסלול לא נשמר", Toast.LENGTH_SHORT).show();
                }


            }
        };
        mMap.snapshot(callback);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request code
                        Intent data = result.getData();

                        if (mMap != null)
                            setLocationListenerForNewRoute();
                    }
                }
            });


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e("GPS", "User denied to access location");
                    openGpsEnableSetting();
                    break;
            }
        } else if (requestCode == REQUEST_ENABLE_GPS) {
            //Toast.makeText(this, "REQUEST ENABLE GPS", Toast.LENGTH_SHORT).show();
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGpsEnabled) {
                openGpsEnableSetting();
            } else {
                if (mMap == null) {
                   // Toast.makeText(this, "mMAP NULL", Toast.LENGTH_SHORT).show();
                    ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                            .getMapAsync(this);
                }
                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MapsActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);

                   // Toast.makeText(this, "PERMISSION DID NOT GRANTED AND DOES NOT SHOWING", Toast.LENGTH_SHORT).show();

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                setLocationListenerForNewRoute();
            }
        }
    }


    //API 23 and above need to check permission at run time
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    //request permission for saving EXTERNAL FILES
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);


            snapShot();
            //resume tasks needing this permission

        }

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (mMap == null) {
               // Toast.makeText(this, "mMAP NULL", Toast.LENGTH_SHORT).show();
                ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                        .getMapAsync(this);
            }
            if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

               // Toast.makeText(this, "PERMISSION DID NOT GRANTED AND DOES NOT SHOWING", Toast.LENGTH_SHORT).show();

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            setLocationListenerForNewRoute();


        }
    }


    private void openScreenshot(File imageFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }


    private void saveRouteDetails() {


        //save file in storage database under user id
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("maps").child(mAuth.getCurrentUser().getUid()).child("picNum" + userHelperClass.getRoutes().size() + ".jpeg");
        UploadTask uploadTask = storageReference.putFile(Uri.fromFile(imageFile));

        //save the image uri in user helper class
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //saving new ROUTE in user helper class and restart map fragment

                        if (route == null) {
                            Log.i("ROUTE", "NULL IN SAVE ROUTE DETAILS");
                            route = new Route();
                        }


                        currentTime = Calendar.getInstance().getTime();
                        Toast.makeText(getApplicationContext(), timeRideToBeSave, Toast.LENGTH_SHORT).show();

                        route.setTime(timeRideToBeSave);
                        route.setAvgSpeed(averageSpeedTextView.getText().toString());
                        route.setDistance(distanceTextView.getText().toString());
                        route.setHourOfRoute(startRidingHour);
                        route.setDateOfRoute(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime()));
                        route.setImageMapUri(uri.toString());

                        userHelperClass.getRoutes().add(route);

                        Map map = new HashMap<>();
                        map.put("routes", userHelperClass.getRoutes());

                        FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                                progressBar.setVisibility(View.GONE);
                                showSharedMessage(route, mMap);
                                route = new Route();

                            }
                        });


                    }
                });

            }
        });

    }

    private void updateUIMap() {

        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        chronometer.stop();
        running = false;
        startBtn.setText("התחל");
        startBtn.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.GONE);
        stopBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        if (savedRoute != null) {
            getSharedPreferences("file", 0).edit().remove("myRoute").commit();
            savedRoute = null;
        }

        distance = 0;
        averageSpeed = 0;

    }


    //SET LISTENER FOR LOCATION OF USER AND TRACK< PAINT< AND MOVE CAMERA BY USER LOCATION
    private void setLocationListenerForNewRoute() {

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                    if (lastLocation != null && running) {
                        distance += location.distanceTo(lastLocation);//in meters


                        if (distance == 0 || SystemClock.elapsedRealtime() - chronometer.getBase() == 0) {
                            averageSpeedTextView.setText("קמ״ש 0");
                        } else {
                            Log.i("ZERO DIVIDED", distance + " OR " + chronometer.getBase() + " OR " + SystemClock.elapsedRealtime());
                            averageSpeed = (distance / ((SystemClock.elapsedRealtime() - chronometer.getBase() - pauseOffset) / 1000));
                            averageSpeedTextView.setText("קמ״ש " + new DecimalFormat("#.#").format(averageSpeed * 3.6f));
                        }


//                    if (savedRoute == null) {
//                        updateTrack();
//                    }else updateTrackShared();

                        updateTrack();

                        distanceTextView.setText("מ׳ " + distance);

                        if (distance > 1000) {

                            distanceTextView.setText("קמ׳ " + ((float) distance / 1000));


                        }

                    }


                    if (!isSharedSaved)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18.0f));


                    lastLocation = new Location(location);


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }


}


