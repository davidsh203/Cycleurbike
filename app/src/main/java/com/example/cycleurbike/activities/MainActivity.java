package com.example.cycleurbike.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
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
            LogInScreen logInScreen = new LogInScreen(); //יצירת פרייגמנט חדש למסך הכניסה לאפליקציה
            logInScreen.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_layout, logInScreen).commit();
        }
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

    public void loadResetPasswordScreen(){
        ResetPasswordScreen resetPasswordScreen = new ResetPasswordScreen();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layout, resetPasswordScreen);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}