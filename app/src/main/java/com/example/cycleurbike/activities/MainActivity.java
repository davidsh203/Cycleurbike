package com.example.cycleurbike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;

import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.example.cycleurbike.R;
import com.example.cycleurbike.fragments.LogInScreen;
import com.example.cycleurbike.fragments.RegisterScreen;
import com.example.cycleurbike.fragments.MainAppScreen;
import com.example.cycleurbike.fragments.ResetPasswordScreen;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "isStoragePermission";
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            //Toast.makeText(this, "PERMISSION DID NOT GRANTED AND DOES NOT SHOWING", Toast.LENGTH_SHORT).show();
        }else //Toast.makeText(this, "PERMISSION GRANTED ALREADY", Toast.LENGTH_SHORT).show();

        isStoragePermissionGranted();
        openDialogGps();

        SharedPreferences preferences = getSharedPreferences("file", 0);
        preferences.edit().remove("myRoute").commit();

        if (findViewById(R.id.fragment_layout) != null) {

            // savedInstanceState - האם קיים מצב בפרייגמנט הראשון
            if (savedInstanceState != null) {
                return;
            }
            if(!internet_connection()){
                popUpMessage("על מנת להשתמש באפליקציה הנך נדרש/ת להתחבר לאינטרנט");
            }

            LogInScreen logInScreen = new LogInScreen(); //יצירת פרייגמנט חדש למסך הכניסה לאפליקציה
            logInScreen.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_layout, logInScreen).commit();

        }
    }

    //פונקציה שמקפיצה הודעת שגיאה אם המשתמש אינו מחובר לאינטרנט
    public void popUpMessage(final String msg) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);

        // תוכן ההודעה
        alertDialogBuilder.setTitle(msg);

        // קביעת כפתורי אישור וביטול
        alertDialogBuilder
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {  //כפתור לאישור ההודעה
                    public void onClick(DialogInterface dialog, int id) {
                      //לאחר שכפתור האישור נלחץ בודק כל 7 שניות האם האינטרנט מחובר ואם לא מקפיץ שוב את ההודעה
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!internet_connection())popUpMessage(msg);
                            }
                        },7000);
                    }
                })
                .setNegativeButton("צא מהאפליקציה", new DialogInterface.OnClickListener() { //כפתור לביטול ההודעה
                    public void onClick(DialogInterface dialog, int id) {
                       //ברגע שכפתור זה נלחץ האפליקציה תסגר
                        finish();
                    }
                });

        // יוצר את דיאלוג ההודעה
        AlertDialog alertDialog = alertDialogBuilder.create();

        // מפעיל את ההודעה
        alertDialog.show();
    }

    //פונקציה שבודקת האם יש חיבור לאינטרנט
    public boolean internet_connection() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    //פונקציה שטוענת את הכניסה לאפליקציה
    public void loadLogInScreen() {
        LogInScreen logInScreen = new LogInScreen();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, logInScreen);
        transaction.commit();
    }

    //פונקציה שטוענת את מסך ההרשמה
    public void loadRegisterScreen() {
        RegisterScreen registerScreen = new RegisterScreen();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, registerScreen);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //פונקציה שטוענת את המסך הראשי
    public void loadMainAppScreen() {
        MainAppScreen mainAppScreen = new MainAppScreen();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, mainAppScreen);
        transaction.commit();
    }

    //פונקציה שטוענת את מסך איפוס הסיסמה
    public void loadResetPasswordScreen(){
        ResetPasswordScreen resetPasswordScreen = new ResetPasswordScreen();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, resetPasswordScreen);
        transaction.addToBackStack(null);
        transaction.commit();
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


    private void openDialogGps() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(MainActivity.this);

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
                                    rae.startResolutionForResult(MainActivity.this, 2);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS","Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.e("GPS","Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("GPS","checkLocationSettings -> onCanceled");
                    }
                });

    }
}
