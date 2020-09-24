package com.example.cycleurbike.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cycleurbike.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordScreen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText inputEmail;
    Button resetButton;
    private FirebaseAuth mAuth;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResetPasswordScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetPassword.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordScreen newInstance(String param1, String param2) {
        ResetPasswordScreen fragment = new ResetPasswordScreen();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reset_password, container, false);

        inputEmail = view.findViewById(R.id.enterMailTextResetScreen);
        resetButton = view.findViewById(R.id.resetButtonResetScreen);
        mAuth = FirebaseAuth.getInstance();


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(),"אנא הכנס אימייל",Toast.LENGTH_LONG).show();
                } else{
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText((getActivity()),"הוראות לביצוע איפוס סיסמה נשלחו למייל",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getActivity(),"איפוס סיסמה נכשל,אנא בדוק את כתובת האימייל שהזנת",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }


            }
        });

        return view;
    }
}