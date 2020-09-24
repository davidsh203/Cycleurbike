package com.example.cycleurbike.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterScreen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    EditText regFirstName, regLastName,regEmail,regPassword,regBirthDay,regCity;
    Button regFinishButton;
    String id;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterScreen newInstance(String param1, String param2) {
        RegisterScreen fragment = new RegisterScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }
    /*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/

    public void CreateUser(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register_screen, container, false);
        mAuth = FirebaseAuth.getInstance();


        regFirstName = view.findViewById(R.id.firstNameEditTextRegisterScreen);
        regLastName =   view.findViewById(R.id.lastNameEditTextRegisterScreen);
        regEmail =   view.findViewById(R.id.emailEditTextRegisterScreen);
        regPassword =   view.findViewById(R.id.passwordEditTextRegisterScreen);
        regBirthDay =   view.findViewById(R.id.birthDateEditTextRegisterScreen);
        regCity =   view.findViewById(R.id.cityEditTextRegisterScreen);
        regFinishButton = view.findViewById(R.id.finishButtonRegisterScreen);
        final Animation myAnim4 = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        Button button4 = (Button) view.findViewById(R.id.finishButtonRegisterScreen);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim4);
            }
        });


        if(mAuth.getCurrentUser() != null){  //התנאי בודק אם יש משתמש קיים במערכת, באם קיים מכניס אותו למסך הראשי

            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.loadMainAppPage(); //

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
                UserHelperClass helperClass = new UserHelperClass(firstName,lastName,email,password,birthDay,city);
                helperClass.setId(reference.push().getKey());
                id=helperClass.getId1();
                reference.child(helperClass.getId1()).child("First name").setValue(helperClass.getFirstName());
                reference.child(helperClass.getId1()).child("lLast name").setValue(helperClass.getLastName());
                reference.child(helperClass.getId1()).child("Email").setValue(helperClass.getEmail());
                reference.child(helperClass.getId1()).child("Password").setValue(helperClass.getPassword());
                reference.child(helperClass.getId1()).child("BirthDay").setValue(helperClass.getBirthDay());
                reference.child(helperClass.getId1()).child("City").setValue(helperClass.getCity());
                reference.child(helperClass.getId1()).removeValue();

                if (TextUtils.isEmpty(email)) {
                    regEmail.setError("נא הכנס אימייל");
                    regEmail.requestFocus();
                }

                else if (TextUtils.isEmpty(password)) {
                    regPassword.setError("נא הכנס סיסמה");
                }

                else if (password.length() < 6) {
                    regPassword.setError("הסיסמה חייבת להכיל לפחות 6 תווים");
                }
                else if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(getActivity(),"אנא מלא את שדות האימייל והסיסמה",Toast.LENGTH_LONG).show();
                }
               else if(!(email.isEmpty() && password.isEmpty())){
                   mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           Toast.makeText(getActivity(), "המשתמש נוצר", Toast.LENGTH_LONG).show();
                           // startActivity(new Intent(getContext(), MainAppPage.class));

                           MainActivity mainActivity = (MainActivity) getActivity();
                           mainActivity.loadMainAppPage(); //

                       } else {
                           Toast.makeText(getActivity(), "התרחשה שגיאה,יכול להיות שהאימייל קיים כבר במערכת אנא נסה אימייל אחר" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                          // reference.child(helperClass.getId1()).removeValue();
                           reference = rootNode.getReference("users");
                           reference.child(id).removeValue();
                           //MainActivity mainActivity = (MainActivity) getActivity();
                           //mainActivity.loadRegisterScreen(); //
                       }
                   }

                });
            }}
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