package com.example.cycleurbike.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.cycleurbike.R;
import com.example.cycleurbike.activities.MainActivity;
import com.example.cycleurbike.activities.MapsActivity;
import com.example.cycleurbike.activities.RiderProfileActivity;
import com.example.cycleurbike.activities.SharedRoutesActivity;
import com.example.cycleurbike.activities.WeatherScreen;
import com.example.cycleurbike.activities.YouTube;
import com.google.firebase.auth.FirebaseAuth;

public class MainAppScreen extends Fragment {


    private FirebaseAuth mAuth;


    public MainAppScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_app_screen, container, false);



        final Button weatherButton = view.findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherScreen.class);
                startActivity(intent);
            }
        });

        final Button rideLearnButton =  view.findViewById(R.id.rideLearnButtonFragMain);
        rideLearnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YouTube.class);
                startActivity(intent);
            }
        });

       final Button beginNewRideButton = (Button) view.findViewById(R.id.beginNewRideButtonFragMain);
        beginNewRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        final Button signOutButton = view.findViewById(R.id.signOutButtonMainAppScreen);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getInstance().signOut();
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadLogInScreen();
                Toast.makeText(getActivity(),"התנתקת בהצלחה",Toast.LENGTH_LONG).show();

            }
        });

        final Button ProfileBtn = view.findViewById(R.id.newProfileButtonFragMain);
        ProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RiderProfileActivity.class));

            }
        });


        final Button sharedRoutesBtn = view.findViewById(R.id.searchRideButtonFragMain);
        sharedRoutesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SharedRoutesActivity.class));
            }
        });

        final Button contactEmail = view.findViewById(R.id.contactEmailButtonMainAppScreen);

        contactEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"cycleurbike@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_TEXT, "");
                try {
                    startActivity(Intent.createChooser(i, "שליחת מייל מתבצעת כעת"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "לצערנו אין לך אפליקציה מייל.", Toast.LENGTH_SHORT).show();

                }
            }});

        final Button sharedAppButton = view.findViewById(R.id.sharedAppButtonMainAppScreen);
        sharedAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); // message type is plain text
                i.putExtra(Intent.EXTRA_TEXT, "היי, אני רוצה להמליץ על אפליקציה חדשה שהתקנתי: www.googleDrive.com");
                startActivity(i);

            }
        });
        return view;
    }
}