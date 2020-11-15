package com.example.cycleurbike.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cycleurbike.ClassHelpers.Adapters.RoutesProfileAdapter;
import com.example.cycleurbike.R;
import com.example.cycleurbike.fragments.UserHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderProfileActivity extends AppCompatActivity {


    TextView riderNameTextView, emailTextView, birthdayTextView, cityTextView;
    RecyclerView routsRecyclerView;
    RecyclerView.Adapter routsAdapter;

    UserHelperClass userHelperClass;

    FirebaseAuth mAuth;

    public RiderProfileActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_profile);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        riderNameTextView = findViewById(R.id.profile_rider_name_text_view_profile_activity);
        emailTextView = findViewById(R.id.rider_email_address_profile_activity);
        birthdayTextView = findViewById(R.id.birth_of_dat_rider_profile_activity);
        cityTextView = findViewById(R.id.city_of_rider_profile_acitivty);

        routsRecyclerView = findViewById(R.id.routs_recycler_view_profile_activity);


        FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userHelperClass = snapshot.getValue(UserHelperClass.class);
                initializeRoutesRecyclerView();
                FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).removeEventListener(this);

                riderNameTextView.setText("שם הרוכב: " + userHelperClass.getFirstName() + " " + userHelperClass.getLastName());
                emailTextView.setText("אימייל: " + userHelperClass.getEmail());
                birthdayTextView.setText("תאריך לידה: " + userHelperClass.getBirthDay());
                cityTextView.setText("עיר מגורים: " + userHelperClass.getCity());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void initializeRoutesRecyclerView() {

        routsRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        routsRecyclerView.setLayoutManager(layoutManager);
        routsAdapter = new RoutesProfileAdapter(getApplicationContext(), userHelperClass.getRoutes());
        routsRecyclerView.setAdapter(routsAdapter);

    }
}
