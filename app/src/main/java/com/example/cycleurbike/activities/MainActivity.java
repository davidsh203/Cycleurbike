package com.example.cycleurbike.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.cycleurbike.R;
import com.example.cycleurbike.fragments.LogInScreen;
import com.example.cycleurbike.fragments.RegisterScreen;
import com.example.cycleurbike.fragments.MainAppPage;
import com.example.cycleurbike.fragments.ResetPasswordScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_layout) != null) { // לא הצליח לטעון את הפקד אם ריק

            if (savedInstanceState != null) { // savedInstanceState - האם קיים מצב בפרייגמנט הראשון
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


    public void popUpMessage(final String msg) { //פונקציה שמקפיצה הודעת שגיאה אם המשתמש אינו מחובר לאינטרנט
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);

        // set title
        alertDialogBuilder.setTitle(msg);

        // set dialog message
        alertDialogBuilder
                .setMessage("")
                .setCancelable(false)
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {  //כפתור לאישור ההודעה
                        // if this button is clicked, close
                        // current activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!internet_connection())popUpMessage(msg);
                            }
                        },7000);
                    }
                })
                .setNegativeButton("צא מהאפליקציה", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { //כפתור לביטול ההודעה ויציאה מן ההודעה ללא פעולה
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        //dialog.cancel();
                        finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    boolean internet_connection() { //פונקציה שבודקת האם יש חיבור לאינטרנט
        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public void loadLogInScreen() { //פונקציה שטוענת את הכניסה לאפליקציה
        LogInScreen logInScreen = new LogInScreen();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, logInScreen);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    public void loadRegisterScreen() {  //פונקציה שטוענת את מסך ההרשמה
        RegisterScreen registerScreen = new RegisterScreen();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, registerScreen);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void loadMainAppPage() { //פונקציה שטוענת את המסך הראשי
        MainAppPage mainAppPage = new MainAppPage();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, mainAppPage);
        // transaction.addToBackStack(null);  //ברגע שהמשתמש לוחץ על מקש חזור בפלאפון זה מחזיר אותו למסך הקודם שהוא היה,במקרה הזה לא צריך כי ברגע שהוא התחבר אין צורך לחזור למסך של ההתחברות אלא בלחיצת כפתור התנתק
        transaction.commit();
    }

    public void loadResetPasswordScreen(){ //פונקציה שטוענת את מסך איפוס הסיסמה
        ResetPasswordScreen resetPasswordScreen = new ResetPasswordScreen();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, resetPasswordScreen);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}