package com.example.cycleurbike.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.cycleurbike.R;
import com.example.cycleurbike.fragments.LogInScreen;
import com.example.cycleurbike.fragments.RegisterScreen;
import com.example.cycleurbike.fragments.MainAppPage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_layout) != null) { // לא הצליח לטעון את הפקד אם ריק

            if (savedInstanceState != null) { // savedInstanceState - האם קיים מצב בפריימנט הראשון
                return;
            }

            LogInScreen firstFragment = new LogInScreen();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_layout, firstFragment).commit();

        }

    }
    public void loadFirstFragment() {
        LogInScreen firstFragment = new LogInScreen();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_layout, firstFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void loadSecFragment() {

        RegisterScreen secondFragment = new RegisterScreen();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_layout, secondFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void loadMainPage() {

        MainAppPage mainPageFragment = new MainAppPage();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_layout, mainPageFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }


}