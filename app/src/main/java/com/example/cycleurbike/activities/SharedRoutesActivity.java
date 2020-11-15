package com.example.cycleurbike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.cycleurbike.ClassHelpers.Adapters.RoutesProfileAdapter;
import com.example.cycleurbike.ClassHelpers.Adapters.SharedRoutesAdapter;
import com.example.cycleurbike.ClassHelpers.Route;
import com.example.cycleurbike.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SharedRoutesActivity extends AppCompatActivity {

    RecyclerView sharedRecyclerView;
    RecyclerView.Adapter shareRoutesAdapter;
    FirebaseAuth mAuth;

    ArrayList<Route> sharedRoutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_routes);

        mAuth = FirebaseAuth.getInstance();

        sharedRecyclerView = findViewById(R.id.shared_routes_recycler_view_shared_routes_activity);

        FirebaseDatabase.getInstance().getReference("share routes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    sharedRoutes = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        Log.i("DATA", snapshot.getKey());

                        if (dataSnapshot.exists()) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                sharedRoutes.add(dataSnapshot1.getValue(Route.class));
                                Log.i("SHARED ROUTE", sharedRoutes.get(sharedRoutes.size() - 1).toString());
                                Log.i("DATA SNAP", snapshot.getKey());
                            }
                        }



                    }

                    Log.i("SIZE", sharedRoutes.size() + "");

                    initializeSharedRoutesRecyclerView();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    private void initializeSharedRoutesRecyclerView() {

        sharedRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        sharedRecyclerView.setLayoutManager(layoutManager);
        shareRoutesAdapter = new SharedRoutesAdapter(getApplicationContext(), sharedRoutes, this, getPreferences(MODE_PRIVATE));
        sharedRecyclerView.setAdapter(shareRoutesAdapter);

    }
}
