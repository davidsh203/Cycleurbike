package com.example.cycleurbike.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.cycleurbike.activities.MapsActivity;
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
    private EditText logEmail, logPassword;
    private  Animation shakeAnim;
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

    //פונקציה ששולחת ערך לפונקציה אחרת הבודקת האם המשתמש מחובר או לא
    @Override
    public void onStart() {
        super.onStart();
        if (mAuthStateListener != null) {
        mAuth.addAuthStateListener(mAuthStateListener);}
    }


    //פונקציה שבודקת אם המבנה של המייל שהוקלד תקין
    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.log_in_screen, container, false);
        logEmail = view.findViewById(R.id.enterMailTextLoginScreen);
        logPassword = view.findViewById(R.id.enterPasswordTextLoginScreen);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()){
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.loadMainAppScreen();
        }
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {  //פונקציה שבודקת האם המשתמש מחובר למערכת ואם כן מעבירה אותו למסך הראשי
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser mfiFirebaseUser = mAuth.getCurrentUser();
////                 && mfiFirebaseUser.isEmailVerified()
//                if (mfiFirebaseUser != null) {
//                    MainActivity mainActivity = (MainActivity) getActivity();
//                    mainActivity.loadMainAppScreen();
//                }
//            }
//        };


        final Button enterButton = view.findViewById(R.id.enterButtonLoginScreen);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = logEmail.getText().toString();
                String password = logPassword.getText().toString();

                if(!isEmailValid(email)){
                    logEmail.setError("אנא הכנס מייל תקין");
                } else if (email.isEmpty()) {
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
                                    startButtonAnimationEnter(enterButton,shakeAnim);
                                    //mainActivity.loadMainAppScreen();
                                }

                            }
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                }
                                //אם האימייל אינו קיים במערכת
                                catch (FirebaseAuthInvalidUserException invalidEmail) {
                                    Toast.makeText(getActivity(), "המייל אינו קיים במערכת אנא הירשם בכפתור מטה", Toast.LENGTH_LONG).show();
                                }
                                // אם הסיסמה שגויה
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

        final Button registerButton = view.findViewById(R.id.registerButtonLoginScreen);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonAnimationRegister(registerButton,shakeAnim);
            }
        });

        // אנימציה שכחתי סיסמה
        shakeAnim = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        final Button forgetPasswordButton = view.findViewById(R.id.forgetPasswordButtonLoginScreen);
        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonAnimationReset(forgetPasswordButton,shakeAnim);
            }
        });
        final Button contactEmail = view.findViewById(R.id.contactEmailButtonLoginScreen);

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

        final Button sharedAppButton = view.findViewById(R.id.sharedAppButtonLoginScreen);
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

    // פונקציה שמפעילה אנימה לכפתור שכחתי סיסמה שנלחץ לפני שהכפתור יבצע את פעולתו
    private void startButtonAnimationReset(Button btn, Animation anim) {
        btn.setAnimation(anim);
        btn.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadResetPasswordScreen();
            }
        });
    }

    private void startButtonAnimationEnter(Button btn, Animation anim) {
        btn.setAnimation(anim);
        btn.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadMainAppScreen();
            }
        });
    }

    private void startButtonAnimationRegister(Button btn, Animation anim) {
        btn.setAnimation(anim);
        btn.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }


            @Override
            public void onAnimationEnd(Animation animation) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.loadRegisterScreen();
            }
        });
    }
}




