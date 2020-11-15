package com.example.cycleurbike.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cycleurbike.R;
import com.example.cycleurbike.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterScreen extends Fragment {


    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    EditText regFirstName, regLastName, regEmail, regPassword, regBirthDay, regCity;
    Button regFinishButton;
    String id;

    /*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/

    //פונקציה שבודקת אם המבנה של המייל שהוקלד תקין
    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register_screen, container, false);
        mAuth = FirebaseAuth.getInstance();


        regFirstName = view.findViewById(R.id.firstNameEditTextRegisterScreen);
        regLastName = view.findViewById(R.id.lastNameEditTextRegisterScreen);
        regEmail = view.findViewById(R.id.emailEditTextRegisterScreen);
        regPassword = view.findViewById(R.id.passwordEditTextRegisterScreen);
        regBirthDay = view.findViewById(R.id.birthDateEditTextRegisterScreen);
        regCity = view.findViewById(R.id.cityEditTextRegisterScreen);
        regFinishButton = view.findViewById(R.id.finishButtonRegisterScreen);
        final Animation myAnim4 = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        Button button4 = (Button) view.findViewById(R.id.finishButtonRegisterScreen);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim4);
            }
        });


        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {  //התנאי בודק אם יש משתמש קיים במערכת, באם קיים מכניס אותו למסך הראשי

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.loadMainAppScreen(); //

            // startActivity(new Intent(getActivity().getApplicationContext(),LogInScreen.class));
            //  getActivity().finish();
        }
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // inner class
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                //Get all the values

                String firstName = regFirstName.getText().toString();
                String lastName = regLastName.getText().toString();
                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();
                String birthDay = regBirthDay.getText().toString();
                String city = regCity.getText().toString();

                if (TextUtils.isEmpty(firstName)) {
                    regFirstName.setError("נא הכנס שם פרטי");
                    regFirstName.requestFocus();
                } else if (TextUtils.isEmpty(lastName)) {
                    regLastName.setError("נא הכנס שם משפחה");
                    regLastName.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    regEmail.setError("נא הכנס אימייל");
                    regEmail.requestFocus();
                } else if (!isEmailValid(email)) {
                    regEmail.setError("נא הכנס איימייל תקין");
                    regEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    regPassword.setError("נא הכנס סיסמה");
                    regPassword.requestFocus();
                } else if (TextUtils.isEmpty(city)) {
                    regCity.setError("נא הכנס עיר");
                    regCity.requestFocus();
                } else if (password.length() < 6) {
                    regPassword.setError("הסיסמה חייבת להכיל לפחות 6 תווים");
                    regPassword.requestFocus();
                } else if (firstName.length() > 25) {
                    regFirstName.setError("אנא הכנס שם פרטי תקין");
                    regFirstName.requestFocus();
                } else if (lastName.length() > 25) {
                    regLastName.setError("אנא הכנס שם משפחה תקין");
                    regLastName.requestFocus();
                } else if (city.length() > 25) {
                    regCity.setError("אנא הכנס עיר תקינה");
                    regCity.requestFocus();
                } else {
                    final UserHelperClass helperClass = new UserHelperClass(firstName, lastName, email, birthDay, city);
                    //helperClass.setId(reference.push().getKey());
                    helperClass.setId(firstName + " " + lastName + " Id: " + reference.push().getKey());

//                    id=helperClass.getId1();
//                    reference.child(helperClass.getId1()).child("First name").setValue(helperClass.getFirstName());
//                    reference.child(helperClass.getId1()).child("Last name").setValue(helperClass.getLastName());
//                    reference.child(helperClass.getId1()).child("Email").setValue(helperClass.getEmail());
//                    //reference.child(helperClass.getId1()).child("Password").setValue(helperClass.getPassword());
//                    reference.child(helperClass.getId1()).child("BirthDay").setValue(helperClass.getBirthDay());
//                    reference.child(helperClass.getId1()).child("City").setValue(helperClass.getCity());


                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            final FirebaseUser user = mAuth.getCurrentUser();

                            if (task.isSuccessful()) {

                                Log.i("CREATE USER", "SUCCESS");

                                reference.child(user.getUid()).setValue(helperClass, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {


                                        if (error != null) {

                                            Log.i("ERROR DATABASE", error.getMessage());


                                        } else {
                                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getActivity(), "אימות אימייל נשלח כעת נא להיכנס למייל להמשך הרשמה", Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "אימות מייל נכשל,נא נסה שנית", Toast.LENGTH_LONG).show();
                                                }
                                            });
//                                            MainActivity mainActivity = (MainActivity) getActivity();
//                                            mainActivity.loadMainAppScreen();

                                        }

                                    }
                                });

//                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
//                               @Override
//                               public void onComplete(@NonNull Task<Void> task) {
//                                   Toast.makeText(getActivity(), "המשתמש נוצר בהצלחה", Toast.LENGTH_LONG).show();
//
//                                   Intent intent = new Intent(getContext(), MainAppScreen.class);
//                                   intent.putExtra("user", helperClass);
//                                   startActivity(intent);
//                               }
//                           });

//                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(getActivity(),"אימות אימייל נשלח כעת נא להיכנס למייל להמשך הרשמה",Toast.LENGTH_LONG).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getActivity(),"אימות מייל נכשל,נא נסה שנית",Toast.LENGTH_LONG).show();
//                                }
//                            });

                                MainActivity mainActivity = (MainActivity) getActivity();
                                mainActivity.loadLogInScreen();
                            } else {
                                Toast.makeText(getActivity(), "התרחשה שגיאה,יכול להיות שהאימייל קיים כבר במערכת אנא נסה אימייל אחר" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                // reference.child(helperClass.getId1()).removeValue();
                                //reference = rootNode.getReference("users");
                                //reference.child(id).removeValue();
                                //MainActivity mainActivity = (MainActivity) getActivity();
                                //mainActivity.loadRegisterScreen();
                            }
                        }

                    });
                }
            }
        });
        return view;
    }

/*
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            Toast.makeText(getActivity(),"אתה מחובר למערכת",Toast.LENGTH_LONG).show();
           // startActivity(new Intent(getActivity(),MainAppPage.class));

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.loadMainAppPage(); //
        }else {
            Toast.makeText(getActivity(),"מלא את פרטיך על מנת להירשם",Toast.LENGTH_LONG).show();
        }
    }*/
}