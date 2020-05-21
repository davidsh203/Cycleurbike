package com.example.cycleurbike.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.cycleurbike.R;
import com.example.cycleurbike.fragments.Fragment1;
import com.example.cycleurbike.fragments.Fragment2;
import com.example.cycleurbike.fragments.Main_Instruction_Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_layout) != null) { // לא הצליח לטעון את הפקד אם ריק

            if (savedInstanceState != null) { // savedInstanceState - האם קיים מצב בפריימנט הראשון
                return;
            }

            Fragment1 firstFragment = new Fragment1();

            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_layout, firstFragment).commit();

        }

    }
    public void loadFirstFragment() {
        Fragment1 firstFragment = new Fragment1();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_layout, firstFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void loadSecFragment() {

        Fragment2 secondFragment = new Fragment2();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_layout, secondFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void loadMainPage() {

        Main_Instruction_Fragment mainPageFragment = new Main_Instruction_Fragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_layout, mainPageFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }



}