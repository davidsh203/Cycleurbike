package com.example.cycleurbike.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cycleurbike.R;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cycleurbike.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInScreen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    EditText logEmail, logPassword;
    Boolean userLog = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInScreen newInstance(String param1, String param2) {
        LogInScreen fragment = new LogInScreen();
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


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if (mAuthStateListener != null) {
        mAuth.addAuthStateListener(mAuthStateListener);}
    }

/*
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
*/



//פונקציה שבודקת אם המבנה של המייל שהוקלד תקין
    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.log_in_screen, container, false);
        logEmail = view.findViewById(R.id.enterMailTextMainScreen);
        logPassword = view.findViewById(R.id.enterPasswordTextMainScreen);
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mfiFirebaseUser = mAuth.getCurrentUser();
                if (mfiFirebaseUser != null) {
                    if (!mfiFirebaseUser.isEmailVerified()) {
                        //MainActivity mainActivity = (MainActivity) getActivity();
                        // mainActivity.loadLogInScreen();
                        //Toast.makeText(getActivity(),"אמת מייל",Toast.LENGTH_LONG).show();
                    } else if (mfiFirebaseUser.isEmailVerified()) {
                        //Toast.makeText(getActivity(), "התחברת בהצלחה!", Toast.LENGTH_LONG).show();
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.loadMainAppPage();
                    }
                } else {
                    //Toast.makeText(getActivity(),"שם משתמש או סיסמה אינם נכונים אנא נסה שנית",Toast.LENGTH_LONG).show();
                }
            }
        };


        final Animation rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);

        final Button enterButtonFrag1 = (Button) view.findViewById(R.id.enterButtonMainScreen);
        /*enterButtonFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // inner class
              startButtonAnimation(enterButtonFrag1,rotateAnim);

            }
        });*/
        enterButtonFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(rotateAnim);
                mAuth.removeAuthStateListener(mAuthStateListener);

                String email = logEmail.getText().toString();
                String password = logPassword.getText().toString();
                if(!isEmailValid(email)){ logEmail.setError("אנא הכנס מייל תקין");}

                    if (email.isEmpty()) {
                    logEmail.setError("אנא הכנס מייל");
                } else if (password.isEmpty()) {
                    logPassword.setError("אנא הכנס סיסמה");
                } else if (password.length() < 6) {
                    logPassword.setError("הסיסמה חייבת להכיל לפחות 6 תווים");
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(getActivity(), "אנא הכנס מייל וסיסמה", Toast.LENGTH_LONG).show();
                } else if (!(email.isEmpty() && password.isEmpty())) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (task.isSuccessful()) {

                                if (!user.isEmailVerified()) {
                                    Toast.makeText(getActivity(), "אמת מייל", Toast.LENGTH_LONG).show();
                                } else if (user.isEmailVerified()) {
                                    Toast.makeText(getActivity(), "התחברת בהצלחה", Toast.LENGTH_LONG).show();
                                    MainActivity mainActivity = (MainActivity) getActivity();
                                    mainActivity.loadMainAppPage();
                                }

                            }
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                }
                                // if user enters wrong email.
                                catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    Toast.makeText(getActivity(), "המייל אינו קיים במערכת אנא הירשם בכפתור מטה", Toast.LENGTH_LONG).show();
                                }
                                // if user enters wrong password.
                                catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                    String email = logEmail.getText().toString();
                                    if(isEmailValid(email)) //בודק אם בכלל המייל שהוזן בעל מבנה תקין
                                    {Toast.makeText(getActivity(), "הסיסמה שהזנת שגויה אנא נסה שנית", Toast.LENGTH_LONG).show();}
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "התרחשה שגיאה אנא נסה שנית", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });


        final Animation myAnim2 = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
        Button registerButtonFrag1 = (Button) view.findViewById(R.id.registerButtonMainScreen);
        registerButtonFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.startAnimation(myAnim2);

                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadRegisterScreen();
            }
        });

        // אנימציה שכחתי סיסמה
        final Animation myAnim3 = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        final Button forgetPasswordButtonFrag1 = (Button) view.findViewById(R.id.forgetPasswordButtonMainScreen);
        forgetPasswordButtonFrag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(myAnim3);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadResetPasswordScreen();
            }
        });

        return view;
    }

    // כפתור כניסה על מנת שהאנימציה תתבצע וייראו אותה
    private void startButtonAnimation(Button btn, Animation anim) {
        btn.setAnimation(anim);
        btn.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // TODO למצוא דרך להפעיל את הפונקציה על ידי העברת הפרמטר שנלחץ לפונ' בכדי לקצר בקוד
            @Override
            public void onAnimationEnd(Animation animation) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadMainAppPage();
            }
        });
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


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null && currentUser.isEmailVerified()) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.loadMainAppPage(); //
        } else {
            Toast.makeText(getActivity(), "מלא את פרטיך על מנת להירשם", Toast.LENGTH_LONG).show();
        }
    }
}




